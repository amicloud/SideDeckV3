package com.outplaysoftworks.sidedeck

import android.widget.TextView

import com.outplaysoftworks.sidedeck.AnimateTextView.animateTextView

internal class SubtractLpCommand(override val target: Int, amount: Int, val textView: TextView, private val log: LpLog) : Command {
    override var amount: Int = 0
        private set

    init {
        this.amount = amount
    }

    override fun execute() {
        CommandDelegator.commandHistory.add(this)
        val prevLp: Int
        val zero: Boolean
        when (target) {
            1 -> {
                prevLp = LpCalculatorModel.player1Lp
                if (prevLp - amount < 0) {
                    if (!LpCalculatorModel.allowsNegativeLp) {
                        amount = LpCalculatorModel.player1Lp
                        LpCalculatorModel.subtractLpFromPlayer1(amount)
                        animateTextView(prevLp, LpCalculatorModel.player1Lp, textView, true)
                        log.onSubtract(this)
                        return
                    }
                }
                LpCalculatorModel.subtractLpFromPlayer1(amount)
                zero = LpCalculatorModel.player1Lp == 0
                animateTextView(prevLp, LpCalculatorModel.player1Lp, textView, zero)
                log.onSubtract(this)
                return
            }
            2 -> {
                prevLp = LpCalculatorModel.player2Lp
                if (prevLp - amount < 0) {
                    if (!LpCalculatorModel.allowsNegativeLp) {
                        amount = LpCalculatorModel.player2Lp
                        LpCalculatorModel.subtractLpFromPlayer2(amount)
                        animateTextView(prevLp, LpCalculatorModel.player2Lp, textView, true)
                        log.onSubtract(this)
                        return
                    }
                }
                LpCalculatorModel.subtractLpFromPlayer2(amount)
                zero = LpCalculatorModel.player2Lp == 0
                animateTextView(prevLp, LpCalculatorModel.player2Lp, textView, zero)
                log.onSubtract(this)
                return
            }
        }
    }

    override fun unExecute() {
        CommandDelegator.commandHistory.remove(this)
        when (target) {
            1 -> {
                LpCalculatorModel.addLpToPlayer1(amount)
                textView.text = Integer.toString(LpCalculatorModel.player1Lp)
                log.onAddSubtractUndo()
                return
            }
            2 -> {
                LpCalculatorModel.addLpToPlayer2(amount)
                textView.text = Integer.toString(LpCalculatorModel.player2Lp)
                log.onAddSubtractUndo()
                return
            }
        }
    }
}
