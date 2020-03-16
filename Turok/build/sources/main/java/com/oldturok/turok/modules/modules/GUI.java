package com.oldturok.turok.module.modules;

import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.gui.turok.*;
import com.oldturok.turok.TurokMod;

import org.lwjgl.input.Keyboard;

import com.oldturok.turok.util.TurokGL; // TurokGL.

// Update by Rina 09/03/20.
// CickGUI is P.
@Module.Info(name = "clickGUI", category = Module.Category.TUROK_HIDDEN)
public class GUI extends Module {
    public GUI() {
        getBind().setKey(TurokMod.TUROK_GUI_BUTTON);
    }

    @Override
    protected void onEnable() {
        if (!(mc.currentScreen instanceof DisplayGuiScreen)) {
            TurokGL.refresh_color(190, 190, 190, 50);
            RenderHelper.drawFilledRectangle(0, 0, mc.displayHeight, mc.displayWidth);

            mc.displayGuiScreen(new DisplayGuiScreen(mc.currentScreen));
        }

        disable();
    }
}
