package com.oldturok.turok.util;

import java.awt.Color;

public class TurokColor extends Color {
	public TurokColor(int r, int g, int b) {
		super(r, g, b);
	}

	public int hex() {
		return convert_to_hex(getRed(), getGreen(), getBlue());
	}

	public static int convert_to_hex(int r, int g, int b) {
		return (r << 16 | g << 8 | b);
	}
}