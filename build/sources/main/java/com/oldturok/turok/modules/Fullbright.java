package com.oldturok.turok.modules;

import com.oldturok.turok.modules.api.Module;
import org.lwjgl.input.Keyboard;

public class Fullbright extends Module {
    private float previousGamma;
    public Fullbright() {
        super("Fullbright", Keyboard.KEY_B);
    }

    @Override
    public void onToggle(boolean state) {
        super.onToggle(state);

        if (state) previousGamma = minecraft.gameSettings.gammaSetting;
        minecraft.gameSettings.gammaSetting = (state ? 100f : previousGamma);
    }
}
