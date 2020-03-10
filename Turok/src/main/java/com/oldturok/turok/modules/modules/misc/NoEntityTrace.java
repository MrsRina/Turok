package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

// Update by Rina 09/03/20.
@Module.Info(name = "NoEntityTrace", category = Module.Category.TUROK_MISC, description = "Blocks entities from stopping you from mining")
public class NoEntityTrace extends Module {
    private Setting<TraceMode> mode = register(Settings.e("Mode", TraceMode.DYNAMIC));

    private static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        NoEntityTrace.INSTANCE = this;
    }

    public static boolean shouldBlock() {
        return INSTANCE.isEnabled() && (INSTANCE.mode.getValue() == TraceMode.STATIC || mc.playerController.isHittingBlock);
    }

    private enum TraceMode {
        STATIC, DYNAMIC
    }
}