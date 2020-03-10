package com.oldturok.turok.command.commands;

import com.oldturok.turok.command.syntax.parsers.ModuleParser;
import com.oldturok.turok.command.syntax.ChunkBuilder;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.command.Command;
import com.oldturok.turok.module.Module;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("t", new ChunkBuilder()
                .append("module", true, new ModuleParser())
                .build());
    }

    @Override
    public void call(String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module '" + args[0] + "'");
            return;
        }
        m.toggle();
        Command.sendChatMessage(m.getName() + (m.isEnabled() ? " &aenabled" : " &cdisabled"));
    }
}
