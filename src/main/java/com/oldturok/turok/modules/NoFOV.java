package com.oldturok.turok.modules;

import com.oldturok.turok.modules.api.Module;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoFOV extends Module {
    public NoFOV() {
        super("NoFOV", Keyboard.KEY_C);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onFOV(FOVUpdateEvent event) {
        if(getState()) {
            if (event.getEntity().isSprinting()) {
                event.setNewfov(1.15f);
            }
            else {
                event.setNewfov(1f);
            }
        }
    }
}
