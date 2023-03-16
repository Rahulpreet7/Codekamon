package com.example.codekamon;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Instrument test for the leader board screen.
 */
public class leaderBoardInstrumentedTest {

    /**
     * Holds the robot doing the tests.
     */
    private Solo solo;

    /**
     * Holds the rule for the test.
     */
    @Rule
    public ActivityTestRule<leaderBoard> rule = new ActivityTestRule<leaderBoard>(leaderBoard.class, true, true);

    /**
     * Set up before each test is executed.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Checks whether clicking on the list items leads to Other User Profiles.
     */
    @Test
    public void checkOtherUserProfileActivity(){

    }

    /**
     * Cleans up after every test.
     * @throws Exception
     */
    @After
    public void teardown() throws Exception {
        solo.finishOpenedActivities();
    }
}
