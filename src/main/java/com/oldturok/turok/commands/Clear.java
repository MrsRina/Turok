package com.oldturok.turok.commands;

import com.oldturok.turok.commands.api.Command;

public class Clear extends Command {
    public Clear() {
        super("clear", "clears the entire chat history");
    }

    @Override
    public boolean DoCommand(String[] commandArgs) {
        minecraft.ingameGUI.getChatGUI().clearChatMessages(false);
        clientResponse(responseBuilder("Chat cleared!"));

        return true;
    }
}
