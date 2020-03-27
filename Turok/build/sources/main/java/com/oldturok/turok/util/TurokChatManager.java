package com.oldturok.turok.util;

import com.oldturok.turok.commands.*;
import com.oldturok.turok.TurokChat;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;

import java.util.ArrayList;
import java.util.HashMap;

// Rina.
public class TurokChatManager {
	public static ArrayList<TurokChat> command_list = new ArrayList<TurokChat>();
	static HashMap<String, TurokChat> lookup        = new HashMap<>();

	public final String prefix;
	public final Style format;

	public static void update_chat_to_accept_commands() {
		lookup.clear();

		for (TurokChat commands :  command_list) {
			lookup.put(commands.get_name().toLowerCase(), commands);
		}
	}

	public TurokChatManager(String prefix_, Style format_) {
		prefix = prefix_;
		format = format_;

		command_list.add(new TurokHelp());
		command_list.add(new TurokPos());
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

	public static ArrayList<TurokChat> get_commands() {
		return command_list;
	}

	public static TurokChat get_command(String cmd) {
		return lookup.get(cmd.toLowerCase());
	}
}