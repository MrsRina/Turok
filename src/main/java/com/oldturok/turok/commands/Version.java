package com.oldturok.turok.commands;

import com.oldturok.turok.Turok;
import com.oldturok.turok.commands.api.Command;

public class Version extends Command {
    public Version() {
        super("version", "gets the version of the current client");
    }

    @Override
    public boolean DoCommand(String[] commandArgs) {
        clientResponse(responseBuilder("You are running " + Turok.client.CLIENT_NAME + " " + Turok.client.CLIENT_VERSION));

        return true;
    }
}
