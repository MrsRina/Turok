package com.oldturok.turok;

import com.oldturok.turok.TurokMod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;

// Rina.
public class TurokChat {
	public String name;
	public String description;

	public Minecraft mc = Minecraft.getMinecraft();

	public TurokChat(String name, String description) {
		this.name        = name;
		this.description = description;
	}

	public boolean get_message(String[] message) {
		return false;
	}

	public String get_description() {
		return this.description;
	}

	public String get_name() {
		return this.name;
	}
}
