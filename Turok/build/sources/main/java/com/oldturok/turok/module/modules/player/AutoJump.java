package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.module.Module;

@Module.Info(name = "AutoJump", category = Module.Category.TUROK_PLAYER, description = "Automatically jumps if possible")
public class AutoJump extends Module {

    @Override
    public void onUpdate() {
        if (mc.player.isInWater() || mc.player.isInLava()) mc.player.motionY = 0.1;
        else if (mc.player.onGround) mc.player.jump();
    }

}
