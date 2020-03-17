package com.oldturok.turok.util;

import com.oldturok.turok.commands.*;
import com.oldturok.turok.TurokChat;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;

import java.util.ArrayList;

// Rina.
public class TurokChatManager {
	public ArrayList<TurokChat> command_list = new ArrayList<TurokChat>();

	public final String prefix;
	public final Style format;

	public TurokChatManager(String prefix_, Style format_) {
		prefix = prefix_;
		format = format_;

		command_list.add(new TurokDev());
		command_list.add(new TurokPrefix());
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