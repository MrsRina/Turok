package com.oldturok.turok.commands;

import com.oldturok.turok.gui.TurokChatCommand;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import com.mojang.realmsclient.gui.ChatFormatting;

// Rina.
public class TurokPrefix extends TurokChat {
	public TurokPrefix() {
		super("prefix", "Change prefix.");
	}

	@Override
	public boolean Get_Message(String[] message) {
		if (message.length >= 2) {
			String prefix = message[2];

			set_prefix(message[2]);

			TurokMessage.send_client_msg(ChatFormatting.GREEN + "The new character is: " + message[2]);
		} else if (message.length > 3) {
			TurokMessage.send_client_msg(ChatFormatting.RED + "For set a new prefix you need only one argument.");
		} else if (message.length == 1){
			TurokMessage.send_client_msg(ChatFormatting.RED + "Set a new character using 'prefix [character]'.");
		}
	
		return true;
	}
}