package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.module.Module;

import net.minecraft.network.play.client.CPacketPlayer;

// Rina.
// Coded in 05/03/20.
@Module.Info(name = "TurokPacketFly", category = Module.Category.TUROK_MOVEMENT)
public class TurokPacketFly extends Module {
	private Setting<Boolean> no_kick = register(Settings.b("No Kick", true));
	private Setting<Float> speed = register(Settings.floatBuilder("Speed").withMinimum(1.0f).withValue(5.0f).withMaximum(6.0f));

	private int teleport_id;

	private List<CPacketPlayer> packets = new ArrayList<CPacketPlayer>();
}