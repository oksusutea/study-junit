/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
 ***/
package chapter_11.util;

// text courtesy of Herman Melville (Moby Dick) from
// http://www.gutenberg.org/cache/epub/2701/pg2701.txt 

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.junit.*;
import java.util.logging.*;

import static chapter_11.util.ContainsMatches.containsMatches;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SearchTest {

    private static final String A_TITLE = "1";
    private InputStream stream;

    @Before
    public void turnOffLoging() {
        Search.LOGGER.setLevel(Level.OFF);
    }

    @After
    public void closeResources() throws IOException {
        stream.close();
    }

    @Test
    public void returnsMatchesShowingContextWhenSearchStringInContent()
        throws IOException {
        stream = streamOn("rest of text here"
            + "1234567890search term1234567890"
            + "more rest of text");
        // search
        Search search = new Search(stream, "search term", A_TITLE);
        search.setSurroundingCharacterCount(10);

        search.execute();
        //assertFalse(search.errored());

        assertThat(search.getMatches(), containsMatches(new Match[]
            {new Match(A_TITLE, "search term",
                "1234567890search term1234567890")}));
    }

    @Test
    public void noMatchesReturnedWhenSearchStringNotInContent() {
        stream = streamOn("any text");
        Search search = new Search(stream, "text that doesn't match", A_TITLE);

        search.execute();

        //assertThat(search.getMatches().size(), equalTo(0));
        assertTrue(search
            .getMatches()
            .isEmpty());  //누락된 추상화. 비어있다는 개념을 표현하기 위해 단언을 바꾸면 크기 비교를 하는 불필요한 노력을 줄일 수 있다.
    }

    private InputStream streamOn(String pageContent) {
        return new ByteArrayInputStream(pageContent.getBytes());
    }

    @Test
    public void returnsErroredWhenUnableToReadStream() {
        stream = createStreamThrowingErrorWhenRead();
        Search search = new Search(stream, "", "");

        search.execute();

        assertTrue(search.errored());
    }

    private InputStream createStreamThrowingErrorWhenRead() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
    }

    @Test
    public void erroredReturnsFalseWhenReadSucceeds() {
        stream = streamOn("");
        Search search = new Search(stream, "", "");

        search.execute();

        assertFalse(search.errored());
    }
}
