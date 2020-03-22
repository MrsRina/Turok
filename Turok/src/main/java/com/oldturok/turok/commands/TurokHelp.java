package com.oldturok.turok.commands;

import com.oldturok.turok.gui.TurokChatCommand;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import com.mojang.realmsclient.gui.ChatFormatting;

public class TurokHelp extends TurokChat {
	public TurokHelp() {
		super("help", "A simple help command.");
	}

	@Override
	public Get_Message(Stringp[] message) {
		if (message.length == 1) {
			String command = message[1];

			for (Module module : ModuleManager.getModules()) {
				if (command.equals(module.getName())) {
					TurokMessage.send_client_msg(ChatFormatting.GREEN + ModuleManager.getModuleByName(command).getDescription());
				} else {
					TurokMessage.send_client_msg(ChatFormatting.RED + "This module not exist.");
				}
			}
		} else if (message.length > 2) {
			TurokMessage.send_client_msg(ChatFormatting.RED + "You need write: -help 'module_name', try look on GUI, for it press P.");
		}

		return true;
	}
}