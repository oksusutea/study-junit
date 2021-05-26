package chapter_01.iloveyouboss;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import org.junit.Test;

public class ScoreCollectionTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    @Test
    public void answerArithmeticMeanOfTwoNumber9() {
        // 준비
        ScoreCollection collection = new ScoreCollection();
        collection.add(() -> 5);
        collection.add(() -> 7);

        // 실행
        int actualResult = collection.arithmeticMean();

        // 단언
        assertThat(actualResult, equalTo(6));
    }

}