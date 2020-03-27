package com.oldturok.turok.module.modules.combat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import com.oldturok.turok.event.events.EntityEvent;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.event.TurokEvent;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

@Module.Info(name = "AntiKnockback", description = "Modify knockback to 0.", category = Module.Category.TUROK_MOVEMENT)
public class AntiKnockback extends Module {
	@EventHandler
	private Listener<PacketEvent.Receive> packetEventListener = new Listener<>(event -> {
		if (event.getEra() == TurokEvent.Era.PRE) {
			if (event.getPacket() instanceof SPacketEntityVelocity) {
				SPacketEntityVelocity knockback = (SPacketEntityVelocity) event.getPacket();

				if (knockback.getEntityID() == mc.player.entityId) {
					event.cancel();

					knockback.motionX *= 0.0f;
					knockback.motionY *= 0.0f;
					knockback.motionZ *= 0.0f;
				}
			} else if (event.getPacket() instanceof SPacketExplosion) {
				event.cancel();

				SPacketExplosion knockback = (SPacketExplosion) event.getPacket();

				knockback.motionX *= 0.0f;
				knockback.motionY *= 0.0f;
				knockback.motionZ *= 0.0f;
			}
		}
	});

	@EventHandler
	private Listener<EntityEvent.EntityCollision> entityCollisionListener = new Listener<>(event -> {
		if (event.getEntity() == mc.player) {
			event.cancel();

			return;
		}
	});
}