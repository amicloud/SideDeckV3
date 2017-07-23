package com.outplaysoftworks.sidedeck;

/**
 * Created by Billy on 7/22/2017.
 */

public class LpCalculatorModel {
    private static int enteredValue;

    public static int getEnteredValue() {
        return enteredValue;
    }

    public static void setEnteredValue(int enteredValue) {
        LpCalculatorModel.enteredValue = enteredValue;
    }

    /**
     * If the final value would be less than 7 digits, appends the pressed number to the currently
     * entered value.
     * @param i Integer to append to entered value
     * @return Returns true if successful, returns false otherwise.
     */
    public static boolean appendToEnteredValue(int i){
        String evString = Integer.toString(enteredValue);
        String appendedString = evString + Integer.toString(i);
        if(Integer.parseInt(appendedString) < 999999){
            setEnteredValue(Integer.parseInt(appendedString));
            return true;
        } else {
            return false;
        }
    }
}
