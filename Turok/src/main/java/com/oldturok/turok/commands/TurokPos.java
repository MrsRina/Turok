package com.oldturok.turok.commands;

import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

// Rina.
public class TurokPos extends TurokChat {
	public TurokPos() {
		super("pos", "Send pos into end message.");
	}

	@Override
	public boolean Get_Message(String[] message) {
		String msg = "";

		for (int i = 1; i < message.length; i++) {
			msg += message[i] + " ";
		}

		int posX = (int) mc.player.posX;
		int posY = (int) mc.player.posY;
		int posZ = (int) mc.player.posZ;
		
		if (msg.equals("")) {
			TurokMessage.send_client_msg("For send a custom message with pos in the end. '-sendpos BASEEEEE!! foound!!! !! (pos)'");
		} else {
			TurokMessage.user_send_msg(msg + " " + Integer.toString(posX) + " " + Integer.toString(posY) + " " + Integer.toString(posZ));
		}
	
		return true;
	}
}