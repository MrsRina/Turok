package com.oldturok.turok;

import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokMod;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.Minecraft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Rina.
public class TurokMessage {
	public static Minecraft mc = Minecraft.getMinecraft();

	public static void user_send_msg(String msg) {
		mc.player.connection.sendPacket(new CPacketChatMessage(msg));
	}

	public static void send_msg(String msg) {
		mc.player.sendMessage(new ChatMessage(msg));
	}

	public static void send_client_msg(String message) {
		send_msg(TurokMod.TUROK_MOD_NAME + " - " + " " + message);
	}

	public static void send_error_msg(String message) {
		send_msg(TurokMod.TUROK_MOD_NAME + ": " + ChatFormatting.RED + message);
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