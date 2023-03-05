package com.example.codekamon;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Instrument test for the main screen.
 */
public class MainActivityInstrumentedTest {

    /**
     * Holds the robot doing the tests.
     */
    private Solo solo;

    /**
     * Holds the rule for the test.
     */
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    /**
     * Set up before each test is executed.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Checks whether clicking the map works leads to MapsActivity.
     */
    @Test
    public void checkMapPressed(){
        solo.clickOnView(solo.getView(R.id.map_icon));
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
    }

    /**
     * Checks whether clicking scan icon leads to QRCodeScanActivity.
     */
    @Test
    public void checkAddCodePressed(){
        solo.clickOnView(solo.getView(R.id.add_code_icon));
        solo.waitForActivity("QRCodeScanActivity");
        solo.assertCurrentActivity("Wrong Activity", QRCodeScanActivity.class);
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
