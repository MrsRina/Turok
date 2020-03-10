package com.oldturok.turok.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;

import java.util.LinkedList;
import java.util.Queue;

// Update by Rina 09/03/20.
@Module.Info(name = "Blink", category = Module.Category.TUROK_PLAYER)
public class Blink extends Module {
    Queue<CPacketPlayer> packets = new LinkedList<>();
    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (isEnabled() && event.getPacket() instanceof CPacketPlayer) {
            event.cancel();
            packets.add((CPacketPlayer) event.getPacket());
        }
    });
    private EntityOtherPlayerMP clonedPlayer;

    @Override
    protected void onEnable() {
        if (mc.player != null) {
            clonedPlayer = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
            clonedPlayer.copyLocationAndAnglesFrom(mc.player);
            clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
            mc.world.addEntityToWorld(-100, clonedPlayer);
        }
    }

    @Override
    protected void onDisable() {
        while (!packets.isEmpty())
            mc.player.connection.sendPacket(packets.poll());

        EntityPlayer localPlayer = mc.player;
        if (localPlayer != null) {
            mc.world.removeEntityFromWorld(-100);
            clonedPlayer = null;
        }
    }

    @Override
    public String getHudInfo() {
        return String.valueOf(packets.size());
    }
}