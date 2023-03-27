package com.example.codekamon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

public class CommentsInstrumentedTest {
    private Solo solo;

    @Test
    public void testCommenting() throws InterruptedException {
        //Set up
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("127.0.0.1",8080);
        QRCode code = new QRCode("dummyContent");
        code.setName("dummyName");
        QRCodesDB codesDB = new QRCodesDB(firestore);
        codesDB.addQRCode(code, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                return;
            }
        });
        Thread.sleep(2000);

        HashMap<String, String> map = new HashMap<>();
        map.put("dummyName", "dummyContent");
        Player player = new Player();
        String deviceId = Settings.Secure.getString(InstrumentationRegistry.getInstrumentation().getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        player.setAndroidId(deviceId);
        player.setUserName("dummyName2");
        player.setHighestScore(21);
        player.setLowestScore(34);
        player.setNumScanned(1);
        player.setEmail("here");
        player.setUserRankSimple(1);
        player.setTotalScore(45);
        player.setPlayerCodes(map);
        PlayersDB playersDB = new PlayersDB(firestore, true);
        playersDB.addPlayer(player, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                return;
            }
        });
        Thread.sleep(2000);

        ActivityTestRule<CommentsActivity> rule = new ActivityTestRule<>(CommentsActivity.class, true, true);
        Intent intent = new Intent();
        intent.putExtra("QRCode name", "dummyName");
        rule.launchActivity(intent);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());


        solo.waitForActivity("CommentsActivity");
        solo.assertCurrentActivity("Wrong Activity", CommentsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_comment_edittext), "This is a test comment.");
        Thread.sleep(1000);
        solo.clickOnView(solo.getView(R.id.add_comment_button));
        assertTrue(solo.searchText("dummyName2"));
        assertTrue(solo.searchText("This is a test comment."));

        solo.finishOpenedActivities();
        firestore.terminate();
        Thread.sleep(2000);
    }

    @Test
    public void testUnableToComment() throws InterruptedException {
        //Set up
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("127.0.0.1",8080);
        QRCode code = new QRCode("dummyContent");
        code.setName("dummyName");
        QRCodesDB codesDB = new QRCodesDB(firestore);
        codesDB.addQRCode(code, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                return;
            }
        });
        Thread.sleep(2000);

        HashMap<String, String> map = new HashMap<>();
        map.put("dummyName", "dummyContent");
        Player player = new Player();
        String deviceId = Settings.Secure.getString(InstrumentationRegistry.getInstrumentation().getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        player.setAndroidId(deviceId);
        player.setUserName("dummyName2");
        player.setHighestScore(21);
        player.setLowestScore(34);
        player.setNumScanned(1);
        player.setEmail("here");
        player.setUserRankSimple(1);
        player.setTotalScore(45);
        player.setPlayerCodes(new HashMap<>());
        PlayersDB playersDB = new PlayersDB(firestore, true);
        playersDB.addPlayer(player, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                return;
            }
        });
        Thread.sleep(2000);

        ActivityTestRule<CommentsActivity> rule = new ActivityTestRule<>(CommentsActivity.class, true, true);
        Intent intent = new Intent();
        intent.putExtra("QRCode name", "dummyName");
        rule.launchActivity(intent);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        //Test
        solo.waitForActivity("CommentsActivity");
        solo.assertCurrentActivity("Wrong Activity", CommentsActivity.class);

        Thread.sleep(2000);
        LinearLayout linearLayout = (LinearLayout) solo.getView(R.id.add_comment_layout);
        assertTrue(linearLayout.getVisibility() == View.INVISIBLE);

        //Teardown
        solo.finishOpenedActivities();
        firestore.terminate();
        Thread.sleep(2000);
    }


}
