package com.oldturok;

import com.oldturok.turok.module.modules.combat.TurokCrystalAura;
import com.oldturok.turok.module.modules.combat.TurokInsaneAura;
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
	private static String separe;
	private static String server;
	private static String event_1;
	private static String event_extra;

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
					if (mc.player != null) {
						name   = mc.player.getName();
						separe = " - ";
					} else {
						name        = "";
						separe      = " ";
						event_extra = "";
						event_1     = "";
					}

					if (mc.world != null) {
						if (mc.isIntegratedServerRunning()) {
							server = "just offline";

							event_1 = "relaxing in " +  mc.world.getBiome(mc.player.getPosition()).getBiomeName();
						} else if (mc.getCurrentServerData() != null) {
							server = mc.getCurrentServerData().serverIP;

							event_extra = "";

							if (mc.player.getHealth() < 4.0f) {
								if (mc.player.isDead) {
									event_1 = "died";
								} else {
									event_1 = "low health";
								}
							} else {
								if (ModuleManager.getModuleByName("TurokCrystalAura").isEnabled() || ModuleManager.getModuleByName("TurokInsaneAura").isEnabled()) {
									event_1 = "crystaling...";
								} else {
									event_1 = "relaxing in " +  mc.world.getBiome(mc.player.getPosition()).getBiomeName();
								}
							}
						}

					} else {
						server      = "main menu";
						separe      = " ";
						event_extra = "...";
						event_1     = "";
					}

					detail = name + separe + server;
					state  = event_1 + event_extra;

					discord_rpc.Discord_RunCallbacks();

					discord_presence.details = detail;
					discord_presence.state   = state;

					discord_rpc.Discord_UpdatePresence(discord_presence);
				} catch (Exception exc) {
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

	// I used like referece KAMI BLUE discord for INSTANCE, I can say, is not equal.
	static {
		discord_rpc      = DiscordRPC.INSTANCE;
		discord_presence = new DiscordRichPresence();
	}
}