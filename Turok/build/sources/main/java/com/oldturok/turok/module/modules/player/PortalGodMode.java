package com.oldturok.turok.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import net.minecraft.network.play.client.CPacketConfirmTeleport;

import java.util.LinkedList;
import java.util.Queue;

@Module.Info(name = "PortalGodMode", category = Module.Category.TUROK_PLAYER)
public class PortalGodMode extends Module {

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (isEnabled() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.cancel();
        }
    });
}
