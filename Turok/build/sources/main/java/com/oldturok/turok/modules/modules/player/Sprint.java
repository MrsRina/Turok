package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;

// Update by Rina 09/03/20.
@Module.Info(name = "Sprint", description = "Automatically makes the player sprint", category = Module.Category.TUROK_PLAYER)
public class Sprint extends Module {

	@Override
	public void onUpdate() {
		if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) && !(mc.player.isSneaking()) && !(mc.player.collidedHorizontally) && !(mc.player.getFoodStats().getFoodLevel() <= 6f)) {
			mc.player.setSprinting(true);
		}
	}
}
