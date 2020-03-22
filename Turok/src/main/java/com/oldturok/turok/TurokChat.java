package com.oldturok.turok;

import com.oldturok.turok.TurokMod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Rina.
public class TurokChat {
	public String name;
	public String description;
	public String prefix;

	public TurokChat(String name, String description) {
		this.name        = name;
		this.description = description;
	}

	public void set_prefix(String prefix) {
		this.prefix = prefix;
	}

	public String get_prefix() {
		return this.prefix;
	}

	public boolean Get_Message(String[] message) {
		return false;
	}
}
