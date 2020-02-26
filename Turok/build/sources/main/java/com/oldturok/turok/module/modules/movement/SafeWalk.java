package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;

@Module.Info(name = "SafeWalk", category = Module.Category.TUROK_MOVEMENT, description = "Keeps you from walking off edges")
public class SafeWalk extends Module {

    private static SafeWalk INSTANCE;

    public SafeWalk() {
        INSTANCE = this;
    }

    public static boolean shouldSafewalk() {
        return INSTANCE.isEnabled();
    }
}