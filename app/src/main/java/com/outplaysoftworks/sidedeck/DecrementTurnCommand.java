package com.outplaysoftworks.sidedeck;

import android.widget.Button;

/**
 * Created by Billy on 7/26/2017.
 */

public class DecrementTurnCommand implements Command {

    private final Button btTurn;
    private final String turnString;

    public DecrementTurnCommand(Button btTurn, String turnString) {
        this.btTurn = btTurn;
        this.turnString = turnString;
    }

    @Override
    public void execute() {
        CommandDelegator.commandHistory.add(this);
        if(LpCalculatorModel.decrementTurn()){
            btTurn.setText(turnString + Integer.toString(LpCalculatorModel.getCurrentTurn()));
        }
    }

    @Override
    public void unExecute() {
        CommandDelegator.commandHistory.remove(this);
        if(LpCalculatorModel.incrementTurn()){
            btTurn.setText(turnString + Integer.toString(LpCalculatorModel.getCurrentTurn()));
        }
    }

    @Override
    public int getTarget() {
        return 0;
    }

    @Override
    public int getAmount() {
        return 0;
    }
}
