package com.example.codekamon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.provider.Settings;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
/**
 * This class "MapsActivityTest" contains the following tests:<br>
 * - Checks if the Back button in MapsActivity goes back to previous activity (i.e MainActivity).<br>
 * - Checks if adapter view list can be clicked short to zoom it, and long to check comments.<br>
 * - Check if search bar shows the proper information.<br>
 *
 *  Author(s): Elisandro Cruz Martinez
 */
public class MapsActivityTest {
    private Solo solo;
    private FirebaseFirestore firestore;

    /**
     * This private method "setUpFireBaseMocked" is used to set up the mocked firebase.
     * @throws InterruptedException
     */
    private void setUpFireBaseMocked() throws InterruptedException {
        //Set up
        firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("127.0.0.1",8080);
        QRCode code = new QRCode("dummyContent");
        code.setName("dummyName");
        code.setLocation(53,-112);
        QRCodesDB codesDB = new QRCodesDB(firestore);
        codesDB.addQRCode(code, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                return;
            }
        });
        TimeUnit.SECONDS.sleep(2);

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
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * This private method "eraseFireBaseMocked" is used to destroy the firebase in order to do the next test.
     * @throws InterruptedException
     */
    private void eraseFireBaseMocked() throws InterruptedException{
        solo.finishOpenedActivities();
        firestore.terminate();
        TimeUnit.SECONDS.sleep(2);
    }

    @BeforeClass
    public static void terminateBeforeTests() throws InterruptedException{
        FirebaseFirestore.getInstance().terminate();
        TimeUnit.SECONDS.sleep(2);
    }
    /**
     * this test "TestBackButton" checks if the buttons in MapsActivity go back to previous activity
     * @throws InterruptedException
     */
    @Test
    public void testBackButtonMocked() throws InterruptedException {
        setUpFireBaseMocked();

        ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, true);
        Intent intent = new Intent();
        rule.launchActivity(intent);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("Wrong Activity, not in MapsActivity", MapsActivity.class);
        TimeUnit.SECONDS.sleep(5);
        solo.clickOnView(solo.getView(R.id.Back));

        eraseFireBaseMocked();
    }

    /**
     * The test case "TestSearchViewAndDeletesMocked" checks if the search bar changes the contents in the search view.
     * @throws InterruptedException
     */
    @Test
    public void testSearchViewAddAndDeletesMocked() throws InterruptedException {
        setUpFireBaseMocked();

        int sec = 10;

        ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, true);
        Intent intent = new Intent();
        rule.launchActivity(intent);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("Wrong Activity, not in MapsActivity", MapsActivity.class);
        TimeUnit.SECONDS.sleep(sec);


        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchView search = (SearchView) solo.getView(R.id.searchView);
                search.setQuery("dummyName", true);
                search.clearFocus();
            }
        });
        TimeUnit.SECONDS.sleep(sec);

        ListView list = (ListView) solo.getView(R.id.list_view_codes);
        int n = list.getAdapter().getCount();
        assertTrue("List is still empty", n == 1);
        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchView search = (SearchView) solo.getView(R.id.searchView);
                search.setQuery("", false);
                search.clearFocus();
            }
        });

        eraseFireBaseMocked();
    }

    /**
     * The test case "testClickingOnAdapterMocked" check if the adapter is populated, is clickable, and you can see the comments.
     * @throws InterruptedException
     */
    @Test
    public void testClickingOnAdapterMocked() throws InterruptedException {
        setUpFireBaseMocked();
        // set up the timer seconds.
        int sec = 10;

        ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, true);
        Intent intent = new Intent();
        rule.launchActivity(intent);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // check if the activity map is opened
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("Wrong Activity, not in MapsActivity", MapsActivity.class);
        TimeUnit.SECONDS.sleep(sec);

        // check the search view can actually type a query for the search bar
        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchView search = (SearchView) solo.getView(R.id.searchView);
                search.setQuery("dummy", true);
                search.clearFocus();
            }
        });

        TimeUnit.SECONDS.sleep(sec);

        // Check if the list view contains the "dummyName" code
        ListView list = (ListView) solo.getView(R.id.list_view_codes);
        int n = list.getAdapter().getCount();
        assertTrue("List is still empty", n == 1);
        Object searched = list.getAdapter().getItem(n - 1);
        String searchedWord = ((GeoCodeLocation) searched).getName();
        assertEquals(searchedWord, "dummyName");

        solo.clickInList(n);

        // Check if holding the button and checking the comments goes to QRCodeActivity
        TimeUnit.SECONDS.sleep(sec);
        solo.clickLongInList(n);
        solo.waitForActivity("QRCodeActivity");
        solo.assertCurrentActivity("Wrong Activity", QRCodeActivity.class);
        TextView name = (TextView) solo.getView(R.id.qr_code_name_text);
        assertTrue(searchedWord.equals(name.getText().toString()));

        eraseFireBaseMocked();
    }
}
