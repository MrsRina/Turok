package com.oldturok.turok.util;

import java.io.IOException;

import pircBot.NickAlreadyInUseException;
import pircBot.IrcException;
import pircBot.PircBot;

public class TurokIRCManager extends PircBot {
	public final String IRC_HOST_NAME    = "irc.freenode.net";
	public final String IRC_HOST_CHANNEL = "#filhadaputa1negoney2turok3";
	public final int IRC_HOST_PORT       = 6667;

	private static String user_name;

	public TurokIRCManager(String username) {
		try {
			String first_name = username.substring(0, 1);
			int int_          = Integer.parseInt(first_name);

			username = "MC" + username;
		} catch (NumberFormatException exc) {

		}

		this.user_name = username;
	}

	public void start_connect() {
		this.setAutoNickChange(true);

		this.setName(user_name);

		this.changeNick(user_name);

		try {
			this.connect(IRC_HOST_NAME, IRC_HOST_PORT);
		} catch (NickAlreadyInUseException exc) {
			exc.printStackTrace();
		} catch (IOException exc) {
			exc.printStackTrace();
		} catch (IrcException exc) {
			exc.printStackTrace();
		}

		this.joinChannel(IRC_HOST_CHANNEL);
	}
}