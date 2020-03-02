package com.oldturok.turok.turokrpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.TurokMod;

// Rina.
public class TurokDiscordP {
	public static DiscordRichPresence discord_presence;

	private static boolean discord_started;
	private static final DiscordRPC discord_rpc;

	public static String detail;
	public static String state;

	public static void start() {
		TurokDiscordP.discord_presence = new DiscordRichPresence();
		TurokDiscordP.discord_started = false;
		
		if (TurokDiscordP.discord_started) return;
		TurokDiscordP.discord_started = true;

		final DiscordEventHandlers handler_ = new DiscordEventHandlers();
		TurokDiscordP.discord_rpc.Discord_Initialize("683841698778185818", handler_, true, "");
		TurokDiscordP.discord_presence.startTimestamp = System.currentTimeMillis() / 1000l;

		TurokDiscordP.discord_presence.largeImageKey = "splash";
		TurokDiscordP.discord_presence.largeImageText = "Turok Client " + TurokMod.MODVER;

		new Thread(TurokDiscordP::discordRpcInit).start();
	}

	public static void end() {
		TurokDiscordP.discord_rpc.Discord_Shutdown();
	}

	private static void discordRpcInit() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				TurokDiscordP.discord_rpc.Discord_RunCallbacks();

				TurokDiscordP.discord_presence.details = detail;
				TurokDiscordP.discord_presence.state = state;

				TurokDiscordP.discord_rpc.Discord_UpdatePresence(TurokDiscordP.discord_presence);
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

	static {
		discord_rpc = DiscordRPC.INSTANCE;
		TurokDiscordP.discord_presence = new DiscordRichPresence();
		TurokDiscordP.discord_started = false;
	}
}