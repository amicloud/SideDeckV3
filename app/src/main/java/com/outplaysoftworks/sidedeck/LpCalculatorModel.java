package com.outplaysoftworks.sidedeck;

import android.content.Context;

/**
 * Created by Billy on 7/22/2017.
 */

public class LpCalculatorModel {
    private String player1Name;
    private String player2Name;
    private boolean allowsNegativeLp;
    private int lpDefault;
    private int player1Lp;
    private int player2Lp;
    private int mEnteredValue;
    private Context mContext;

    public LpCalculatorModel(Context context) {
        mContext = context;
    }

    int getEnteredValue() {
        return mEnteredValue;
    }
    private void setEnteredValue(int enteredValue) {
        mEnteredValue = enteredValue;
    }

    /**
     * If the final value would be less than 7 digits, appends the pressed number to the currently
     * entered value.
     *
     * @param i Integer to append to entered value
     * @return Returns true if successful, returns false otherwise.
     */
    boolean appendToEnteredValue(int i) {
        String evString = Integer.toString(mEnteredValue);
        String appendedString = evString + Integer.toString(i);
        if (Integer.toString(mEnteredValue).length() < 6) {
            setEnteredValue(Integer.parseInt(appendedString));
            return true;
        } else {
            return false;
        }
    }

    public int getLpDefault() {
        return lpDefault;
    }

    public void setLpDefault(int lpDefault) {
        this.lpDefault = lpDefault;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public boolean isAllowsNegativeLp() {
        return allowsNegativeLp;
    }

    public void setAllowsNegativeLp(boolean allowsNegativeLp) {
        this.allowsNegativeLp = allowsNegativeLp;
    }
}
