package com.outplaysoftworks.sidedeck;

import android.widget.TextView;

/**
 * Created by Billy on 7/23/2017.
 */

public class AddLpCommand implements Command {

    int target;
    int amount;
    LpCalculatorModel model;
    TextView textView;

    public AddLpCommand(int target, int amount, LpCalculatorModel model, TextView textView) {
        this.target = target;
        this.amount = amount;
        this.model = model;
        this.textView = textView;
    }

    @Override
    public void execute() {
        CommandDelegator.commandHistory.add(this);
        switch(target){
            case 1:
                model.addLpToPlayer1(amount);
                textView.setText(Integer.toString(model.getPlayer1Lp()));
                return;
            case 2:
                model.addLpToPlayer2(amount);
                textView.setText(Integer.toString(model.getPlayer2Lp()));
                return;
            default:
        }
    }

    @Override
    public void unExecute() {
        CommandDelegator.commandHistory.remove(this);
        switch(target){
            case 1:
                model.subtractLpFromPlayer1(amount);
                textView.setText(Integer.toString(model.getPlayer1Lp()));
                return;
            case 2:
                model.subtractLpFromPlayer2(amount);
                textView.setText(Integer.toString(model.getPlayer2Lp()));
                return;
            default:
        }
    }
}
