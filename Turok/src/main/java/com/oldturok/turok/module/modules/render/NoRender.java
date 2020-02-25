package com.oldturok.turok.module.modules.render;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.*;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;

@Module.Info(name = "NoRender", category = Module.Category.TUROK_RENDER, description = "Ignore entity spawn packets")
public class NoRender extends Module {

    private Setting<Boolean> mob = register(Settings.b("Mob", false));
    private Setting<Boolean> gentity = register(Settings.b("GEntity", false));
    private Setting<Boolean> object = register(Settings.b("Object", false));
    private Setting<Boolean> xp = register(Settings.b("XP", false));
    private Setting<Boolean> paint = register(Settings.b("Paintings", false));
    private Setting<Boolean> fire = register(Settings.b("Fire"));

    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {
        Packet packet = event.getPacket();
        if ((packet instanceof SPacketSpawnMob && mob.getValue()) ||
                (packet instanceof SPacketSpawnGlobalEntity && gentity.getValue()) ||
                (packet instanceof SPacketSpawnObject && object.getValue()) ||
                (packet instanceof SPacketSpawnExperienceOrb && xp.getValue()) ||
                (packet instanceof SPacketSpawnPainting && paint.getValue()))
            event.cancel();
    });

    @EventHandler
    public Listener<RenderBlockOverlayEvent> blockOverlayEventListener = new Listener<>(event -> {
        if (fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) event.setCanceled(true);
    });

}
