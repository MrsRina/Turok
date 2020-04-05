package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

// Rina.
@Module.Info(name = "CustomFOV", description = "Custom FOV?", category = Module.Category.TUROK_RENDER)
public class CustomFov extends Module {
	private Setting<Float> custom_fov = register(Settings.floatBuilder("FOV").withMinimum(30f).withValue(110f).withMaximum(180f));

	public static float old_fov = mc.gameSettings.fovSetting;

	@Override
	public void onDisable() {
		mc.gameSettings.fovSetting = old_fov;
	}

	@Override
	public void onUpdate() {
		mc.gameSettings.fovSetting = custom_fov.getValue();
	}

	@Override
	public String getHudInfo() {
		return String.valueOf(custom_fov.getValue());
	}
}