package com.oldturok.turok.module.modules.combat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;

import java.util.function.Predicate;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;

// Rina.
// Update by Rina in 05/03/20.
@Module.Info(name = "TurokCriticals", category = Module.Category.TUROK_COMBAT)
public class TurokCriticals extends Module {
	private Listener<AttackEntityEvent> packetEvent = new Listener<>(event -> {
		if (!mc.player.isInLava() && !mc.player.isInWater()) {
				if (mc.player.onGround) {
					mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1625, mc.player.posZ, false));
					mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));

					mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 4.0E-6, mc.player.posZ, false));
					mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));					

					mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0E-6, mc.player.posZ, false));
					mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));

					mc.player.connection.sendPacket((Packet) new CPacketPlayer());

					mc.player.onCriticalHit(event.getTarget());
				}
			}
	}, (Predicate<AttackEntityEvent>[]) new Predicate[0]);

	@Override
	public String getHudInfo() {
		return String.valueOf("packet");
	}
}