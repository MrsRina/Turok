package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.turokrpc.TurokDiscordP;

// Rina.
@Module.Info(name = "TurokRPC", category = Module.Category.TUROK_MISC)
public class TurokRPC extends Module {
	private Setting<Boolean> show_name = register(Settings.b("Show Name", true));
	private Setting<Boolean> show_server = register(Settings.b("Show Server", true));
	private Setting<Boolean> show_doom = register(Settings.b("Show Doom Mode", true));

	private String name;
	private String playing;
	private String server;
	private String doom;

	@Override
	public void onEnable() {
		TurokDiscordP.start();
	}

	@Override
	public void onDisable() {
		TurokDiscordP.end();
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
				playing = " playing in ";
				server = "survival offline";
			} else if (mc.getCurrentServerData() != null) {
				playing = " playing in ";
				server = mc.getCurrentServerData().serverIP;
			} else {
				playing = " in";
				server = "main menu";
			}
		} else {
			playing = "";
			server = "";
		}

		if (show_doom.getValue()) {
			doom = "Doomshop motherfuckz!";
		} else {
			doom = "Turok...";
		}

		TurokDiscordP.detail = name + playing + server;
		TurokDiscordP.state = doom;
	}
}