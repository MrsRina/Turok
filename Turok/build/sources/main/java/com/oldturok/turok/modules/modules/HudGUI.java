package com.oldturok.turok.module.modules;

import com.oldturok.turok.gui.turok.DisplayHudScreen;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMod;

import org.lwjgl.input.Keyboard;

// Rina.
@Module.Info(name = "HudGUI", category = Module.Category.TUROK_HIDDEN)
public class HudGUI extends Module {
	public HudGUI(){
		getBind().setKey(TurokMod.TUROK_HUD_BUTTON);
	}

	@Override
	public void onEnable() {
		if (!(mc.currentScreen instanceof DisplayHudScreen)) {
			mc.displayGuiScreen(new DisplayHudScreen(mc.currentScreen));
		}

		disable();
	}
}