package com.oldturok.turok.turokrpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;

import com.oldturok.turok.module.ModuleManager;

public class TurokDiscordP {
	public static DiscordRichPresence discord_presence;

	private static final DiscordRPC rpc;
	private static boolean discord_started;

	private static String deta_;
	private static String state;

	public static void start() {
		if (TurokDiscordP.discord_started) return;
		TurokDiscordP.discord_started = true;

		final DiscordEventHandlers handler_ = new DiscordEventHandlers();
		TurokDiscordP.rpc.Discord_Initialize("638403216278683661", handler_, true, "");
		TurokDiscordP.discord_presence.startTimestamp = System.currentTimeMillis() / 1000l;

		deta_ = "Turok test rpc";
		state = "gay";

		TurokDiscordP.discord_presence.details = deta_;
		TurokDiscordP.discord_presence.state = state;

		TurokDiscordP.rpc.Discord_UpdatePresence(TurokDiscordP.discord_presence);

		new Thread(TurokDiscordP::discordRpcInit).start();
	}

	private static void discordRpcInit() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				TurokDiscordP.rpc.Discord_RunCallbacks();

				TurokDiscordP.discord_presence.details = deta_;
				TurokDiscordP.discord_presence.state = state;

				TurokDiscordP.rpc.Discord_UpdatePresence(TurokDiscordP.discord_presence);
			}

			catch (Exception exc) {
				exc.printStackTrace();
			}

			try {
				Thread.sleep(4000L);
			}

			catch (InterruptedException exc_) {
				exc_.printStackTrace();
			}
		}
	}
}