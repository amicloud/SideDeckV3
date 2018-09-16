package com.outplaysoftworks.sidedeck

import org.junit.Before
import org.junit.Test

import junit.framework.Assert.assertEquals

/**
 * .
 */

class LpCalculatorModelTests {

    private val model: LpCalculatorModel? = null
    @Before
    fun init() {
        //Mock loading settings from preferences
        LpCalculatorModel.resetTurns()
        LpCalculatorModel.lpDefault = 8000
        LpCalculatorModel.player1Lp = LpCalculatorModel.lpDefault
        LpCalculatorModel.player2Lp = LpCalculatorModel.lpDefault
        LpCalculatorModel.player1Name = "Player 1"
        LpCalculatorModel.player2Name = "Player 2"
    }

    @Test
    fun addsLpToPlayer1() {
        LpCalculatorModel.addLpToPlayer1(1000)
        assertEquals(9000, LpCalculatorModel.player1Lp)
    }

    @Test
    fun addsLpToPlayer2() {
        LpCalculatorModel.addLpToPlayer2(1000)
        assertEquals(9000, LpCalculatorModel.player2Lp)
    }

    @Test
    fun subtractsLpFromPlayer1() {
        LpCalculatorModel.subtractLpFromPlayer1(1000)
        assertEquals(7000, LpCalculatorModel.player1Lp)
    }

    @Test
    fun subtractsLpFromPlayer2() {
        LpCalculatorModel.subtractLpFromPlayer2(1000)
        assertEquals(7000, LpCalculatorModel.player2Lp)
    }

    @Test
    fun appendsToEnteredValue_ifValidValue() {
        LpCalculatorModel.appendToEnteredValue("780")
        assertEquals(780, LpCalculatorModel.getEnteredValue())
    }

    @Test
    fun doesNotAppendToEnteredValue_ifValueToHigh() {
        LpCalculatorModel.appendToEnteredValue("7800000")
        println(LpCalculatorModel.getEnteredValue())
        assertEquals(0, LpCalculatorModel.getEnteredValue())
    }

    @Test
    fun setsAndGetsPlayer1Name() {
        val testName = "Test Player 1"
        LpCalculatorModel.player1Name = testName
        assertEquals(testName, LpCalculatorModel.player1Name)
    }

    @Test
    fun setsAndGetsPlayer2Name() {
        val testName = "Test Player 2"
        LpCalculatorModel.player2Name = testName
        assertEquals(testName, LpCalculatorModel.player2Name)
    }

    @Test
    fun clearsEnteredValue() {
        LpCalculatorModel.appendToEnteredValue("1000")
        LpCalculatorModel.clearEnteredValue()
        assertEquals(0, LpCalculatorModel.getEnteredValue())
    }

    @Test
    fun setsAndGetsIsAllowsNegativeLp() {
        LpCalculatorModel.allowsNegativeLp = false
        assertEquals(false, LpCalculatorModel.allowsNegativeLp)
        LpCalculatorModel.allowsNegativeLp = true
        assertEquals(true, LpCalculatorModel.allowsNegativeLp)
    }

    @Test
    fun getsCurrentTurn() {
        assertEquals(1, LpCalculatorModel.currentTurn)
    }

    @Test
    fun incrementsCurrentTurn() {
        LpCalculatorModel.incrementTurn()
        LpCalculatorModel.incrementTurn()
        assertEquals(3, LpCalculatorModel.currentTurn)
    }

    @Test
    fun decrementsCurrentTurn() {
        LpCalculatorModel.incrementTurn()
        LpCalculatorModel.incrementTurn()
        LpCalculatorModel.decrementTurn()
        LpCalculatorModel.decrementTurn()
        assertEquals(1, LpCalculatorModel.currentTurn)
    }

    @Test
    fun doesNotDecrementTurnIfCurrentTurnIs1() {
        LpCalculatorModel.decrementTurn()
        LpCalculatorModel.decrementTurn()
        assertEquals(1, LpCalculatorModel.currentTurn)
    }
}
