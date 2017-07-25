package com.outplaysoftworks.sidedeck;

import android.widget.TextView;

/**
 * Created by Billy on 7/23/2017.
 */

public class AddLpCommand implements Command {

    int target;
    int amount;
    TextView textView;

    public AddLpCommand(int target, int amount, TextView textView) {
        this.target = target;
        this.amount = amount;
        this.textView = textView;
    }

    @Override
    public void execute() {
        CommandDelegator.commandHistory.add(this);
        switch(target){
            case 1:
                LpCalculatorModel.addLpToPlayer1(amount);
                textView.setText(Integer.toString(LpCalculatorModel.getPlayer1Lp()));
                return;
            case 2:
                LpCalculatorModel.addLpToPlayer2(amount);
                textView.setText(Integer.toString(LpCalculatorModel.getPlayer2Lp()));
                return;
            default:
        }
    }

    @Override
    public void unExecute() {
        CommandDelegator.commandHistory.remove(this);
        switch(target){
            case 1:
                LpCalculatorModel.subtractLpFromPlayer1(amount);
                textView.setText(Integer.toString(LpCalculatorModel.getPlayer1Lp()));
                return;
            case 2:
                LpCalculatorModel.subtractLpFromPlayer2(amount);
                textView.setText(Integer.toString(LpCalculatorModel.getPlayer2Lp()));
                return;
            default:
        }
    }
}
