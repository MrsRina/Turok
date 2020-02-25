package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.module.Module;

@Module.Info(name = "Fastplace", category = Module.Category.TUROK_PLAYER, description = "Nullifies block place delay")
public class Fastplace extends Module {

    @Override
    public void onUpdate() {
        mc.rightClickDelayTimer = 0;
    }
}
