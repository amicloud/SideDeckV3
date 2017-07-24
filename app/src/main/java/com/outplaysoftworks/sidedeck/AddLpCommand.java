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
        int previous;
        switch(target){
            case 1:
                previous = model.getPlayer1Lp();
                model.addLpToPlayer1(amount);
                //LpCalculator.onPlayer1LpUpdated(previous, amount);
                textView.setText(Integer.toString(model.getPlayer1Lp()));
                return;
            case 2:
                previous = model.getPlayer2Lp();
                model.addLpToPlayer2(amount);
                textView.setText(Integer.toString(model.getPlayer2Lp()));
                //LpCalculator.onPlayer2LpUpdated(previous, amount);
                return;
            default:
        }
    }

    @Override
    public void unExecute() {

    }
}
