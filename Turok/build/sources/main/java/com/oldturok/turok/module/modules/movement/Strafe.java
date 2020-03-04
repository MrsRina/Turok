package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.event.events.EntityEvent;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.EventHandler;
import com.oldturok.turok.event.events.PlayerMoveEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraft.util.math.MathHelper;

// Rina.
@Module.Info(name = "Strafe", category = Module.Category.TUROK_HIDDEN)
public class Strafe extends Module {
	float player_forward;
	float player_strafe;
	float player_pitch;
	float player_yaw;

	@EventHandler
	private Listener<EntityEvent.EntityCollision> payerMoveEvent = new Listener<>(event -> {
		player_forward = mc.player.movementInput.moveForward;
		player_strafe = mc.player.movementInput.moveStrafe;
		player_pitch = mc.player.rotationPitch;
		player_yaw = mc.player.rotationYaw;

		if (player_forward == 0.0f && player_strafe == 0.0f) {
			event.setX(0.0f);
			event.setZ(0.0f);

		} else {
			if (player_forward != 0.0f) {
				if (player_strafe > 0.0f) {
					player_yaw += (player_strafe > 0.0f) ? -45 : 45;

				} else if (player_strafe < 0.0f) {
					player_yaw += (player_strafe > 0.0f) ? 45 : -45;
				}

				player_strafe = 0.0f;

				if (player_forward > 0.0f) {
					player_forward = 0.5f;
				} else if (player_forward < 0.0f) {
					player_forward = - 0.5f;
				}
			}

			event.setX((player_forward * 0.2000f) * Math.cos(Math.toRadians((player_yaw + 90.0f))) + (player_strafe * 0.2000f) * Math.sin(Math.toRadians((player_yaw + 90.0f))));
			event.setZ((player_forward * 0.2000f) * Math.sin(Math.toRadians((player_yaw + 90.0f))) - (player_strafe * 0.2000f) * Math.cos(Math.toRadians((player_yaw + 90.0f))));
		}
	});

}