package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.module.Module;

@Module.Info(name = "Fastbreak", category = Module.Category.TUROK_PLAYER, description = "Nullifies block hit delay")
public class Fastbreak extends Module {

    @Override
    public void onUpdate() {
        mc.playerController.blockHitDelay = 0;
    }
}
