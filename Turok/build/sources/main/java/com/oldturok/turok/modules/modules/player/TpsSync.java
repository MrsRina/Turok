package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.module.Module;

// Update by Rina 09/03/20.
@Module.Info(name = "TpsSync", description = "Synchronizes some actions with the server TPS", category = Module.Category.TUROK_PLAYER)
public class TpsSync extends Module {
    private static TpsSync INSTANCE;

    public TpsSync() {
        INSTANCE = this;
    }

    public static boolean isSync() {
        return INSTANCE.isEnabled();
    }

}