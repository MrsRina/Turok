package com.oldturok;

import com.oldturok.turok.module.modules.combat.TurokCrystalAura;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMod;

import net.minecraft.client.Minecraft;

// Rina.
// Turok RPC.
public class TurokRPC {
	public static DiscordRichPresence discord_presence;

	private static boolean discord_started;
	private static final DiscordRPC discord_rpc;

	public static String detail;
	public static String state;

	private static String name;
	private static String server;
	private static String event_1;

	public static TurokCrystalAura crystalfunction = (TurokCrystalAura) ModuleManager.getModuleByName("TurokCrystalAura");
	public static Minecraft mc = Minecraft.getMinecraft();

	public static void start() {
		discord_presence = new DiscordRichPresence();

		final DiscordEventHandlers handler_ = new DiscordEventHandlers();
		discord_rpc.Discord_Initialize("683841698778185818", handler_, true, "");

		discord_presence.startTimestamp = System.currentTimeMillis() / 1000l;
		discord_presence.largeImageKey  = "splash";
		discord_presence.largeImageText = "Turok Client " + TurokMod.TUROK_MOD_VERSION;

		new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					if (mc.player == null) {
						name = "";
					} else {
						name = mc.player.getName();
					}

					if (mc.isIntegratedServerRunning()) {
						server = "";
					} else if (mc.getCurrentServerData() != null) {
						server = mc.getCurrentServerData().serverIP;
					} else {
						server = "main menu";
					}

					if (ModuleManager.getModuleByName("TurokCrystalAura").isEnabled() || ModuleManager.getModuleByName("TurokInsaneAura").isEnabled()) {
						event_1 = "crystaling " + TurokCrystalAura.player_target;

						life(true);
					} else {
						event_1 = mc.world.getBiome(mc.player.getPosition()).getBiomeName();

						life(false);
					}

					detail = name + " - " + server;
					state  = event_1 + "";

					discord_rpc.Discord_RunCallbacks();

					discord_presence.details = detail;
					discord_presence.state   = state;

					discord_rpc.Discord_UpdatePresence(discord_presence);
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
		}, "RPC-Callback-Handler").start();
	}

	public static void life(Boolean type) {
		if (mc.player.getHealth() < 5.0f) {
			event_1 = "health " + Float.toString(mc.player.getHealth());			
		} else {
			if (type) {
				event_1 = "crystaling " + TurokCrystalAura.player_target;
			} else {
				event_1 = mc.world.getBiome(mc.player.getPosition()).getBiomeName();
			}
		}
	}

	static {
		discord_rpc      = DiscordRPC.INSTANCE;
		discord_presence = new DiscordRichPresence();
	}
}