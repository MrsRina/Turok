package com.oldturok.turok.chat;

import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.chatcmd.Chat;
import com.oldturok.turok.util.Wrapper;

// Context:
import com.oldturok.turok.gui.turok.widgets.TurokFrameUI;

public class TurokDev extends Chat {
	public TurokDev() {
		super("dev");
	}

	@Override
	public void getMessage(String[] args) {
		String message = args[0];
		String value   = args[1];

		if (message != null) {
			if (message.equalsIgnoreCase("speed_effect")) {
				TurokFrameUI.speed_effect = Integer.parseInt(value.trim());

				Chat.sendChatMessage("Effect Speed seted to " + value);
			}

			return;
		} else {
			Chat.sendChatMessage("Try something.");

			return;
		}
	}
}