package com.oldturok.turok.commands;

import com.oldturok.turok.gui.turok.widgets.TurokFrameUI;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

// Rina.
public class TurokDev extends TurokChat {
	public TurokDev() {
		super("turokdev", "For dev/Rina use.");
	}

	@Override
	public boolean Get_Message(String[] message) {
		String variable = message[1];
		String value    = message[2];

		if (message == null) {
			TurokMessage.send_msg("Ok what you will make with it.");
		} else {
			if (variable.equalsIgnoreCase("speed_effect")) {
				if (value != null) {
					TurokFrameUI.speed_effect = Integer.parseInt(value.trim());

					TurokMessage.send_msg("Speed tick setted to " + value);
				} else {
					TurokMessage.send_msg("Set some value for it.");
				}
			}
		}

		return true;
	}
}