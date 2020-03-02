package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

// Rina.
@Module.Info(name = "InventoryWalker", category = Module.Category.TUROK_MOVEMENT)
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
			KeyBinding.setKeyBindState(FORWARD, true);
		} else {
			KeyBinding.setKeyBindState(FORWARD, false);
		}

		if (Keyboard.isKeyDown(BACK)){
			KeyBinding.setKeyBindState(BACK, true);
		} else {
			KeyBinding.setKeyBindState(BACK, false);
		}

		if (Keyboard.isKeyDown(RIGHT)) {
			KeyBinding.setKeyBindState(RIGHT, true);
		} else {
			KeyBinding.setKeyBindState(RIGHT, false);
		}

		if (Keyboard.isKeyDown(LEFT)) {
			if (mc.player.onGround) {
				KeyBinding.setKeyBindState(LEFT, true);
			}
		} else {
			if (mc.player.onGround) {
				KeyBinding.setKeyBindState(LEFT, false);
			}
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