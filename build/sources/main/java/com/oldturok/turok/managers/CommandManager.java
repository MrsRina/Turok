package com.oldturok.turok.managers;

import com.oldturok.turok.Turok;
import com.oldturok.turok.commands.*;
import com.oldturok.turok.commands.api.Command;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;

public class CommandManager {
    public ArrayList<Command> commandList = new ArrayList<Command>();
    public final String commandPrefix;
    public final Style chatFormat;
    public final String responsePrefix = new TextComponentString(new TextComponentString("[").setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText()
    + new TextComponentString(Turok.client.CLIENT_NAME).setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText()
    + new TextComponentString("] ").setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText()).getFormattedText();

    public CommandManager(String prefix, Style format) {
        commandPrefix = prefix;
        chatFormat = format;

        commandList.add(new Version());
        commandList.add(new Clear());
        commandList.add(new Bind());
        commandList.add(new Say());

        commandList.add(new Help());
    }

    public String[] GetArgs(String message) {
        String[] arguments = {};
        if (message.startsWith(commandPrefix)) {
            arguments = message.replaceFirst(commandPrefix, "").split(" ");
        }

        return arguments;
    }

    public boolean ContainsPrefix(String message) {
        return message.startsWith(commandPrefix);
    }
}
