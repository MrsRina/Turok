package com.oldturok.turok.modules;

import com.oldturok.turok.modules.api.Module;
import org.lwjgl.input.Keyboard;

public class Rainbow extends Module {
    public Rainbow() {
        super("Rainbow", Keyboard.KEY_NONE);
    }

    @Override
    public void onGui(int offset) {}
}
