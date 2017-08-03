package com.outplaysoftworks.sidedeck;

/**
 * Created by Billy on 7/23/2017.
 */

public interface Command {

    enum PLAYER{
        PLAYER1, PLAYER2
    }

    void execute();

    void unExecute();

    int getTarget();

    int getAmount();
}
