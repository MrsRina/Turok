package com.oldturok.turok.module.modules;

import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMod;
import org.lwjgl.input.Keyboard;

// Modify by Rina 05/03/20.
// CickGUI is 
@Module.Info(name = "clickGUI", description = "Opens the Click GUI", category = Module.Category.TUROK_HIDDEN)
public class ClickGUI extends Module {
    public ClickGUI() {
        getBind().setKey(TurokMod.TUROK_GUI_BUTTON);
    }

    @Override
    protected void onEnable() {
        if (!(mc.currentScreen instanceof DisplayGuiScreen)) {
            mc.displayGuiScreen(new DisplayGuiScreen(mc.currentScreen));
        }
        disable();
    }

}
