package com.oldturok.turok.chatcmd;

import com.oldturok.turok.TurokMod;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatManager {
	private ArrayList<ChatManager> commands;
	
	public ChatManager() {
		TurokMod.log.info("Commands initialised");
	}

	public void callCommand(String command) {
		String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		
		String label = parts[0].substring(1);
		String[] args = removeElement(parts, 0);
		
		for (int i = 0; i < args.length; i++){
			if (args[i]==null) continue;
			args[i] = strip(args[i], "\"");
		}
		
		for (ChatManager c : commands){
			if (c.getLabel().equalsIgnoreCase(label)){
				c.call(parts);
				return;
			}
		}
		
		ChatManager.sendChatMessage("Unknown command. try 'commands' for a list of commands.");
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
	
	public ChatManager getCommandByLabel(String commandLabel){
		for (ChatManager c : commands){
			if (c.getLabel().equals(commandLabel)) return c;
		}
		return null;
	}
	
	public ArrayList<ChatManager> getCommands() {
		return commands;
	}
	
}
