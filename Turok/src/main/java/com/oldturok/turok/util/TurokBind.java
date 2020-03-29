package com.oldturok.turok.util;

import org.lwjgl.input.Keyboard;

public class TurokBind {
	int bind_key;

	public TurokBind(int key) {
		this.bind_key = key;
	}

	public int get_key() {
		return this.bind_key;
	}

	public boolean is_empty() {
		return (this.bind_key < 0);
	}

	public void set_key(int key) {
		this.bind_key = key;
	}

	public boolean keydown(int key) {
		return (!is_empty() && key == get_key());
	}

	public String capitalise(String str) {
		if (str.isEmpty()) {
			return "";
		} else {
			return Character.toUpperCase(str.charAt(0)) + (str.length() != 1 ? str.substring(1).toLowerCase() : "");
		}
	}

	public static TurokBind none() {
		return new TurokBind(- 1);
	}

	@Override
	public String toString() {
		return is_empty() ? "None" : (this.bind_key < 0 ? "None" : capitalise(Keyboard.getKeyName(this.bind_key)));
	}
}