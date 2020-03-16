package com.oldturok.turok;

import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokMod;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.ITextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Rina.
public class TurokChat {
	public String name;
	public String description;

	public static boolean get_message(String[] msg) {
		return false;
	}

	public static void send_msg(String message) {
		Wrapper.getPlayer().sendMessage(new ChatMessage(TurokMod.TUROK_MOD_NAME + " - " + " " + message));
	}

	public static class ChatMessage extends TextComponentBase {
		String text;
		
		public ChatMessage(String text) {
			
			Pattern p       = Pattern.compile("&[0123456789abcdefrlosmk]");
			Matcher m       = p.matcher(text);
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
}
