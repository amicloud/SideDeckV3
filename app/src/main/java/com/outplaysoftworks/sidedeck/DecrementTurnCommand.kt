package com.outplaysoftworks.sidedeck

import android.widget.Button

/**
 * .
 */

class DecrementTurnCommand(private val btTurn: Button, private val turnString: String) : Command {

    override val target: Int
        get() = 0

    override val amount: Int
        get() = 0

    override fun execute() {
        CommandDelegator.commandHistory.add(this)
        if (LpCalculatorModel.decrementTurn()) {
            btTurn.text = turnString + Integer.toString(LpCalculatorModel.currentTurn)
        }
    }

    override fun unExecute() {
        CommandDelegator.commandHistory.remove(this)
        if (LpCalculatorModel.incrementTurn()) {
            btTurn.text = turnString + Integer.toString(LpCalculatorModel.currentTurn)
        }
    }
}
