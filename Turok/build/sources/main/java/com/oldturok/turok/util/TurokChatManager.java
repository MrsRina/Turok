package com.oldturok.turok.util;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
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

	public static final Setting<String> prefix = Settings.s("Prefix", "-");

	public final Style format;

	public static void update_chat_to_accept_commands() {
		lookup.clear();

		for (TurokChat commands :  command_list) {
			lookup.put(commands.get_name().toLowerCase(), commands);
		}
	}

	public TurokChatManager(Style format_) {
		format = format_;

		command_list.add(new TurokFriend());
		command_list.add(new TurokHelp());
		command_list.add(new TurokListFriends());
		command_list.add(new TurokPos());
		command_list.add(new TurokPrefix());
	}

	public String[] GetArgs(String message) {
		String[] arguments = {};

		if (message.startsWith(prefix.getValue())) {
			arguments = message.replaceFirst(prefix.getValue(), "").split(" ");
		}

		return arguments;
	}

	public boolean ContainsPrefix(String message) {
		return message.startsWith(prefix.getValue());
	}

	public static ArrayList<TurokChat> get_commands() {
		return command_list;
	}

	public String get_prefix() {
		return prefix.getValue();
	}

	public void set_prefix(String new_prefix) {
		prefix.setValue(new_prefix);
	}

	public static TurokChat get_command(String cmd) {
		return lookup.get(cmd.toLowerCase());
	}
}