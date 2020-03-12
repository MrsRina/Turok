package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.module.modules.combat.TurokCrystalAura;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMod;

// Rina.
// Turok RPC.
@Module.Info(name = "TurokRPC", category = Module.Category.TUROK_MISC)
public class TurokRPC extends Module {
	public static DiscordRichPresence discord_presence;

	private static boolean discord_started;
	private static final DiscordRPC discord_rpc;

	public static String detail;
	public static String state;

	private Setting<Boolean> show_name = register(Settings.b("Show Name", true));
	private Setting<Boolean> show_server = register(Settings.b("Show Server", true));
	private Setting<Boolean> show_events = register(Settings.b("Currrent Events", true));

	private String name;
	private String server;
	private String event_1;

	TurokCrystalAura crystalfunction = (TurokCrystalAura) ModuleManager.getModuleByName("TurokCrystalAura");

	@Override
	public void onEnable() {
		discord_presence = new DiscordRichPresence();
		discord_started = false;
		
		if (discord_started) return;
		discord_started = true;

		final DiscordEventHandlers handler_ = new DiscordEventHandlers();
		discord_rpc.Discord_Initialize("683841698778185818", handler_, true, "");

		discord_presence.startTimestamp = System.currentTimeMillis() / 1000l;
		discord_presence.largeImageKey  = "splash";
		discord_presence.largeImageText = "Turok Client " + TurokMod.TUROK_MOD_VERSION;

		new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					discord_rpc.Discord_RunCallbacks();

					discord_presence.details = detail;
					discord_presence.state = state;

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

	@Override
	public void onDisable() {
		discord_rpc.Discord_Shutdown();
	}

	@Override
	public void onUpdate() {
		if (show_name.getValue()) {
			name = mc.player.getName();
		} else {
			name = "";
		}

		if (show_server.getValue()) {
			if (mc.isIntegratedServerRunning()) {
				server = "survival offline";
			} else if (mc.getCurrentServerData() != null) {
				server = mc.getCurrentServerData().serverIP;
			} else {
				server = "main menu";
			}
		} else {
			server = "";
		}

		if (show_events.getValue()) {
			if (ModuleManager.getModuleByName("TurokCrystalAura").isEnabled()) {
				event_1 = "crystaling " + TurokCrystalAura.player_target;

				life(true);
			} else {
				event_1 = mc.world.getBiome(mc.player.getPosition()).getBiomeName();

				life(false);
			}

		} else {
			event_1 = "";
		}

		detail = name + " - " + server;
		state = event_1 + "";
	}

	public void life(Boolean type) {
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
		discord_rpc = DiscordRPC.INSTANCE;
		discord_presence = new DiscordRichPresence();
		discord_started = false;
	}
}