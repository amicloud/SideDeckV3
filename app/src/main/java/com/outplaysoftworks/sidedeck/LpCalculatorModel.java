package com.outplaysoftworks.sidedeck;

/**
 * Created by Billy on 7/22/2017.
 */

public class LpCalculatorModel {
    static private String player1Name;
    static private String player2Name;
    static private boolean allowsNegativeLp;
    static private int lpDefault;
    static private int player1Lp;
    static private int player2Lp;
    static private int mEnteredValue;
    static private int currentTurn;

    LpCalculatorModel(){

    }

    static int getEnteredValue() {
        return mEnteredValue;
    }
    private static void setEnteredValue(int enteredValue) {
        mEnteredValue = enteredValue;
    }

    /**
     * If the final value would be less than 7 digits, appends the pressed number to the currently
     * entered value.
     *
     * @param i Integer to append to entered value
     * @return Returns true if successful, returns false otherwise.
     */
    static boolean appendToEnteredValue(String i) {
        String evString = Integer.toString(mEnteredValue);
        String appendedString = evString + i;
        if (appendedString.length() < 7) {
            setEnteredValue(Integer.parseInt(appendedString));
            return true;
        } else {
            return false;
        }
    }

    static boolean clearEnteredValue(){
        setEnteredValue(0);
        return true;
    }

    static int getLpDefault() {
        return lpDefault;
    }

    static void setLpDefault(int zlpDefault) {
        lpDefault = zlpDefault;
    }

    static String getPlayer1Name() {
        return player1Name;
    }

    static void setPlayer1Name(String zplayer1Name) {
        player1Name = zplayer1Name;
    }

    static String getPlayer2Name() {
        return player2Name;
    }

    static void setPlayer2Name(String zplayer2Name) {
        player2Name = zplayer2Name;
    }

    static boolean getAllowsNegativeLp() {
        return allowsNegativeLp;
    }

    static void setAllowsNegativeLp(boolean zallowsNegativeLp) {
        allowsNegativeLp = zallowsNegativeLp;
    }

    static void addLpToPlayer1(int amount) {
        player1Lp += amount;
    }

    static void addLpToPlayer2(int amount) {
        player2Lp += amount;
    }

    static void subtractLpFromPlayer1(int amount){
        player1Lp -= amount;
    }

    static void subtractLpFromPlayer2(int amount){
        player2Lp -= amount;
    }

    static int getPlayer1Lp() {
        return player1Lp;
    }

    static void setPlayer1Lp(int zplayer1Lp) {
        player1Lp = zplayer1Lp;
    }

    static int getPlayer2Lp() {
        return player2Lp;
    }

    static void setPlayer2Lp(int zplayer2Lp) {
        player2Lp = zplayer2Lp;
    }



    static int getCurrentTurn() {
        return currentTurn;
    }

    private static void setCurrentTurn(int zcurrentTurn) {
        currentTurn = zcurrentTurn;
    }

    static boolean incrementTurn(){
        currentTurn++;
        return true;
    }

    static boolean decrementTurn(){
        if(currentTurn == 1){
            return false;
        } else {
        currentTurn--;
        return true;
        }
    }

    static void resetTurns(){
        setCurrentTurn(1);
    }
}
