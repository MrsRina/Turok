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
		chat_manager = new TurokChatManager(new Style().setColor(TextFormatting.GRAY));
	}

	public static void turok_update_commands() {
		chat_manager.update_chat_to_accept_commands();
	}

	public void set_prefix(String new_prefix) {
		chat_manager.set_prefix(new_prefix);
	}

	public String get_prefix() {
		return chat_manager.get_prefix();
	}

	@SubscribeEvent
	public void onChat(ClientChatEvent event) {
		String msg        = event.getMessage();
		String[] msg_args = chat_manager.GetArgs(event.getMessage());

		boolean command_us = false;

		if (msg_args.length > 0) {
			for (TurokChat chat : chat_manager.command_list) {
				try {
					if (chat_manager.GetArgs(event.getMessage())[0].equalsIgnoreCase(chat.name)) {
						command_us = chat.Get_Message(chat_manager.GetArgs(event.getMessage()));
					}
				} catch (Exception exc) {} // Gay!?
			}

			if (!command_us && chat_manager.ContainsPrefix(event.getMessage())) {
				
				TurokMessage.send_client_msg("Use command help or talk with Rina.");

				command_us = false;
			}

			event.setMessage("");
		}
	}
}
