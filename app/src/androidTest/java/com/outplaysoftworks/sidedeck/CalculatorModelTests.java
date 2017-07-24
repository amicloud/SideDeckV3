package com.outplaysoftworks.sidedeck;

import android.annotation.SuppressLint;
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

    @SuppressLint("ApplySharedPref")
    @Before
    public void initSettingsForTesting(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.KEYplayerOneDefaultNameSetting), "Player 1");
        editor.putString(context.getString(R.string.KEYplayerTwoDefaultNameSetting), "Player 2");
        editor.putString(context.getString(R.string.KEYdefaultLpSetting), "8000");
        editor.commit();
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

    @Test
    public void onClickClearButton_clearEnteredValue(){
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton4)).perform(click());
        onView(withId(R.id.LpCalculatorButton5)).perform(click());
        onView(withId(R.id.LpCalculatorButton6)).perform(click());
        onView(withId(R.id.LpCalculatorButtonClear)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("")));
    }

    @Test
    public void onClickEnteredValue_clearEnteredValue(){
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton4)).perform(click());
        onView(withId(R.id.LpCalculatorButton5)).perform(click());
        onView(withId(R.id.LpCalculatorButton6)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("")));
    }

    @Test
    public void onClickDoubleZero_appendsDoubleZero(){
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton00)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("100")));
    }

    @Test
    public void onClickTripleZero_appendsTripleZero(){
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("1000")));
    }

    @Test
    public void onClickPlusP1_addsToP1Lp(){
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("9000")));
    }

    @Test
    public void onClickPlusP2_addsToP2Lp(){
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonPlusPlayer2)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("9000")));
    }

    @Test
    public void onClickMinusP1_subtractsFromP1Lp(){
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("7000")));
    }

    @Test
    public void onClickMinusP2_subtractsFromP2Lp(){
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer2)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("7000")));
    }
}