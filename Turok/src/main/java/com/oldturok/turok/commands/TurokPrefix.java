package com.oldturok.turok.commands;

import com.oldturok.turok.TurokChat;

// Rina.
public class TurokPrefix extends TurokChat {
	public TurokPrefix() {
		super("prefix", "Change Prefix");
	}

	@Override
	public boolean get_message(String[] message) {
		if (message == null) {
			TurokChat.send_msg("try ")
		}
	}
}