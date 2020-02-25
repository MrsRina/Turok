package com.oldturok.turok.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@Module.Info(name = "AntiForceLook", category = Module.Category.TUROK_PLAYER)
public class AntiForceLook extends Module {

    @EventHandler
    Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {
        if (mc.player == null) return;
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    });

}
