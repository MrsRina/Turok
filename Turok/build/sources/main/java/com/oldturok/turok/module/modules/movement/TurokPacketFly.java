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

	public int fly_angle;

	public boolean player_forward = mc.gameSettings.keyBindForward.isKeyDown();
	public boolean player_back    = mc.gameSettings.keyBindBack.isKeyDown();
	public boolean player_right   = mc.gameSettings.keyBindRight.isKeyDown();
	public boolean player_left    = mc.gameSettings.keyBindLeft.isKeyDown();

	@Override
	public void onUpdate() {
		if (player_left && player_right) {
			fly_angle = player_forward ? 0 : player_back ? 180 : - 1;

		} else if (player_forward && player_back) {
			fly_angle = player_left ? - 90 : (player_right ? 90 : - 1);

		} else {
			fly_angle = player_left ? - 90 : (player_right ? 90 : - 1);

			if (player_forward) {
				fly_angle /= 2;
			} else if (player_back) {
				fly_angle = 180 - (fly_angle / 2);
			}
		}

		if (fly_angle != - 1 && (player_forward || player_back || player_left || player_right)) {
			float yaw = mc.player.rotationYaw + fly_angle;
			mc.player.motionX = EntityUtil.getRelativeX(yaw) * 0.2f;
			mc.player.motionZ = EntityUtil.getRelativeZ(yaw) * 0.2f;
		}

		mc.player.motionY = 0;
		mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? 0.0622 : 0) - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.0622 : 0), mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
		mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY - 42069, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
		
		if (no_kick.getValue()) {
			return;
		}
	}
}