package com.oldturok.turok.module.modules;

import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMod;

import org.lwjgl.input.Keyboard;

@Mopdule.Info(name = "HudGUI", category = Module.Category.TUROK_HIDDEN)
public class HudGUI extends Module {
	public HudGUI {
		getBind().setKey(TurokMod.TUROK_HUD_BUTTON);
	}

	@Override
	public void onEnable() {
		if (!(mc.currentScreen instanceof DisplayGuiScreen)) {
			mc.displayGuiScreen(new DisplayGuiScreen(mc.currentScreen));
		}

		disable();
	}
}