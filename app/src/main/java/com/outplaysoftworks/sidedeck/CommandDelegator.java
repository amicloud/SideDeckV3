package com.outplaysoftworks.sidedeck;

import java.util.ArrayList;

/**
 * Handles general command functions and history
 */

public class CommandDelegator {
    static ArrayList<Command> commandHistory = new ArrayList<>();

    static boolean undoLastCommand(){
        if(commandHistory.size() > 0) {
            Command command = commandHistory.get(commandHistory.size() - 1);
            command.unExecute();
            return true;
        } else {
            return false;
        }
    }

    public static void reset() {
        commandHistory = new ArrayList<>();
    }
}
