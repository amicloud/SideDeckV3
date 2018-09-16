package com.outplaysoftworks.sidedeck

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

import junit.framework.Assert.assertEquals

/**
 * .
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class LpCalculatorRoboTests {
    private var activity: MainActivity? = null
    private val durationSleepPadding = 250

    @Before
    fun initSettingsForTesting() {
        val context = RuntimeEnvironment.application.applicationContext
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean(context.getString(R.string.KEYallowNegativeLp), false)
        editor.putString(context.getString(R.string.KEYplayerOneDefaultNameSetting), "Player 1")
        editor.putString(context.getString(R.string.KEYplayerTwoDefaultNameSetting), "Player 2")
        editor.putString(context.getString(R.string.KEYdefaultLpSetting), "8000")
        editor.commit()
        activity = Robolectric.setupActivity(MainActivity::class.java!!)
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
    fun onClickButton1_appends1ToEnteredValue() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        val tv = activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView
        assertEquals("1", tv.text.toString())
    }

    @Test
    fun addsToEnteredValueWhenNumberClicked() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton2).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton3).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton4).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton5).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton6).performClick()
        val tv = activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView
        assertEquals("Entered value should display 123456", "123456", tv.text.toString())
    }

    @Test
    fun doesNotAddToEnteredValueIfAtSixDigits() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton2).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton3).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton4).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton5).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton6).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton6).performClick()
        val tv = activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView
        assertEquals("Entered value should display 123456", "123456", tv.text.toString())
    }

    @Test
    fun onClickClearButton_clearEnteredValue() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton2).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton3).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton4).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton5).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton6).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonClear).performClick()
        assertEquals("Entered value should be blank", "", (activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView).text.toString())
    }

    @Test
    fun onClickEnteredValue_clearEnteredValue() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton2).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton3).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton4).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton5).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton6).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue).performClick()
        assertEquals("Entered value should be blank", "", (activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView).text.toString())
    }

    @Test
    fun onClickDoubleZero_appendsDoubleZero() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton00).performClick()
        assertEquals("Entered value should be 100", "100", (activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView).text.toString())
    }

    @Test
    fun onClickTripleZero_appendsTripleZero() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        assertEquals("Entered value should be 1000", "1000", (activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView).text.toString())
    }

    @Test
    fun onClickUndo_unexecutesLastAddition() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonPlusPlayer1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        assertEquals("Player 1 LP should be 8000", "8000", (activity!!.findViewById<View>(R.id.LpCalculatorTextPlayer1Lp) as TextView).text.toString())
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonPlusPlayer2).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        assertEquals("Player 1 LP should be 8000", "8000", (activity!!.findViewById<View>(R.id.LpCalculatorTextPlayer2Lp) as TextView).text.toString())
    }

    @Test
    fun onClickUndo_unexecutesLastSubtraction() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonMinusPlayer1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        assertEquals("Player 1 LP should be 8000", "8000", (activity!!.findViewById<View>(R.id.LpCalculatorTextPlayer1Lp) as TextView).text.toString())
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonMinusPlayer2).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        assertEquals("Player 2 LP should be 8000", "8000", (activity!!.findViewById<View>(R.id.LpCalculatorTextPlayer2Lp) as TextView).text.toString())
    }

    @Test
    fun onClickUndo_doesNothingIfNoCommandsInHistory() {
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
    }


    @Test
    fun onInit_setsUpTurnDisplay() {
        assertEquals("Turn Should Display Turn\n1", "Turn\n1", (activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn) as Button).text.toString())
    }

    @Test
    fun onClickTurn_incrementsTurnDisplay() {
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        assertEquals("Turn Should Display Turn\n2", "Turn\n2", (activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn) as Button).text.toString())
    }

    @Test
    fun onLongClickTurn_decrementTurnDisplay() {
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performLongClick()
        assertEquals("Turn Should Display Turn\n2", "Turn\n2", (activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn) as Button).text.toString())
    }

    @Test
    fun onLongClickTurn_doNothingIfOnTurn1() {
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performLongClick()
        assertEquals("Turn Should Display Turn\n1", "Turn\n1", (activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn) as Button).text.toString())
    }

    @Test
    fun onClickUndo_unexecutesLastTurnIncrement() {
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        assertEquals("Turn Should Display Turn\n2", "Turn\n2", (activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn) as Button).text.toString())
    }

    @Test
    fun onClickUndo_unexecutesLastTurnDecrement() {
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performLongClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonUndo).performClick()
        assertEquals("Turn Should Display Turn\n3", "Turn\n3", (activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn) as Button).text.toString())
    }

    @Test
    fun onClickReset_doesReset() {
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonMinusPlayer1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonMinusPlayer2).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonTurn).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton1).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButton000).performClick()
        activity!!.findViewById<View>(R.id.LpCalculatorButtonReset).performClick()
        assertEquals("P1 LP should be 8000", "8000", (activity!!.findViewById<View>(R.id.LpCalculatorTextPlayer1Lp) as TextView).text.toString())
        assertEquals("P2 LP should be 8000", "8000", (activity!!.findViewById<View>(R.id.LpCalculatorTextPlayer2Lp) as TextView).text.toString())
        assertEquals("Entered value should be blank", "", (activity!!.findViewById<View>(R.id.LpCalculatorTextEnteredValue) as TextView).text.toString())
        assertEquals(LpCalculatorModel.player1Lp, 8000)
        assertEquals(LpCalculatorModel.player2Lp, 8000)
        assertEquals(LpCalculatorModel.getEnteredValue(), 0)
    }

    @Test
    fun onDetatch() {
        val activityController = Robolectric.buildActivity<MainActivity>(MainActivity::class.java!!).create().destroy()
    }
}
