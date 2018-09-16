package com.outplaysoftworks.sidedeck

import android.widget.TextView

import com.outplaysoftworks.sidedeck.AnimateTextView.animateTextView

/**
 * Command to add LP to target player and animate textview
 */

internal class AddLpCommand(override val target: Int, override val amount: Int, val textView: TextView, val log: LpLog) : Command {

    override fun execute() {
        CommandDelegator.commandHistory.add(this)
        val prevLp: Int
        when (target) {
            1 -> {
                prevLp = LpCalculatorModel.player1Lp
                LpCalculatorModel.addLpToPlayer1(amount)
                animateTextView(prevLp, LpCalculatorModel.player1Lp, textView, false)
                log.onAdd(this)
                return
            }
            2 -> {
                prevLp = LpCalculatorModel.player2Lp
                LpCalculatorModel.addLpToPlayer2(amount)
                animateTextView(prevLp, LpCalculatorModel.player2Lp, textView, false)
                log.onAdd(this)
                return
            }
        }
    }

    override fun unExecute() {
        CommandDelegator.commandHistory.remove(this)
        when (target) {
            1 -> {
                LpCalculatorModel.subtractLpFromPlayer1(amount)
                textView.text = Integer.toString(LpCalculatorModel.player1Lp)
                log.onAddSubtractUndo()
                return
            }
            2 -> {
                LpCalculatorModel.subtractLpFromPlayer2(amount)
                textView.text = Integer.toString(LpCalculatorModel.player2Lp)
                log.onAddSubtractUndo()
                return
            }
        }
    }
}
