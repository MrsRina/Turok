package com.oldturok.turok.gui;

import com.oldturok.turok.util.TurokChatManager;
import com.oldturok.turok.TurokChat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;

public class TurokChatCommand {
	public TurokChatManager chat_manager;

	public TurokChatCommand() {
		chat_manager = new TurokChatManager("-", new Style().setColor(TextFormatting.GRAY));
	}

	@SubscribeEvent
	public void onChat(ClientChatEvent event) {
		String msg        = event.getMessage();
		String[] msg_args = chat_manager.GetArgs(event.getMessage());

		boolean command_us = false;

		if (msg_args.length > 0) {
			for (TurokChat chat : chat_manager.command_list) {
				if (chat_manager.GetArgs(event.getMessage())[0].equalsIgnoreCase(chat.name)) {
					command_us = TurokChat.get_message(chat_manager.GetArgs(event.getMessage()));
				}
			}

			if (!command_us && chat_manager.ContainsPrefix(event.getMessage())) {
				TurokChat.send_msg("A error ocurred.");

				command_us = false;
			}

			event.setMessage("");
		}
	}
}
