package com.outplaysoftworks.sidedeck;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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

    @Before
    public void initSettingsForTesting(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.KEYplayerOneDefaultNameSetting), "Player 1");
        editor.putString(context.getString(R.string.KEYplayerTwoDefaultNameSetting), "Player 2");
        editor.putInt(context.getString(R.string.KEYdefaultLpSetting),8000);

    }

    @Test
    public void addsToEnteredValueWhenNumberClicked() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton4)).perform(click());
        onView(withId(R.id.LpCalculatorButton5)).perform(click());
        onView(withId(R.id.LpCalculatorButton6)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("123456")));
    }

    @Test
    public void doesNotAddToEnteredValueIfAtSixDigits(){
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton4)).perform(click());
        onView(withId(R.id.LpCalculatorButton5)).perform(click());
        onView(withId(R.id.LpCalculatorButton6)).perform(click());
        onView(withId(R.id.LpCalculatorButton6)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("123456")));
    }

    @Test
    public void setsDefaultLpFromSettings(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int defaultLp = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.KEYdefaultLpSetting), "8000"));
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText(Integer.toString(defaultLp))));
    }

    @Test
    public void setsPlayerNamesFromSettings(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String player1Name = sharedPreferences.getString(context.getString(R.string.KEYplayerOneDefaultNameSetting), context.getString(R.string.playerOne));
        onView(withId(R.id.LpCalculatorTextPlayer1Name)).check(matches(withText(player1Name)));
        String player2Name = sharedPreferences.getString(context.getString(R.string.KEYplayerTwoDefaultNameSetting), context.getString(R.string.playerTwo));
        onView(withId(R.id.LpCalculatorTextPlayer2Name)).check(matches(withText(player2Name)));
    }
}