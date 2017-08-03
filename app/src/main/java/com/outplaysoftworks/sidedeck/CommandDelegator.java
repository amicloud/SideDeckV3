package com.outplaysoftworks.sidedeck;

import java.util.ArrayList;

/**
 * Handles general command functions and history
 */

class CommandDelegator {
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

    public static void executeAll() {
        ArrayList<Command> oldCommands = new ArrayList<>();
        oldCommands.addAll(commandHistory);
        commandHistory.clear();
        for(Command command : oldCommands){
            command.execute();
        }
    }
}
