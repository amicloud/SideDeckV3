package com.outplaysoftworks.sidedeck

import android.widget.Button

/**
 * .
 */

class IncrementTurnCommand(private val btTurn: Button, private val turnString: String, private val logFragment: LpLog) : Command {

    override val target: Int
        get() = 0

    override val amount: Int
        get() = 0

    override fun execute() {
        if (LpCalculatorModel.incrementTurn()) {
            CommandDelegator.commandHistory.add(this)
            btTurn.text = turnString + Integer.toString(LpCalculatorModel.currentTurn)
            logFragment.onTurnIncremented(this)
        }
    }

    override fun unExecute() {
        if (LpCalculatorModel.decrementTurn()) {
            CommandDelegator.commandHistory.remove(this)
            btTurn.text = turnString + Integer.toString(LpCalculatorModel.currentTurn)
        }
    }
}
