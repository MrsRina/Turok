package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;

// Modify by Rina 05/03/20.
@Module.Info(name = "NoHurtCam", category = Module.Category.TUROK_RENDER, description = "Disables the 'hurt' camera effect")
public class NoHurtCam extends Module {

    private static NoHurtCam INSTANCE;

    public NoHurtCam() {
        INSTANCE = this;
    }

    public static boolean shouldDisable() {
        return INSTANCE != null && INSTANCE.isEnabled();
    }

}