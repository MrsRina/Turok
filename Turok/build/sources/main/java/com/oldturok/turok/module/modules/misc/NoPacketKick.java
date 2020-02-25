package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.module.Module;

@Module.Info(name = "NoPacketKick", category = Module.Category.TUROK_MISC, description = "Prevent large packets from kicking you")
public class NoPacketKick {
    private static NoPacketKick INSTANCE;

    public NoPacketKick() {
        INSTANCE = this;
    }

    public static boolean isEnabled() {
        return INSTANCE.isEnabled();
    }

}
