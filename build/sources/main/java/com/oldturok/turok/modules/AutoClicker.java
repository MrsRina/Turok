package com.oldturok.turok.modules;

import com.oldturok.turok.helpers.DelayTimer;
import com.oldturok.turok.modules.api.Module;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class AutoClicker extends Module {
    private final Random random = new Random();
    private DelayTimer delayTimer = new DelayTimer();

    private int min_cps = 9, max_cps = 13;

    public AutoClicker() {
        super("AutoClicker", Keyboard.KEY_R);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        MouseClick();
    }

    private void MouseClick() {
        if (minecraft.gameSettings.keyBindAttack.isKeyDown()) {
            int _a = 1000 / (random.nextInt(max_cps - min_cps) + min_cps);
            if (delayTimer.hasTimeElapsed(_a, true)) {
            	minecraft.player.swingArm(minecraft.player.swingingHand);
            	switch (minecraft.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        minecraft.playerController.attackEntity(minecraft.player, minecraft.objectMouseOver.entityHit);
                        break;
                    case BLOCK:
                        minecraft.playerController.clickBlock(minecraft.objectMouseOver.getBlockPos(), minecraft.objectMouseOver.sideHit);
                        break;
                }
            }
        }
        else {
            delayTimer.resetTime();
        }
    }
}
