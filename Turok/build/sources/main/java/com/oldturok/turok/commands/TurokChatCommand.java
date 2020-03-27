package com.oldturok.turok.commands;

import com.oldturok.turok.util.TurokChatManager;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;

// Rina.
public class TurokChatCommand {
	public static TurokChatManager chat_manager;

	public TurokChatCommand() {
		chat_manager = new TurokChatManager(TurokMod.TUROK_CHAT_PREFIX, new Style().setColor(TextFormatting.GRAY));
	}

	public static void turok_update_commands() {
		chat_manager.update_chat_to_accept_commands();
	}

	@SubscribeEvent
	public void onChat(ClientChatEvent event) {
		String msg        = event.getMessage();
		String[] msg_args = chat_manager.GetArgs(event.getMessage());

		boolean command_us = false;

		if (msg_args.length > 0) {
			for (TurokChat chat : chat_manager.command_list) {
				if (chat_manager.GetArgs(event.getMessage())[0].equalsIgnoreCase(chat.name)) {
					command_us = chat.Get_Message(chat_manager.GetArgs(event.getMessage()));
				}
			}

			if (!command_us && chat_manager.ContainsPrefix(event.getMessage())) {
				
				TurokMessage.send_client_msg("Use command help for get hElP.");

				command_us = false;
			}

			event.setMessage("");
		}
	}
}
