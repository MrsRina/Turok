package com.oldturok.turok.command.commands;

import com.oldturok.turok.command.syntax.SyntaxChunk;
import com.oldturok.turok.command.Command;
import com.oldturok.turok.TurokMod;

import java.util.Comparator;

public class CommandsCommand extends Command {

    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY);
    }

    @Override
    public void call(String[] args) {
        TurokMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing(command -> command.getLabel())).forEach(command ->
            Command.sendChatMessage("&7" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &8" + command.getDescription())
        );
    }
}
