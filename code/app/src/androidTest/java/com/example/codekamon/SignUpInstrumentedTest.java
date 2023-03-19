package com.example.codekamon;

import static org.junit.Assert.assertFalse;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class SignUpInstrumentedTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class, true, true);

    @BeforeClass
    public static void setUp(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("127.0.0.1",8080);
    }

    @Before
    public void before() throws InterruptedException {

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    @Test
    public void testSignUp() throws InterruptedException {
        solo.waitForActivity("SignUpActivity");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.pick_username_edit_text), "Godrick");
        solo.enterText((EditText) solo.getView(R.id.email_edit_text), "godrick@grafted.com");
        solo.clickOnView(solo.getView(R.id.sign_up_button));
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.searchText("Godrick");
    }
}
