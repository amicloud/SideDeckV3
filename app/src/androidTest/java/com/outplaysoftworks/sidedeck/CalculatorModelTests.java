package com.outplaysoftworks.sidedeck;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
//@LargeTest
public class CalculatorModelTests {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

    @Test
    public void addToEnteredValueWhenNumberClicked() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton4)).perform(click());
        onView(withId(R.id.LpCalculatorButton5)).perform(click());
        onView(withId(R.id.LpCalculatorButton6)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("123456")));

    }
}