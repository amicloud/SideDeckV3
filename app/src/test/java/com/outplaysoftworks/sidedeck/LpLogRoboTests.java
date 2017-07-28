package com.outplaysoftworks.sidedeck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Billy on 7/26/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LpLogRoboTests {
    @SuppressWarnings("WeakerAccess")
    MainActivity activity;
    @SuppressLint("ApplySharedPref")

    @Before
    public void initSettingsForTesting() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.KEYallowNegativeLp), false);
        editor.putString(context.getString(R.string.KEYplayerOneDefaultNameSetting), "Player 1");
        editor.putString(context.getString(R.string.KEYplayerTwoDefaultNameSetting), "Player 2");
        editor.putString(context.getString(R.string.KEYdefaultLpSetting), "8000");
        editor.commit();
        activity = Robolectric.setupActivity(MainActivity.class);
        ViewPager viewPager = (ViewPager) activity.getWindow().findViewById(R.id.container);
        viewPager.setCurrentItem(1);

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
}
