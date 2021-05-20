/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
 ***/
package chapter_04.scratch;

import org.junit.*;

public class AssertMoreTest {

    @BeforeClass
    public static void initializeSomethingReallyExpensive() {
        // ...
    }

    @AfterClass
    public static void cleanUpSomethingReallyExpensive() {
        // ...
    }

    @Before
    public void createAccount() {
        // ...
    }

    @After
    public void closeConnections() {
        // ...
    }

    @Test
    public void depositIncreasesBalance() {
        // ...
    }

    @Test
    public void hasPositiveBalance() {
        // ...
    }
}
/*
 * 실행 흐름 :
 * @BeforeClass initializeSomethingReallyExpensive
 * @Before createAccount
 * @Test depositIncreasesBalance
 * @After closeConnections
 * @Before createAccount
 * @Test hasPositiveBalance
 * @After closeConnections
 * @AfterClass cleanUpSomethingReallyExpensive
 * JUnit 4 : @BeforeClass == @BeforeAll  : JUnit5
 * JUnit 5 : @AfterClass == @AfterAll : JUnit5
 *
 *
 */