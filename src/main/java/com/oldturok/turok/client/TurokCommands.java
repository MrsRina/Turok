package com.oldturok.turok.client;

import com.oldturok.turok.commands.api.Command;
import com.oldturok.turok.managers.CommandManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TurokCommands {
    public CommandManager commandManager;

    public TurokCommands() {
        commandManager = new CommandManager(".", new Style().setColor(TextFormatting.GRAY));
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String theMessage = event.getMessage();
        String[] commandArgs = commandManager.GetArgs(event.getMessage());
        boolean commandIssued = false;

        if (commandArgs.length > 0) {

            for (Command command : commandManager.commandList) {
                if (commandManager.GetArgs(event.getMessage())[0].equalsIgnoreCase(command.name)) {
                    commandIssued = command.DoCommand(commandManager.GetArgs(event.getMessage()));
                }
            }

            if (!commandIssued && commandManager.ContainsPrefix(event.getMessage())) {
                TextComponentString t = new TextComponentString("Unknown command. Type " + commandManager.commandPrefix + "help for a list of commands");
                t.setStyle(new Style().setColor(TextFormatting.GRAY));
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(commandManager.responsePrefix + t.getFormattedText()));
                commandIssued = true;
            }
        }

        if (commandIssued) { event.setMessage(""); } else { event.setMessage(theMessage); }
    }
}
