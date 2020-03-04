package com.oldturok.turok.module.modules.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.TurokEvent;
import com.oldturok.turok.event.events.EntityEvent;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

// Rina.
// Modify in 03/03/2020.
@Module.Info(name = "Ant-kb", description = "Modify knockback.", category = Module.Category.TUROK_MOVEMENT)
public class Antkb extends Module {
	private Setting<Float> horizontal = register(Settings.f("Velocity Horizontal", 0));
	private Setting<Float> vertical = register(Settings.f("Velocity Vertical", 0));

	@EventHandler
	private Listener<PacketEvent.Receive> packetEventListener = new Listener<>(event -> {
		if (event.getEra() == TurokEvent.Era.PRE) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                SPacketEntityVelocity velocity = (SPacketEntityVelocity) event.getPacket();
                if (velocity.getEntityID() == mc.player.entityId) {
                    if (horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                    velocity.motionX *= horizontal.getValue();
                    velocity.motionY *= vertical.getValue();
                    velocity.motionZ *= horizontal.getValue();
                }
            } else if (event.getPacket() instanceof SPacketExplosion) {
                if (horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                SPacketExplosion velocity = (SPacketExplosion) event.getPacket();
                velocity.motionX *= horizontal.getValue();
                velocity.motionY *= vertical.getValue();
                velocity.motionZ *= horizontal.getValue();
            }
        }
    });

    @EventHandler
    private Listener<EntityEvent.EntityCollision> entityCollisionListener = new Listener<>(event -> {
        if (event.getEntity() == mc.player) {
            if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                event.cancel();
                return;
            }
            event.setX(-event.getX() * horizontal.getValue());
            event.setY(0);
            event.setZ(-event.getZ() * horizontal.getValue());
        }
	});
}