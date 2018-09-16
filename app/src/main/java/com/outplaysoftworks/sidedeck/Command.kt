package com.outplaysoftworks.sidedeck

/**
 * .
 */

interface Command {

    val target: Int

    val amount: Int

    enum class PLAYER {
        PLAYER1, PLAYER2
    }

    fun execute()

    fun unExecute()
}
