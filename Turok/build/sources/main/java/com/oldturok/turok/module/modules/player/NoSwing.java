package com.oldturok.turok.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import net.minecraft.network.play.client.CPacketAnimation;

@Module.Info(name = "NoSwing", category = Module.Category.TUROK_PLAYER, description = "Prevents arm swing animation server side")
public class NoSwing extends Module {

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketAnimation) {
            event.cancel();
        }
    });

}
