package com.outplaysoftworks.sidedeck

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import android.preference.PreferenceManager
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not

@RunWith(AndroidJUnit4::class)
//@LargeTest
class LpCalculatorTests {
    private val durationSleepPadding = 250

    @Rule
    var mActivityRule = ActivityTestRule<MainActivity>(
            MainActivity::class.java)


    @SuppressLint("ApplySharedPref")
    @Before
    fun initSettingsForTesting() {
        val context = getInstrumentation().targetContext
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean(context.getString(R.string.KEYallowNegativeLp), false)
        editor.putString(context.getString(R.string.KEYplayerOneDefaultNameSetting), "Player 1")
        editor.putString(context.getString(R.string.KEYplayerTwoDefaultNameSetting), "Player 2")
        editor.putString(context.getString(R.string.KEYdefaultLpSetting), "8000")
        editor.commit()
    }

    @After
    fun cleanUpModel() {
        LpCalculatorModel.clearEnteredValue()
        LpCalculatorModel.player1Name = LpCalculatorModel.player1Name
        LpCalculatorModel.player1Lp = LpCalculatorModel.player1Lp
        LpCalculatorModel.player2Lp = LpCalculatorModel.player2Lp
        LpCalculatorModel.allowsNegativeLp = false
        LpCalculatorModel.lpDefault = 8000
    }

