package com.outplaysoftworks.sidedeck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
//@LargeTest
public class LpCalculatorTests {
    private int durationSleepPadding = 250;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @SuppressLint("ApplySharedPref")
    @Before
    public void initSettingsForTesting() {
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.KEYallowNegativeLp), false);
        editor.putString(context.getString(R.string.KEYplayerOneDefaultNameSetting), "Player 1");
        editor.putString(context.getString(R.string.KEYplayerTwoDefaultNameSetting), "Player 2");
        editor.putString(context.getString(R.string.KEYdefaultLpSetting), "8000");
        editor.commit();
    }

    @After
    public void cleanUpModel(){
        LpCalculatorModel.clearEnteredValue();
        LpCalculatorModel.setPlayer1Name(LpCalculatorModel.getPlayer1Name());
        LpCalculatorModel.setPlayer1Lp(LpCalculatorModel.getPlayer1Lp());
        LpCalculatorModel.setPlayer2Lp(LpCalculatorModel.getPlayer2Lp());
        LpCalculatorModel.setAllowsNegativeLp(false);
        LpCalculatorModel.setLpDefault(8000);
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
    public void doesNotAddToEnteredValueIfAtSixDigits() {
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
    public void onClickClearButton_clearEnteredValue() {
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
    public void onClickEnteredValue_clearEnteredValue() {
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
    public void onClickDoubleZero_appendsDoubleZero() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton00)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("100")));
    }

    @Test
    public void onClickTripleZero_appendsTripleZero() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("1000")));
    }

    @Test
    public void onClickPlusP1_addsToP1Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("9000")));
    }

    @Test
    public void onClickPlusP2_addsToP2Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonPlusPlayer2)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("9000")));
    }

    @Test
    public void onClickMinusP1_subtractsFromP1Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("7000")));
    }

    @Test
    public void onClickMinusP2_subtractsFromP2Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer2)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("7000")));
    }

    @Test
    public void onClickUndo_unexecutesLastAddition() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("9000")));
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("8000")));
    }

    @Test
    public void onClickUndo_unexecutesLastSubtraction() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer2)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("7000")));
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("8000")));
    }

    @Test
    public void onClickUndo_doesNothingIfNoCommandsInHistory() {
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
    }

    @Test
    public void onClickUndo_unexecutesLastTurnIncrement(){
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")));
    }

    @Test
    public void onClickUndo_unexecutesLastTurnDecrement(){
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick());
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n3")));
    }

    @Test
    public void onInit_setsUpTurnDisplay() {
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")));
    }

    @Test
    public void onClickTurn_incrementsTurnDisplay() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n2")));
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n3")));
    }

    @Test
    public void onLongClickTurn_decrementTurnDisplay() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick());
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n2")));
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick());
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")));
    }

    @Test
    public void onLongClickTurn_doNothingIfOnTurn1() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick());
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")));
    }

    @Test
    public void onClickBackWhenPlayer1NameFocused_unfocusesNameInsteadOfClosing() {
        onView(withId(R.id.LpCalculatorTextPlayer1Name)).perform(click());
        pressBack();
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickBackWhenPlayer2NameFocused_unfocusesNameInsteadOfClosing() {
        onView(withId(R.id.LpCalculatorTextPlayer2Name)).perform(click());
        pressBack();
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickReset_resetsToInitialSettingsKeepingPlayerNames() {
        //Mess everyting up
        onView(withId(R.id.LpCalculatorButton1)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton2)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer2)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer1Name)).perform(clearText()).perform(typeText("Test"));
        onView(withId(R.id.LpCalculatorTextPlayer2Name)).perform(clearText()).perform(typeText("Test"));
        pressBack();

        //Reset
        onView(withId(R.id.LpCalculatorButtonReset)).perform(click());

        //Check everything
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("8000")));
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("8000")));
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")));
        onView(withId(R.id.LpCalculatorTextPlayer1Name)).check(matches(withText("Test")));
        onView(withId(R.id.LpCalculatorTextPlayer2Name)).check(matches(withText("Test")));
    }

    @Test
    public void onClickSubtract_goesToZeroIfWillMakeNegativeAndAllowNegativeLpIsDisabled() {
        LpCalculatorModel.setAllowsNegativeLp(false);
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButton0)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATIONLONG + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("0")));
    }

    @Test
    public void onClickSubtract_goesNegativeIfWillMakeNegativeAndAllowNegativeLpIsEnabled() {
        LpCalculatorModel.setAllowsNegativeLp(true);
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButton0)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("-22000")));
    }

    @Test
    public void animatesTextViewOnClickPlus(){
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(not(withText("11000"))));
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("11000")));
    }

    @Test
    public void animatesTextViewOnClickMinus(){
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(not(withText("5000"))));
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("5000")));
    }

    @Test
    public void animatesTextViewLongerDurationOnClickMinusToZero(){
        onView(withId(R.id.LpCalculatorButton8)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(not(withText("0"))));
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);//Wait some additional time
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("0")));
    }

    @Test
    public void doesNotAnimateTextViewOnUndo(){
        onView(withId(R.id.LpCalculatorButton3)).perform(click());
        onView(withId(R.id.LpCalculatorButton000)).perform(click());
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click());
        SystemClock.sleep(AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding);
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click());
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("8000")));
    }
}