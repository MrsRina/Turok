package com.oldturok.turok.util;

import com.oldturok.turok.commands.*;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;

import java.util.ArrayList;

// Rina.
public class TurokChatManager {
	public ArrayList<TurokChat> command_list = new ArrayList<TurokChat>();

	public Style format;

	public TurokChatManager(Style format_) {
		format = format_;

		command_list.add(new TurokPrefix());
	}

	public String[] GetArgs(String message) {
		String[] arguments = {};

		if (message.startsWith(TurokMod.TUROK_CHAT_PREFIX)) {
			arguments = message.replaceFirst(TurokMod.TUROK_CHAT_PREFIX, "").split(" ");
		}

		return arguments;
	}

	public boolean ContainsPrefix(String message) {
		return message.startsWith(TurokMod.TUROK_CHAT_PREFIX);
	}
}