    @Test
    fun addsToEnteredValueWhenNumberClicked() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton2)).perform(click())
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton4)).perform(click())
        onView(withId(R.id.LpCalculatorButton5)).perform(click())
        onView(withId(R.id.LpCalculatorButton6)).perform(click())
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("123456")))
    }

    @Test
    fun doesNotAddToEnteredValueIfAtSixDigits() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton2)).perform(click())
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton4)).perform(click())
        onView(withId(R.id.LpCalculatorButton5)).perform(click())
        onView(withId(R.id.LpCalculatorButton6)).perform(click())
        onView(withId(R.id.LpCalculatorButton6)).perform(click())
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("123456")))
    }

    @Test
    fun onClickClearButton_clearEnteredValue() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton2)).perform(click())
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton4)).perform(click())
        onView(withId(R.id.LpCalculatorButton5)).perform(click())
        onView(withId(R.id.LpCalculatorButton6)).perform(click())
        onView(withId(R.id.LpCalculatorButtonClear)).perform(click())
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("")))
    }

    @Test
    fun onClickEnteredValue_clearEnteredValue() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton2)).perform(click())
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton4)).perform(click())
        onView(withId(R.id.LpCalculatorButton5)).perform(click())
        onView(withId(R.id.LpCalculatorButton6)).perform(click())
        onView(withId(R.id.LpCalculatorTextEnteredValue)).perform(click())
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("")))
    }

    @Test
    fun onClickDoubleZero_appendsDoubleZero() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton00)).perform(click())
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("100")))
    }

    @Test
    fun onClickTripleZero_appendsTripleZero() {
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(withText("1000")))
    }

    @Test
    fun onClickPlusP1_addsToP1Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("9000")))
    }

    @Test
    fun onClickPlusP2_addsToP2Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonPlusPlayer2)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("9000")))
    }

    @Test
    fun onClickMinusP1_subtractsFromP1Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("7000")))
    }

    @Test
    fun onClickMinusP2_subtractsFromP2Lp() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer2)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("7000")))
    }

    @Test
    fun onClickUndo_unexecutesLastAddition() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("9000")))
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("8000")))
    }

    @Test
    fun onClickUndo_unexecutesLastSubtraction() {
        //Add 1000 to entered value
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer2)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("7000")))
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("8000")))
    }

    @Test
    fun onClickUndo_doesNothingIfNoCommandsInHistory() {
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
    }

    @Test
    fun onClickUndo_unexecutesLastTurnIncrement() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")))
    }

    @Test
    fun onClickUndo_unexecutesLastTurnDecrement() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick())
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n3")))
    }

    @Test
    fun onInit_setsUpTurnDisplay() {
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")))
    }

    @Test
    fun onClickTurn_incrementsTurnDisplay() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n2")))
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n3")))
    }

    @Test
    fun onLongClickTurn_decrementTurnDisplay() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick())
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n2")))
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick())
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")))
    }

    @Test
    fun onLongClickTurn_doNothingIfOnTurn1() {
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(longClick())
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")))
    }

    @Test
    fun onClickBackWhenPlayer1NameFocused_unfocusesNameInsteadOfClosing() {
        onView(withId(R.id.LpCalculatorTextPlayer1Name)).perform(click())
        pressBack()
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(isDisplayed()))
    }

    @Test
    fun onClickBackWhenPlayer2NameFocused_unfocusesNameInsteadOfClosing() {
        onView(withId(R.id.LpCalculatorTextPlayer2Name)).perform(click())
        pressBack()
        onView(withId(R.id.LpCalculatorTextEnteredValue)).check(matches(isDisplayed()))
    }

    @Test
    fun onClickReset_resetsToInitialSettingsKeepingPlayerNames() {
        //Mess everyting up
        onView(withId(R.id.LpCalculatorButton1)).perform(click())
        onView(withId(R.id.LpCalculatorButton2)).perform(click())
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click())
        onView(withId(R.id.LpCalculatorButton2)).perform(click())
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton2)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer2)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorButtonTurn)).perform(click())
        onView(withId(R.id.LpCalculatorTextPlayer1Name)).perform(clearText()).perform(typeText("Test"))
        onView(withId(R.id.LpCalculatorTextPlayer2Name)).perform(clearText()).perform(typeText("Test"))
        pressBack()

        //Reset
        onView(withId(R.id.LpCalculatorButtonReset)).perform(click())

        //Check everything
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("8000")))
        onView(withId(R.id.LpCalculatorTextPlayer2Lp)).check(matches(withText("8000")))
        onView(withId(R.id.LpCalculatorButtonTurn)).check(matches(withText("Turn\n1")))
        onView(withId(R.id.LpCalculatorTextPlayer1Name)).check(matches(withText("Test")))
        onView(withId(R.id.LpCalculatorTextPlayer2Name)).check(matches(withText("Test")))
    }

    @Test
    fun onClickSubtract_goesToZeroIfWillMakeNegativeAndAllowNegativeLpIsDisabled() {
        LpCalculatorModel.allowsNegativeLp = false
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButton0)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATIONLONG + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("0")))
    }

    @Test
    fun onClickSubtract_goesNegativeIfWillMakeNegativeAndAllowNegativeLpIsEnabled() {
        LpCalculatorModel.allowsNegativeLp = true
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButton0)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("-22000")))
    }

    @Test
    fun animatesTextViewOnClickPlus() {
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonPlusPlayer1)).perform(click())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(not<View>(withText("11000"))))
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("11000")))
    }

    @Test
    fun animatesTextViewOnClickMinus() {
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(not<View>(withText("5000"))))
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("5000")))
    }

    @Test
    fun animatesTextViewLongerDurationOnClickMinusToZero() {
        onView(withId(R.id.LpCalculatorButton8)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(not<View>(withText("0"))))
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())//Wait some additional time
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("0")))
    }

    @Test
    fun doesNotAnimateTextViewOnUndo() {
        onView(withId(R.id.LpCalculatorButton3)).perform(click())
        onView(withId(R.id.LpCalculatorButton000)).perform(click())
        onView(withId(R.id.LpCalculatorButtonMinusPlayer1)).perform(click())
        SystemClock.sleep((AppConstants.LPCHANGEANIMATIONDURATION + durationSleepPadding).toLong())
        onView(withId(R.id.LpCalculatorButtonUndo)).perform(click())
        onView(withId(R.id.LpCalculatorTextPlayer1Lp)).check(matches(withText("8000")))
    }
}