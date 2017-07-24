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
        model = new LpCalculatorModel();
        model.resetTurns();
        model.setLpDefault(8000);
        model.setPlayer1Lp(model.getLpDefault());
        model.setPlayer2Lp(model.getLpDefault());
        model.setPlayer1Name("Player 1");
        model.setPlayer2Name("Player 2");
    }

    @Test
    public void addsLpToPlayer1(){
        model.addLpToPlayer1(1000);
        assertEquals(9000, model.getPlayer1Lp());
    }

    @Test
    public void addsLpToPlayer2(){
        model.addLpToPlayer2(1000);
        assertEquals(9000, model.getPlayer2Lp());
    }

    @Test
    public void subtractsLpFromPlayer1(){
        model.subtractLpFromPlayer1(1000);
        assertEquals(7000, model.getPlayer1Lp());
    }

    @Test
    public void subtractsLpFromPlayer2(){
        model.subtractLpFromPlayer2(1000);
        assertEquals(7000, model.getPlayer2Lp());
    }

    @Test
    public void appendsToEnteredValue_ifValidValue(){
        model.appendToEnteredValue("780");
        assertEquals(780, model.getEnteredValue());
    }

    @Test
    public void doesNotAppendToEnteredValue_ifValueToHigh(){
        model.appendToEnteredValue("7800000");
        System.out.println(model.getEnteredValue());
        assertEquals(0, model.getEnteredValue());
    }

    @Test
    public void setsAndGetsPlayer1Name(){
        String testName = "Test Player 1";
        model.setPlayer1Name(testName);
        assertEquals(testName, model.getPlayer1Name());
    }

    @Test
    public void setsAndGetsPlayer2Name(){
        String testName = "Test Player 2";
        model.setPlayer2Name(testName);
        assertEquals(testName, model.getPlayer2Name());
    }

    @Test
    public void clearsEnteredValue(){
        model.appendToEnteredValue("1000");
        model.clearEnteredValue();
        assertEquals(0, model.getEnteredValue());
    }

    @Test
    public void setsAndGetsIsAllowsNegativeLp(){
        model.setAllowsNegativeLp(false);
        assertEquals(false, model.isAllowsNegativeLp());
        model.setAllowsNegativeLp(true);
        assertEquals(true, model.isAllowsNegativeLp());
    }

    @Test
    public void getsCurrentTurn(){
        assertEquals(1, model.getCurrentTurn());
    }

    @Test
    public void incrementsCurrentTurn(){
        model.incrementTurn();
        model.incrementTurn();
        assertEquals(3, model.getCurrentTurn());
    }

    @Test
    public void decrementsCurrentTurn(){
        model.incrementTurn();
        model.incrementTurn();
        model.decrementTurn();
        model.decrementTurn();
        assertEquals(1, model.getCurrentTurn());
    }

    @Test
    public void doesNotDecrementTurnIfCurrentTurnIs1(){
        model.decrementTurn();
        model.decrementTurn();
        assertEquals(1, model.getCurrentTurn());
    }
}
