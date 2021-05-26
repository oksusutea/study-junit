/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
 ***/
package chapter_05.controller;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import chapter_05.iloveyouboss.controller.QuestionController;
import chapter_05.iloveyouboss.domain.Question;
import java.time.*;
import java.util.*;
import org.junit.*;

public class QuestionControllerTest {

    private QuestionController controller;

    @Before
    public void create() {
        controller = new QuestionController();
        controller.deleteAll();
    }

    @Test
    public void questionAnswersDateAdded() {
        Instant now = new Date().toInstant();
        controller.setClock(Clock.fixed(now, ZoneId.of("America/Denver")));
        int id = controller.addBooleanQuestion("text");

        Question question = controller.find(id);
        // EntityManager에서 해당 ID로 값을 찾지 못해 Quesion 클래스가 NULL로 떨어짐
        assertThat(question.getCreateTimestamp(), equalTo(now));
    }

}
