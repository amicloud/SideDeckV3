package com.outplaysoftworks.sidedeck;

import java.util.ArrayList;

/**
 * Created by Billy on 7/23/2017.
 */

public class CommandDelegator {
    public static ArrayList<Command> commandHistory = new ArrayList<>();

    public static boolean undoLastCommand(){
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
