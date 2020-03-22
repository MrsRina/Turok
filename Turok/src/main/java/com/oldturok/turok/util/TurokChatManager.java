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
	public String prefix;

	public TurokChatManager(String prefix, Style format_) {
		prefix = prefix;
		format = format_;

		command_list.add(new TurokHelp());
	}

	public void set_prefix(String prefix) {
		prefix = prefix;
	}

	public String get_prefix() {
		return prefix;
	}

	public String[] GetArgs(String message) {
		String[] arguments = {};

		if (message.startsWith(prefix)) {
			arguments = message.replaceFirst(prefix, "").split(" ");
		}

		return arguments;
	}

	public boolean ContainsPrefix(String message) {
		return message.startsWith(prefix);
	}
}