package com.oldturok.turok.chatcmd;

import com.oldturok.turok.chat.BindCommand;
import com.oldturok.turok.util.ClassFinder;
import com.oldturok.turok.TurokMod;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatManager {
	private ArrayList<Chat> chat_commands;
	
	public ChatManager() {

		Set<Class> classList = ClassFinder.findClasses(BindCommand.class.getPackage().getName(), Chat.class);
		for (Class s : classList) {
			if (Chat.class.isAssignableFrom(s)){
				try {
					Chat command = (Chat) s.getConstructor().newInstance();
					chat_commands.add(command);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Couldn't initiate command " + s.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
				}
			}
		}

		TurokMod.turok_log.info("Commands initialised");
	}

	public void callCommand(String command) {
		String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		
		String label = parts[0].substring(1);
		String[] args = removeElement(parts, 0);
		
		for (int i = 0; i < args.length; i++){
			if (args[i]==null) continue;
			args[i] = strip(args[i], "\"");
		}
		
		for (Chat c : chat_commands) {
			if (c.getLabel().equalsIgnoreCase(label)) {
				c.getMessage(parts);
				return;
			}
		}
		
		Chat.sendChatMessage("Unknown command, try use some like this: " + "turok_commands_list");
	}
	
	public static String[] removeElement(String[] input, int indexToDelete) {
	    List result = new LinkedList();

	    for (int i = 0; i < input.length; i++){
	    	if (i != indexToDelete) result.add(input[i]);
	    }

	    return (String[]) result.toArray(input);
	}
	
	private static String strip(String str, String key){
		if (str.startsWith(key) && str.endsWith(key)) return str.substring(key.length(), str.length()-key.length());
		return str;
	}
	
	public Chat getCommandByLabel(String commandLabel){
		for (Chat c : chat_commands){
			if (c.getLabel().equals(commandLabel)) return c;
		}
		return null;
	}
	
	public ArrayList<Chat> getCommands() {
		return chat_commands;
	}
	
}
