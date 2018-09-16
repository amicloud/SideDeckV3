package com.outplaysoftworks.sidedeck

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation

@RunWith(AndroidJUnit4::class)
//@LargeTest
class LpLogTests {
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

}