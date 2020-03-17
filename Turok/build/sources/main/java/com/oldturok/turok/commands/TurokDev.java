package com.oldturok.turok.commands;

import com.oldturok.turok.gui.turok.widgets.TurokFrameUI;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

// Rina.
public class TurokDev extends TurokChat {
	public TurokDev() {
		super("effect", "For dev/Rina use.");
	}

	@Override
	public boolean Get_Message(String[] message) {
		String value = message[1];
		TurokFrameUI.speed_effect = Integer.parseInt(value.trim());

		TurokMessage.send_msg("Speed tick setted to " + value);

		return true;
	}
}