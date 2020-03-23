package com.oldturok.turok;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Rina.
public abstract class TurokChat {
	public String name;
	public String description;

	public static Setting<String> prefix = Settings.s("commandPrefix", ".");

	public TurokChat(String name, String description) {
		this.name        = name;
		this.description = description;
	}

	public String get_decrption() {
		return description;
	}

	public String get_name() {
		return name;
	}

	public String get_prefix() {
		return prefix.getValue();
	}

	public void set_prefix(String new_prefix) {
		prefix.setValue(new_prefix);
	}

	public abstract void command(String[] message);
}
