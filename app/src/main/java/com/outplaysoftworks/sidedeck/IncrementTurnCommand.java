package com.outplaysoftworks.sidedeck;

import android.widget.Button;

/**
 * Created by Billy on 7/26/2017.
 */

public class IncrementTurnCommand implements Command {


    private final Button btTurn;
    private final String turnString;

    public IncrementTurnCommand(Button btTurn, String turnString) {

        this.btTurn = btTurn;
        this.turnString = turnString;
    }

    @Override
    public void execute() {
        CommandDelegator.commandHistory.add(this);
        if(LpCalculatorModel.incrementTurn()){
            btTurn.setText(turnString + Integer.toString(LpCalculatorModel.getCurrentTurn()));
        }
    }

    @Override
    public void unExecute() {
        CommandDelegator.commandHistory.remove(this);
        if(LpCalculatorModel.decrementTurn()){
            btTurn.setText(turnString + Integer.toString(LpCalculatorModel.getCurrentTurn()));
        }
    }
}
