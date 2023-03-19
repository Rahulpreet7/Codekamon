package com.example.codekamon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class MapsActivityTest {
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

    @Test
    public void testOpeningMapsActivity(){
        solo.waitForActivity("MainActivity");
        solo.clickOnView(solo.getView(R.id.map_icon));
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
    }
    @Test
    public void testBackButton() throws InterruptedException {
        solo.waitForActivity("MainActivity");
        solo.clickOnView(solo.getView(R.id.map_icon));
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("Wrong Activity, not in MapsActivity", MapsActivity.class);
        TimeUnit.SECONDS.sleep(5);
        solo.clickOnView(solo.getView(R.id.Back));
        solo.assertCurrentActivity("Not back in MainActivity", MainActivity.class);
    }
    @Test
    public void testClickingOnAdapter() throws InterruptedException {
         int sec = 10;
         solo.waitForActivity("MainActivity");
         solo.clickOnView(solo.getView(R.id.map_icon));
         solo.waitForActivity("MapsActivity");
         solo.assertCurrentActivity("Wrong Activity, not in MapsActivity", MapsActivity.class);
         TimeUnit.SECONDS.sleep(sec);
         solo.getCurrentActivity().runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 SearchView search = (SearchView) solo.getView(R.id.searchView);
                 search.setQuery("Alabama", true);
                 search.clearFocus();
             }
         });
         TimeUnit.SECONDS.sleep(sec);
         ListView list = (ListView) solo.getView(R.id.list_view_codes);
         int n = list.getAdapter().getCount();
         assertTrue("List is still empty", n == 1);
         Object searched = list.getAdapter().getItem(n - 1);
         String searchedWord = ((GeoCodeLocation) searched).getName();
         assertEquals(searchedWord, "Alabama");
         solo.clickInList(n);
         TimeUnit.SECONDS.sleep(sec);
         solo.clickLongInList(n);
         solo.waitForActivity("QRCodeActivity");
         solo.assertCurrentActivity("Wrong Activity", QRCodeActivity.class);
         TextView name = (TextView) solo.getView(R.id.qr_code_name_text);
         assertTrue(searchedWord.equals(name.getText().toString()));
    }
    @Test
    public void testSearchViewAddAndDeletes() throws InterruptedException {
        int sec = 10;
        solo.waitForActivity("MainActivity");
        solo.clickOnView(solo.getView(R.id.map_icon));
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("Wrong Activity, not in MapsActivity", MapsActivity.class);
        TimeUnit.SECONDS.sleep(sec);
        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchView search = (SearchView) solo.getView(R.id.searchView);
                search.setQuery("Alabama", true);
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
    }
}
