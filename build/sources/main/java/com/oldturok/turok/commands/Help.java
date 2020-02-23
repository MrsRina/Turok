package com.oldturok.turok.commands;

import com.oldturok.turok.Turok;
import com.oldturok.turok.commands.api.Command;

public class Help extends Command {
    public Help() {
        super("help", "lists all possible commands");
    }

    @Override
    public boolean DoCommand(String[] commandArgs) {
        for (Command command : Turok.commands.commandManager.commandList) {
            clientResponse(responseBuilder(command.name + ": " + command.description));
        }

        return true;
    }
}
