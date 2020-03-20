package com.oldturok.turok.commands;

import com.oldturok.turok.gui.TurokChatCommand;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

// Rina.
public class TurokPrefix extends TurokChat {
	public TurokPrefix() {
		super("prefix", "Change prefix.");
	}

	@Override
	public boolean Get_Message(String[] message) {
		if (message.length > 1) {
			String prefix = message[1];

			TurokMod.TUROK_CHAT_PREFIX = message[1];
	
			TurokMessage.send_client_msg("The new character is: " + message[1]);
		} else {
			TurokMessage.send_client_msg("Set a new character using 'prefix [character]'.");
		}
	
		return true;
	}
}