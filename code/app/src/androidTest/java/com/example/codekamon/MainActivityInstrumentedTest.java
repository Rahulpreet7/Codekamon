package com.example.codekamon;

import static org.junit.Assert.*;

import android.app.Activity;
import android.provider.Settings;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.remote.FirebaseClientGrpcMetadataProvider;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
     * Tests if clicking the leaderboard icon in main
     * goes to leaderboard activity properly.
     */
    @Test
    public void testMainToLeaderBoard(){
        solo.waitForActivity("MainActivity");
        solo.clickOnView(solo.getView(R.id.leaderboards_icon));
        solo.waitForActivity("leaderBoard");
        solo.assertCurrentActivity("Wrong Activity", leaderBoard.class);
    }

    /**
     * Tests if the username displayed in main is the players'
     * username.
     */
    @Test
    public void testCorrectUsername() throws InterruptedException {
        solo.waitForActivity("MainActivity");
        TextView username = (TextView) solo.getView(R.id.username_text);
        PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
        CountDownLatch latch = new CountDownLatch(1);
        String androidId = Settings.Secure.getString(solo.getCurrentActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        playersDB.getPlayer(androidId, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                assertTrue(success);
                assertTrue(item.getUserName().equals(username.getText().toString()));
                latch.countDown();
            }
        });
        if(!latch.await(30, TimeUnit.SECONDS)){
            throw new InterruptedException();
        }
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
