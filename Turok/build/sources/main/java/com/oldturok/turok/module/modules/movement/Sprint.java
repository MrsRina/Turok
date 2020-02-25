package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;

@Module.Info(name = "Sprint", description = "Automatically makes the player sprint", category = Module.Category.TUROK_MOVEMENT)
public class Sprint extends Module {

    @Override
    public void onUpdate() {
        try {
            if (!mc.player.collidedHorizontally && mc.player.moveForward > 0)
                mc.player.setSprinting(true);
            else
                mc.player.setSprinting(false);
        } catch (Exception ignored) {}
    }

}
