package com.example.trackcovid;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class LocationSelectorActivityTest {
    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(LocationSelectorActivity.class);
    @Test
    public void selectCountry() {
        onView(withId(R.id.country_spinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.country_spinner)).check(matches(withSpinnerText(containsString("Canada"))));
    }
    @Test
    public void selectProv() {
        onView(withId(R.id.province_spinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.province_spinner)).check(matches(withSpinnerText(containsString("Ontario"))));
    }
    @Test
    public void selectBoth() {
        onView(withId(R.id.country_spinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.province_spinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.country_spinner)).check(matches(withSpinnerText(containsString("Canada"))));
        onView(withId(R.id.province_spinner)).check(matches(withSpinnerText(containsString("Alberta"))));
    }
}