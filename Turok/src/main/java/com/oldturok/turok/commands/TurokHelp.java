package com.oldturok.turok.commands;

import com.oldturok.turok.util.TurokChatManager;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import java.util.ArrayList;

// Rina.
public class TurokHelp extends TurokChat {
	public TurokHelp() {
		super("help", "-help commands/command/module module:name/command:name.");
	}

	public boolean Get_Message(String[] message) {
		if (message.length > 2) {
			String help = message[1];
			String what = message[2];

			if (help.equals("command")) {
				try {
					TurokMessage.send_client_msg("Name: " + TurokChatManager.get_command(what).get_name());
					TurokMessage.send_client_msg("Description: " + TurokChatManager.get_command(what).get_description());
				} catch (Exception exc) {
					TurokMessage.send_error_msg("You need write a real command.");
				}
			} else if (help.equals("module")) {
				try {
					TurokMessage.send_client_msg("Name: " + ModuleManager.getModuleByName(what).getName());
					TurokMessage.send_client_msg("Category: " + ModuleManager.getModuleByName(what).getCategory());
					TurokMessage.send_client_msg("Description: " + ModuleManager.getModuleByName(what).getDescription());
				} catch (Exception exc) {
					TurokMessage.send_error_msg("You need write a real module.");
				}
			}
		}

		if (message.length > 1) {
			String help = message[1];

			if (help.equals("commands")) {
				for (TurokChat commands : TurokChatManager.get_commands()) {
					TurokMessage.send_client_msg("Commands: " + commands.get_name());
				}
			} else if (help.equals("modules")) {
				TurokMessage.send_client_msg("-help command command_name | for get help command.");
			}
		} else if (message.length > 0) {
			TurokMessage.send_client_msg("-----------------");
			TurokMessage.send_client_msg("-help module name");
			TurokMessage.send_client_msg("-");
			TurokMessage.send_client_msg("-help command name");
			TurokMessage.send_client_msg("-");
			TurokMessage.send_client_msg("-help commands");
		} 

		return true;
	}
}