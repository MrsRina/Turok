package com.oldturok.turok.module.modules.chat;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokMod;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

// TurokIRC;
import com.oldturok.turok.util.TurokIRCManager;
import com.oldturok.turok.TurokIRC;

// Rina.
@Module.Info(name = "TurokChatIRC", description = "Talk with others users Turok!", category = Module.Category.TUROK_CHAT)
public class TurokChatIRC extends Module {
	public static boolean state = false;

	@Override
	public void onDisable() {
		if (TurokMod.chat_irc.isConnected()) {
			TurokMod.chat_irc.disconnect();

			TurokMessage.send_client_msg("Turok IRC have disconnected.");
			state = false;
		}
	}

	@Override
	public void onEnable() {
		if (mc.world != null) {
			new Thread("enable IRC") {
				@Override
				public void run() {
					try {
						TurokMessage.send_client_msg("Turok connecting to IRC...");

						TurokMod.chat_irc = new TurokIRCManager(mc.getSession().getUsername());
						TurokMod.chat_irc.start_connect();

						TurokMessage.send_client_msg("Turok made connect, use -s for use IRC.");

						state = true;
					} catch (Exception exc) {}
				}
			}.start();
		} else {
			TurokMod.chat_irc = new TurokIRCManager(mc.getSession().getUsername());
			TurokMod.chat_irc.start_connect();

			state = true;
		}
	}

	@Override
	public void onUpdate() {
		if (state) {
			if (TurokMod.chat_irc.isConnected()) {
				if (TurokMod.chat_irc.newMessages()) {
					for (TurokIRC irc : TurokMod.chat_irc.getUnreadLines()) {
						mc.player.sendMessage(new TextComponentString("[Turok-Chat] -> " + irc.get_sender() + irc.get_line().replaceAll(">", TextFormatting.GREEN + ">")));

						irc.set_read(true);	
					}
				}
			}
		}
	}
}