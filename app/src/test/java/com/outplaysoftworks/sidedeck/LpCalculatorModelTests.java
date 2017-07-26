package com.outplaysoftworks.sidedeck;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Billy on 7/23/2017.
 */

public class LpCalculatorModelTests {

    private LpCalculatorModel model;
    @Before
    public void init(){
        //Mock loading settings from preferences
        LpCalculatorModel.resetTurns();
        LpCalculatorModel.setLpDefault(8000);
        LpCalculatorModel.setPlayer1Lp(LpCalculatorModel.getLpDefault());
        LpCalculatorModel.setPlayer2Lp(LpCalculatorModel.getLpDefault());
        LpCalculatorModel.setPlayer1Name("Player 1");
        LpCalculatorModel.setPlayer2Name("Player 2");
    }

    @Test
    public void addsLpToPlayer1(){
        LpCalculatorModel.addLpToPlayer1(1000);
        assertEquals(9000, LpCalculatorModel.getPlayer1Lp());
    }

    @Test
    public void addsLpToPlayer2(){
        LpCalculatorModel.addLpToPlayer2(1000);
        assertEquals(9000, LpCalculatorModel.getPlayer2Lp());
    }

    @Test
    public void subtractsLpFromPlayer1(){
        LpCalculatorModel.subtractLpFromPlayer1(1000);
        assertEquals(7000, LpCalculatorModel.getPlayer1Lp());
    }

    @Test
    public void subtractsLpFromPlayer2(){
        LpCalculatorModel.subtractLpFromPlayer2(1000);
        assertEquals(7000, LpCalculatorModel.getPlayer2Lp());
    }

    @Test
    public void appendsToEnteredValue_ifValidValue(){
        LpCalculatorModel.appendToEnteredValue("780");
        assertEquals(780, LpCalculatorModel.getEnteredValue());
    }

    @Test
    public void doesNotAppendToEnteredValue_ifValueToHigh(){
        LpCalculatorModel.appendToEnteredValue("7800000");
        System.out.println(LpCalculatorModel.getEnteredValue());
        assertEquals(0, LpCalculatorModel.getEnteredValue());
    }

    @Test
    public void setsAndGetsPlayer1Name(){
        String testName = "Test Player 1";
        LpCalculatorModel.setPlayer1Name(testName);
        assertEquals(testName, LpCalculatorModel.getPlayer1Name());
    }

    @Test
    public void setsAndGetsPlayer2Name(){
        String testName = "Test Player 2";
        LpCalculatorModel.setPlayer2Name(testName);
        assertEquals(testName, LpCalculatorModel.getPlayer2Name());
    }

    @Test
    public void clearsEnteredValue(){
        LpCalculatorModel.appendToEnteredValue("1000");
        LpCalculatorModel.clearEnteredValue();
        assertEquals(0, LpCalculatorModel.getEnteredValue());
    }

    @Test
    public void setsAndGetsIsAllowsNegativeLp(){
        LpCalculatorModel.setAllowsNegativeLp(false);
        assertEquals(false, LpCalculatorModel.getAllowsNegativeLp());
        LpCalculatorModel.setAllowsNegativeLp(true);
        assertEquals(true, LpCalculatorModel.getAllowsNegativeLp());
    }

    @Test
    public void getsCurrentTurn(){
        assertEquals(1, LpCalculatorModel.getCurrentTurn());
    }

    @Test
    public void incrementsCurrentTurn(){
        LpCalculatorModel.incrementTurn();
        LpCalculatorModel.incrementTurn();
        assertEquals(3, LpCalculatorModel.getCurrentTurn());
    }

    @Test
    public void decrementsCurrentTurn(){
        LpCalculatorModel.incrementTurn();
        LpCalculatorModel.incrementTurn();
        LpCalculatorModel.decrementTurn();
        LpCalculatorModel.decrementTurn();
        assertEquals(1, LpCalculatorModel.getCurrentTurn());
    }

    @Test
    public void doesNotDecrementTurnIfCurrentTurnIs1(){
        LpCalculatorModel.decrementTurn();
        LpCalculatorModel.decrementTurn();
        assertEquals(1, LpCalculatorModel.getCurrentTurn());
    }
}
