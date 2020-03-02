package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;

import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.turokrpc.TurokDiscordP;
import com.oldturok.turok.module.modules.combat.TurokCrystalAura;

// Rina.
@Module.Info(name = "TurokRPC", category = Module.Category.TUROK_MISC)
public class TurokRPC extends Module {
	private Setting<Boolean> show_name = register(Settings.b("Show Name", true));
	private Setting<Boolean> show_server = register(Settings.b("Show Server", true));
	private Setting<Boolean> show_events = register(Settings.b("Currrent Events", true));

	private String name;
	private String server;
	private String event_1;

	TurokCrystalAura crystalfunction = (TurokCrystalAura) ModuleManager.getModuleByName("TurokCrystalAura");

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
				event_1 = TurokCrystalAura.player_target;

				life(true);
			} else {
				event_1 = mc.world.getBiome(mc.player.getPosition()).getBiomeName();

				life(false);
			}

		} else {
			event_1 = "";
		}

		TurokDiscordP.detail = name + " - " + server;
		TurokDiscordP.state = event_1 + "";
	}

	public void life(Boolean type) {
		if (mc.player.getHealth() < 10.0f) {
			event_1 = Float.toString(mc.player.getHealth());			
		} else {
			if (type) {
				event_1 = TurokCrystalAura.player_target;
			} else {
				event_1 = mc.world.getBiome(mc.player.getPosition()).getBiomeName();
			}
		}
	}
}