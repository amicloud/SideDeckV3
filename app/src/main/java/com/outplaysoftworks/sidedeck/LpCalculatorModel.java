package com.outplaysoftworks.sidedeck;

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
    private int currentTurn;

    LpCalculatorModel(){

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
    boolean appendToEnteredValue(String i) {
        String evString = Integer.toString(mEnteredValue);
        String appendedString = evString + i;
        if (appendedString.length() < 7) {
            setEnteredValue(Integer.parseInt(appendedString));
            return true;
        } else {
            return false;
        }
    }

    boolean clearEnteredValue(){
        setEnteredValue(0);
        return true;
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

    public void addLpToPlayer1(int amount) {
        player1Lp += amount;
    }

    public void addLpToPlayer2(int amount) {
        player2Lp += amount;
    }

    public void subtractLpFromPlayer1(int amount){
        if(player1Lp - amount < 0){
            if(allowsNegativeLp){
                player1Lp -= amount;
            } else {
                player1Lp = 0;
            }
        } else {
        player1Lp -= amount;
        }
    }

    public void subtractLpFromPlayer2(int amount){
        if(player2Lp - amount < 0){
            if(allowsNegativeLp){
                player2Lp -= amount;
            } else {
                player2Lp = 0;
            }
        } else {
            player2Lp -= amount;
        }
    }

    public int getPlayer1Lp() {
        return player1Lp;
    }

    public void setPlayer1Lp(int player1Lp) {
        this.player1Lp = player1Lp;
    }

    public int getPlayer2Lp() {
        return player2Lp;
    }

    public void setPlayer2Lp(int player2Lp) {
        this.player2Lp = player2Lp;
    }



    public int getCurrentTurn() {
        return currentTurn;
    }

    private void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public boolean incrementTurn(){
        currentTurn++;
        return true;
    }

    public boolean decrementTurn(){
        if(currentTurn == 1){
            return false;
        } else {
        currentTurn--;
        return true;
        }
    }

    public void resetTurns(){
        setCurrentTurn(1);
    }
}
