package com.outplaysoftworks.sidedeck

/**
 * .
 */

internal object LpCalculatorModel {
    var player1Name: String? = null
    var player2Name: String? = null
    var allowsNegativeLp: Boolean = false
    var lpDefault: Int = 0
    var player1Lp: Int = 0
    var player2Lp: Int = 0
    private var mEnteredValue: Int = 0
    var currentTurn: Int = 0
        private set

    fun getEnteredValue(): Int {
        return mEnteredValue
    }

    fun setEnteredValue(enteredValue: Int): Boolean {
        mEnteredValue = enteredValue
        return true
    }

    /**
     * If the final value would be less than 7 digits, appends the pressed number to the currently
     * entered value.
     *
     * @param i Integer to append to entered value
     * @return Returns true if successful, returns false otherwise.
     */
    fun appendToEnteredValue(i: String): Boolean {
        val evString = Integer.toString(mEnteredValue)
        val appendedString = evString + i
        if (appendedString.length < 7) {
            setEnteredValue(Integer.parseInt(appendedString))
            return true
        } else {
            return false
        }
    }

    fun clearEnteredValue(): Boolean {
        setEnteredValue(0)
        return true
    }

    fun addLpToPlayer1(amount: Int) {
        player1Lp += amount
    }

    fun addLpToPlayer2(amount: Int) {
        player2Lp += amount
    }

    fun subtractLpFromPlayer1(amount: Int) {
        player1Lp -= amount
    }

    fun subtractLpFromPlayer2(amount: Int) {
        player2Lp -= amount
    }

    fun incrementTurn(): Boolean {
        currentTurn++
        return true
    }

    fun decrementTurn(): Boolean {
        if (currentTurn == 1) {
            return false
        } else {
            currentTurn--
            return true
        }
    }

    fun resetTurns() {
        currentTurn = 1
    }
}
