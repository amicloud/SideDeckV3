package com.outplaysoftworks.sidedeck;

import android.widget.TextView;

import static com.outplaysoftworks.sidedeck.AnimateTextView.animateTextView;

class SubtractLpCommand implements Command {

    private int target;
    private int amount;
    private TextView textView;
    private LpLog log;

    SubtractLpCommand(int target, int amount, TextView textView, LpLog log) {
        this.target = target;
        this.amount = amount;
        this.textView = textView;
        this.log = log;
    }

    @Override
    public void execute() {
        CommandDelegator.commandHistory.add(this);
        int prevLp;
        boolean zero;
        switch(target){
            case 1:
                prevLp = LpCalculatorModel.getPlayer1Lp();
                if(prevLp - amount < 0){
                    if(!LpCalculatorModel.getAllowsNegativeLp()){
                        amount = LpCalculatorModel.getPlayer1Lp();
                        LpCalculatorModel.subtractLpFromPlayer1(amount);
                        animateTextView(prevLp, LpCalculatorModel.getPlayer1Lp(), textView, true);
                        log.onSubtract(this);
                        return;
                    }
                }
                LpCalculatorModel.subtractLpFromPlayer1(amount);
                zero = false;
                if(LpCalculatorModel.getPlayer1Lp() == 0){
                    zero = true;
                }
                animateTextView(prevLp, LpCalculatorModel.getPlayer1Lp(), textView, zero);
                log.onSubtract(this);
                return;
            case 2:
                prevLp = LpCalculatorModel.getPlayer2Lp();
                if(prevLp - amount < 0){
                    if(!LpCalculatorModel.getAllowsNegativeLp()){
                        amount = LpCalculatorModel.getPlayer2Lp();
                        LpCalculatorModel.subtractLpFromPlayer2(amount);
                        animateTextView(prevLp, LpCalculatorModel.getPlayer2Lp(), textView, false);
                        log.onSubtract(this);
                        return;
                    }
                }
                LpCalculatorModel.subtractLpFromPlayer2(amount);
                zero = false;
                if(LpCalculatorModel.getPlayer2Lp() == 0){
                    zero = true;
                }
                animateTextView(prevLp, LpCalculatorModel.getPlayer2Lp(), textView, zero);
                log.onSubtract(this);
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
                log.onAddSubtractUndo();
                return;
            case 2:
                LpCalculatorModel.addLpToPlayer2(amount);
                textView.setText(Integer.toString(LpCalculatorModel.getPlayer2Lp()));
                log.onAddSubtractUndo();
                return;
            default:
        }
    }

    @Override
    public int getTarget() {
        return target;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public TextView getTextView() {
        return textView;
    }
}
