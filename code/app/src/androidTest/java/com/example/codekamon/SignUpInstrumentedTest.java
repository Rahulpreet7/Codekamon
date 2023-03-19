package com.example.codekamon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SignUpInstrumentedTest {
    private Solo solo;
    static FirebaseFirestore firestore;
    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class, true, true);

    @BeforeClass
    public static void setUp() throws InterruptedException {
        FirebaseFirestore.getInstance().terminate();
        Thread.sleep(2000);
        firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("127.0.0.1",8080);
    }

    @Before
    public void before() throws InterruptedException{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Tests sign up when username is taken.
     * Tests sign up when the username is not taken.
     * Tests reopening the app after successfully signing up.
     *
     * @throws InterruptedException Threads sleep interrupted
     */
    @Test
    public void testSignUp() throws InterruptedException {
        Player player = new Player();
        player.setAndroidId("testAndroidId");
        player.setUserName("Godrick");
        PlayersDB playersDB = new PlayersDB(firestore);
        CountDownLatch latch = new CountDownLatch(1);
        playersDB.addPlayer(player, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                return;
            }
        });
        Thread.sleep(2000);

        //Test sign up username taken
        solo.waitForActivity("SignUpActivity");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.pick_username_edit_text), "Godrick");
        solo.enterText((EditText) solo.getView(R.id.email_edit_text), "godrick@grafted.com");
        solo.clickOnView(solo.getView(R.id.sign_up_button));
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);


        playersDB.deletePlayer(player.getAndroidId(), new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                return;
            }
        });
        Thread.sleep(2000);

        //Test sign up user name not taken
        solo.clickOnView(solo.getView(R.id.sign_up_button));
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.searchText("Godrick");

        //Test re open app
        Intent intent = new Intent(solo.getCurrentActivity(), SignUpActivity.class);
        rule.launchActivity(intent);
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.searchText("Godrick");
        FirebaseFirestore.getInstance().terminate();
        Thread.sleep(2000);
    }

}
