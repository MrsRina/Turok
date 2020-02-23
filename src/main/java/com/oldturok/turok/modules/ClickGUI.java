package com.oldturok.turok.modules;

import com.oldturok.turok.managers.GUIManager;
import com.oldturok.turok.modules.api.Module;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGui", Keyboard.KEY_LCONTROL);
    }

    @Override
    public void onGui(int offset) {}

    @Override
    public void onToggle(boolean state) {
        minecraft.displayGuiScreen(new GUIManager());
    }
}
