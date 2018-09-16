package com.outplaysoftworks.sidedeck

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v4.view.ViewPager

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * .
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class LpLogRoboTests {
    internal var activity: MainActivity
    @SuppressLint("ApplySharedPref")
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
        val viewPager = activity.window.findViewById<View>(R.id.container) as ViewPager
        viewPager.currentItem = 1

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
