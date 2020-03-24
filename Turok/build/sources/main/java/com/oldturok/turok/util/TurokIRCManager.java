package com.oldturok.turok.util;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class TurokIRCManager {
	String BOT_TOKEN = "NjkxNzk1ODk0NTQyMTM5NTI3.XnlPsg.KlBDgLLWfq1uYoZjVthb4I5oYaw";

	public static void initBOT() {
		JDABuilder builder = new JDABuilder(AccountType.BOT)
							 .setToken(BOT_TOKEN)
							 .setAutoReconnect(true);

		try {
			jda = builder.buildBlocking();
		} catch (LoginException exc) {
			exc.printStackTrace();
		} catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
}