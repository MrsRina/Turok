package com.oldturok.turok.commands;

import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;

public class TurokPrefix extends TurokChat {
	public TurokPrefix() {
		super("prefix", "Change the prefix.");
	}

	@Override
	public void command(String[] message) {
		if (message.length > 1) {
			set_prefix(message[0]);

			TurokMessage.send_client_msg("The new prefix is " + get_prefix());
		} else if (message.length == 0) {
			TurokMessage.send_error_msg("You need write a value, 'prefix [value]'.");
		}
	}
}