package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;

// Update by Rina 09/03/20.
@Module.Info(name = "NoHurtEffect", category = Module.Category.TUROK_RENDER, description = "Disables the 'hurt' camera effect")
public class NoHurtEffect extends Module {

    private static NoHurtEffect INSTANCE;

    public NoHurtEffect() {
        INSTANCE = this;
    }

    public static boolean shouldDisable() {
        return INSTANCE != null && INSTANCE.isEnabled();
    }

}