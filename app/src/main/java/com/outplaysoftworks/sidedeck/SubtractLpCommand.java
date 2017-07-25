package com.outplaysoftworks.sidedeck;

import android.widget.TextView;

/**
 * Created by Billy on 7/23/2017.
 */

public class SubtractLpCommand implements Command {

    int target;
    int amount;
    TextView textView;

    public SubtractLpCommand(int target, int amount, TextView textView) {
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
                if(prevLp - amount < 0){
                    if(!LpCalculatorModel.getAllowsNegativeLp()){
                        amount = LpCalculatorModel.getPlayer1Lp();
                        LpCalculatorModel.subtractLpFromPlayer1(amount);
                        textView.setText(Integer.toString(LpCalculatorModel.getPlayer1Lp()));
                        return;
                    }
                }
                LpCalculatorModel.subtractLpFromPlayer1(amount);
                textView.setText(Integer.toString(LpCalculatorModel.getPlayer1Lp()));
                return;
            case 2:
                prevLp = LpCalculatorModel.getPlayer1Lp();
                if(prevLp - amount < 0){
                    if(!LpCalculatorModel.getAllowsNegativeLp()){
                        amount = LpCalculatorModel.getPlayer2Lp();
                        LpCalculatorModel.subtractLpFromPlayer2(amount);
                        textView.setText(Integer.toString(LpCalculatorModel.getPlayer2Lp()));
                        return;
                    }
                }
                LpCalculatorModel.subtractLpFromPlayer2(amount);
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
}
