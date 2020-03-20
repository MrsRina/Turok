package com.oldturok.turok.module.modules.player;

import net.minecraft.network.play.client.CPacketChatMessage;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

// Rina.
@Module.Info(name = "Turok", description = "Just a simple module for say I am Turok!", category = Module.Category.TUROK_CHAT)
public class ImTurok extends Module {
	private Setting<Boolean> toxic = register(Settings.b("Toxic Mode Ugly : (", false));

	@Override
	public void onEnable() {
		if (toxic.getValue()) {
			send("I AM TUROK NIGGA, FUCK YOU FAG!");
		} else {
			send("I AM TUROK!");
		}

		disable();
	}

	public void send(String message) {
		mc.player.connection.sendPacket(new CPacketChatMessage(message));
	}
}