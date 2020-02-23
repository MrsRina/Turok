package com.oldturok.turok.modules;

import com.oldturok.turok.Turok;
import com.oldturok.turok.modules.api.Module;
import org.lwjgl.input.Keyboard;

public class Panic extends Module {
    public Panic() {
        super("Panic", Keyboard.KEY_P);
    }

    @Override
    public void onGui(int offset) {} // panic is not meant to display to the gui. It's meant to clear it

    @Override
    public void onToggle(boolean state) {
        super.onToggle(state);

        if (Turok.client.moduleManager.getEnabledModules().size() > 1) {
            for (Module module : Turok.client.moduleManager.getEnabledModules()) {
                module.Toggle();
            }
        }
    }
}
