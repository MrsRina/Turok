package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;

// Modify by Rina 05/03/20.
@Module.Info(name = "AntiFog", description = "Disables or reduces fog", category = Module.Category.TUROK_RENDER)
public class AntiFog extends Module {
    public static Setting<VisionMode> mode = Settings.e("Mode", VisionMode.NOFOG);
    private static AntiFog INSTANCE = new AntiFog();

    public AntiFog() {
        INSTANCE = this;
        register(mode);
    }

    public static boolean enabled() {
        return INSTANCE.isEnabled();
    }

    public enum VisionMode {
        NOFOG, AIR
    }

}