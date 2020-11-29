package com.example.trackcovid;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(LoginActivity.class);
    @Test
    public void test1failedRegisterTest() {
        onView(withId(R.id.EmailField)).perform(typeText("bhmailom"), closeSoftKeyboard());
        onView(withId(R.id.PasswordField)).perform(typeText("password"), closeSoftKeyboard());
        onView((withId(R.id.registerButton))).perform(click());
    }

    @Test
    public void test2failedLoginTest(){
        onView(withId(R.id.EmailField)).perform(clearText(), typeText("bhddsd123@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordField)).perform(clearText(), typeText("wor"), closeSoftKeyboard());
        onView((withId(R.id.loginButton))).perform(click());
    }

    @Test
    public void test3passRegisterTest() {
        onView(withId(R.id.EmailField)).perform(clearText(), typeText("bhddsd123@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordField)).perform(clearText(), typeText("password1234"), closeSoftKeyboard());
        onView((withId(R.id.registerButton))).perform(click());
    }
}