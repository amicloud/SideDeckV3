package com.outplaysoftworks.sidedeck

import java.util.ArrayList

/**
 * Handles general command functions and history
 */

internal object CommandDelegator {
    var commandHistory = ArrayList<Command>()

    fun undoLastCommand(): Boolean {
        if (commandHistory.size > 0) {
            val command = commandHistory[commandHistory.size - 1]
            command.unExecute()
            return true
        } else {
            return false
        }
    }

    fun reset() {
        commandHistory = ArrayList()
    }

    fun executeAll() {
        val oldCommands = ArrayList<Command>()
        oldCommands.addAll(commandHistory)
        commandHistory.clear()
        for (command in oldCommands) {
            command.execute()
        }
    }
}
