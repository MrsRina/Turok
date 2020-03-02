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
	private Setting<Boolean> show_doom = register(Settings.b("Show DoomName", true));

	private String name;
	private String playing;
	private String server;
	private String doom;

	@Override
	public void onEnable() {
		TurokDiscordP.start();
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
				playing = (" Playing in ");
				show_server = ("Survival Offline");
			} else if (mc.getCurrentServerData() != null) {
				playing = (" Playing in ");
				show_server = mc.getCurrentServerData().serverIP;
			} else {
				playing = (" In");
				show_server = ("Main Menu");
			}
		}

		if (show_doom.getValue()) {
			show_doom = ("Doomshop Motherfuckz Client. Turok!!");
		} else {
			show_doom = ("I am Turok!");
		}

		TurokDiscordP.detail = name + playing + show_server;
		TurokDiscordP.state = show_doom;
	}
}