package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.module.Module;

// Modify by Rina 05/03/20.
@Module.Info(name = "NoPackKick", category = Module.Category.TUROK_MISC)
public class NoPackKick {
	private static NoPackKick INSTANCE;

	public NoPackKick() {
		INSTANCE = this;
	}

	public static boolean isEnabled() {
		return INSTANCE.isEnabled();
	}
}