package com.outplaysoftworks.sidedeck;

import android.widget.TextView;

import static com.outplaysoftworks.sidedeck.AnimateTextView.animateTextView;

/**
 * Command to add LP to target player and animate textview
 */

class AddLpCommand implements Command {

    private int target;
    private int amount;
    private TextView textView;

    AddLpCommand(int target, int amount, TextView textView) {
        this.target = target;
        this.amount = amount;
        this.textView = textView;
    }

    @Override
    public void execute() {
        CommandDelegator.commandHistory.add(this);
        int prevLp;
        switch(target){
            case 1:
                prevLp = LpCalculatorModel.getPlayer1Lp();
                LpCalculatorModel.addLpToPlayer1(amount);
                animateTextView(prevLp, LpCalculatorModel.getPlayer1Lp(), textView, false);
                //textView.setText(Integer.toString(LpCalculatorModel.getPlayer1Lp()));
                return;
            case 2:
                prevLp = LpCalculatorModel.getPlayer2Lp();
                LpCalculatorModel.addLpToPlayer2(amount);
                animateTextView(prevLp, LpCalculatorModel.getPlayer2Lp(), textView, false);
                //textView.setText(Integer.toString(LpCalculatorModel.getPlayer2Lp()));
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
