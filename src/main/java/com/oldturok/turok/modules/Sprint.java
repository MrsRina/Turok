package com.oldturok.turok.modules;

import com.oldturok.turok.modules.api.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Keyboard.KEY_V);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (canSprint()) {
            minecraft.player.setSprinting(true);
        }
    }

    private boolean canSprint() {
        return (minecraft.player.moveForward > 0 && !minecraft.player.isActiveItemStackBlocking() && !minecraft.player.isOnLadder() && !minecraft.player.collidedHorizontally && minecraft.player.getFoodStats().getFoodLevel() > 6);
    }
}
