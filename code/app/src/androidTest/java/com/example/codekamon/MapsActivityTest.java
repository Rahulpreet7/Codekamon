package com.example.codekamon;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import android.widget.EditText;
import android.widget.ListView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MapsActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start(){
        Activity activity = rule.getActivity();
    }

    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        //implement the other garbage
    }

}
