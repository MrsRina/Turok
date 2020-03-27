package com.oldturok.turok.commands;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

// Rina.
public class TurokHelp extends TurokChat {
	public TurokHelp() {
		super("help", "Get description module.");
	}

	@Override
	public boolean Get_Message(String[] message) {
		if (message.length > 1) {
			String command = message[1];

			if (command.equals("GUI")) {
				TurokMessage.send_error_msg("You need write a real module.");
			} else {
				try {
					TurokMessage.send_client_msg(command + ": " + ModuleManager.getModuleByName(command).getDescription());
				} catch (Exception exc) {
					TurokMessage.send_error_msg("You need write a real module.");
				}
			}

		} else {
			TurokMessage.send_client_msg("Write a module and get description. '-help Aura'");
		}
	
		return true;
	}
}