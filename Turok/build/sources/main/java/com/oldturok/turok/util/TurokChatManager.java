package com.oldturok.turok.util;

import com.oldturok.turok.util.ClassFinder;
import com.oldturok.turok.util.TurokString;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.commands.*;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Rina.
public class TurokChatManager {
	private ArrayList<TurokChat> chat_commands = new ArrayList<>();

	public TurokChatManager() {
		Set<Class> class_list = ClassFinder.findClasses(TurokPrefix.class.getPackage().getName(), TurokChat.class);
		
		for (Class class_l : class_list) {
			if (TurokChat.class.isAssignableFrom(class_l)) {
				try {
					TurokChat command = (TurokChat) class_l.getConstructor().newInstance();

					chat_commands.add(command);
				} catch (Exception exc) {
					exc.printStackTrace();

					System.err.println("Couldn't initiate command " + class_l.getSimpleName() + "! Err: " + exc.getClass().getSimpleName() + ", message: " + exc.getMessage());
				}
			}
		}
	}

	public void call_command(String commands) {
		String[] command_parts = commands.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		String name            = command_parts[0].substring(1);
		String[] message       = TurokString.remove_element(command_parts, 0);

		for (int int_ = 0; int_ < message.length; int_++) {
			if (message[int_] == null) {
				continue;
			}

			message[int_] = TurokString.strip(message[int_], "\"");
		}

		for (TurokChat command : chat_commands) {
			if (command.get_name().equalsIgnoreCase(name)) {
				command.command(command_parts);

				return;
			}
		}

		TurokMessage.send_client_msg("Try use -help for get help ^^.");
	}

	public TurokChat get_command(String name_) {
		for (TurokChat command : chat_commands) {
			if (command.get_name().equals(name_)) {
				return command;
			}
		}

		return null;
	}

	public ArrayList<TurokChat> get_commands() {
		return chat_commands;
	}
}