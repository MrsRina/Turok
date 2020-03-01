package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.turokrpc.TurokDiscordP;

// Rina.
@Module.Info(name = "TurokRPC", category = Module.Category.TUROK_MISC)
public class TurokRPC extends Module {
	@Override
	public void onEnable() {
		TurokDiscordP.start();
	}
}