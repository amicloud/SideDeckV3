package com.outplaysoftworks.sidedeck;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Billy on 7/23/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CalculatorModelTests {
    private MainActivity activity;

//    @Before
//    public void setup(){
//        activity = Robolectric.setupActivity(MainActivity.class);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Test
//    public void clickingNumber_shouldAddToEnteredValue(){
////        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
//        activity.findViewById(R.id.LpCalculatorButton1).performClick();
//        activity.findViewById(R.id.LpCalculatorButton2).performClick();
//        activity.findViewById(R.id.LpCalculatorButton3).performClick();
//        activity.findViewById(R.id.LpCalculatorButton4).performClick();
//        activity.findViewById(R.id.LpCalculatorButton5).performClick();
//        activity.findViewById(R.id.LpCalculatorButton6).performClick();
//        TextView tv = (TextView) activity.findViewById(R.id.LpCalculatorTextEnteredValue);
//        assertThat(tv.getText().toString(), equalTo("123456"));
//    }

}
