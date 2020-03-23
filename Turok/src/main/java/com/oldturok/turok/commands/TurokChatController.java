package com.oldturok.turok.commands;

import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiChat;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

public class TurokChatController extends GuiChat {
	public String startString;
	public String current_line;
	
	public int cursor;

	public TurokChatController(String startString, String historybuffer, int sentHistoryCursor) {
		super(startString);
		
		this.startString = startString;

		if (!startString.equals(TurokChat.get_prefix())) {
			calculateCommand(startString.substring(TurokChat.get_prefix().length()));
		}

		this.historyBuffer = historybuffer;
		cursor             = sentHistoryCursor;
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		this.sentHistoryCursor = cursor;

		super.keyTyped(typedChar, keyCode);

		String chat_line = this.inputField.getText();

		if (!chat_line.startsWith(TurokChat.get_prefix())) {
			GuiChat gui_chat = new GuiChat(chat_line) {
				int cursor = TurokChatController.this.cursor;

				@Override
				protected void keyTyped(char typedChar, int keyCode) throws IOException {
					this.sentHistoryCursor = cursor;
					super.keyTyped(typedChar, keyCode);
					cursor = this.sentHistoryCursor;
				}
			};

			gui_chat.historyBuffer = this.historyBuffer;
			mc.displayGuiScreen(gui_chat);

			return;
		}

		if (chat_line.equals(TurokChat.get_prefix())) {
			current_line = "";
			return;
		}

		calculateCommand(chat_line.substring(TurokChat.get_prefix().length()));
	}

	protected void calculateCommand(String line) {
		String[] message = line.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

		HashMap<String, TurokChat> options = new HashMap<String, TurokChat>();

		if (message.length == 0) {
			return;
		}

		for (TurokChat command : TurokMod.get_instance().get_chat_manager().get_commands()) {
			if ((command.get_name().startsWith(message[0]) && !line.endsWith(" ")) || command.get_name().equals(message[0])) {
				options.put(command.get_name(), command);
			}
		}

		if (options.isEmpty()) {
			current_line = "";

			return;
		}

		TreeMap<String, TurokChat> map = new TreeMap<String, TurokChat> (options);

		TurokChat alpha_command = map.firstEntry().getValue();

		current_line = alpha_command.get_name().substring(message[0].length());

		if (!line.endsWith(" ")) {
			current_line += " ";
		}
	}
}