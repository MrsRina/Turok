package com.oldturok.turok.module.modules.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Info(name = "AntiHunger", category = Module.Category.TUROK_MOVEMENT, description = "Lose hunger less fast. Might cause ghostblocks.")
public class AntiHunger extends Module {

    @EventHandler
    public Listener<PacketEvent.Send> packetListener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer) event.getPacket()).onGround = false;
        }
    });

}
