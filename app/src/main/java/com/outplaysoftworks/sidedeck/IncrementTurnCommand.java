package com.outplaysoftworks.sidedeck;

import android.widget.Button;

/**
 * Created by Billy on 7/26/2017.
 */

public class IncrementTurnCommand implements Command {


    private final Button btTurn;
    private final String turnString;
    private final LpLog logFragment;

    public IncrementTurnCommand(Button btTurn, String turnString, LpLog logFragment) {
        this.btTurn = btTurn;
        this.turnString = turnString;
        this.logFragment = logFragment;
    }

    @Override
    public void execute() {
        if(LpCalculatorModel.incrementTurn()){
            CommandDelegator.commandHistory.add(this);
            btTurn.setText(turnString + Integer.toString(LpCalculatorModel.getCurrentTurn()));
            logFragment.onTurnIncremented(this);
        }
    }

    @Override
    public void unExecute() {
        if(LpCalculatorModel.decrementTurn()){
            CommandDelegator.commandHistory.remove(this);
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
