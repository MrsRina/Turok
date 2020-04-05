package com.oldturok.turok.commands;

import com.oldturok.turok.util.TurokChatManager;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import java.util.ArrayList;

public class TurokToggle extends TurokChat {
	public TurokToggle() {
		super("t", "-t module | For toggle module.");
	}

	public boolean Get_Message(String[] message) {
		if (message.length > 1) {
			String module = message[1];

			try {
				ModuleManager.getModuleByName(module).toggle();
			} catch (Exception exc) {
				TurokMessage.send_error_msg("You need write a real module, try use -help or see in GUI pressing P.");
			}
		} else {
			TurokMessage.send_client_msg("For toggle module.");
		}

		return true;
	}
}