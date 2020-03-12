package com.oldturok.turok.chatcmd;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokMod;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.ITextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Chat {
	protected String label;
	protected String syntax;
	protected String description;

	public static Setting<String> chatPrefix = Settings.s("chatPrefix", "-");

	public Chat(String label) {
		this.label = label;
		this.description = "Descriptionless";
	}

	public static void sendChatMessage(String message) {
		sendRawChatMessage("&4&a" + TurokMod.MODNAME + "&4&r  - " + message);
	}

	public static void sendStringChatMessage(String[] messages) {
		sendChatMessage("");
		for (String s : messages) sendRawChatMessage(s);
    }

	public static void sendRawChatMessage(String message){
		Wrapper.getPlayer().sendMessage(new ChatMessage(message));
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static String getChatPrefix() {
		return chatPrefix.getValue();
	}
	
	public String getLabel() {
		return label;
	}
	
	public abstract void getMessage(String[] args);

    public static class ChatMessage extends TextComponentBase {
		String text;
		
		public ChatMessage(String text) {
			
			Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
			Matcher m = p.matcher(text);
			StringBuffer sb = new StringBuffer();

			while (m.find()) {
			    String replacement = "\u00A7" + m.group().substring(1);
			    m.appendReplacement(sb, replacement);
			}

			m.appendTail(sb);

			this.text = sb.toString();
		}
		
		public String getUnformattedComponentText() {
			return text;
		}

		@Override
		public ITextComponent createCopy() {
			return new ChatMessage(text);
		}

	}

	public static char SECTIONSIGN() {
    	return '\u00A7';
	}
}