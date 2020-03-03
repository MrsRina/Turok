package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

// Rina.
@Module.Info(name = "InventoryWalker", category = Module.Category.TUROK_HIDDEN)
public class InventoryWalker extends Module {
	int FORWARD = mc.gameSettings.keyBindForward.getKeyCode();
	int BACK = 	mc.gameSettings.keyBindBack.getKeyCode();
	int RIGHT = mc.gameSettings.keyBindRight.getKeyCode();
	int LEFT = mc.gameSettings.keyBindLeft.getKeyCode();
	int JUMP = mc.gameSettings.keyBindJump.getKeyCode();

	@Override
	public void onUpdate() {
		if (mc.currentScreen instanceof GuiChat || mc.currentScreen == null) return;

		if (Keyboard.isKeyDown(FORWARD)) {
			mc.player.motionX -= 0.10f;
		}

		if (Keyboard.isKeyDown(BACK)) {
			mc.player.motionX += 0.10f;
		}

		if (Keyboard.isKeyDown(RIGHT)) {
			mc.player.motionZ -= 0.10f;
		}

		if (Keyboard.isKeyDown(LEFT)) {
			mc.player.motionZ += 0.1f;
		}

		if (Keyboard.isKeyDown(JUMP)) {
			if (mc.player.isInLava() || mc.player.isInWater()) {
				mc.player.motionY += 0.38f;
			} else {
				if (mc.player.onGround) {
					mc.player.jump();
				}
			}
		}
	}
}