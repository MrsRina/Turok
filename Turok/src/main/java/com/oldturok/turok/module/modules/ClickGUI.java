package com.oldturok.turok.module.modules;

import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import com.oldturok.turok.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Info(name = "clickGUI", description = "Opens the Click GUI", category = Module.Category.TUROK_NULL)
public class ClickGUI extends Module {

    public ClickGUI() {
        getBind().setKey(Keyboard.KEY_P);
    }

    @Override
    protected void onEnable() {
        if (!(mc.currentScreen instanceof DisplayGuiScreen)) {
            mc.displayGuiScreen(new DisplayGuiScreen(mc.currentScreen));
        }
        disable();
    }

}
