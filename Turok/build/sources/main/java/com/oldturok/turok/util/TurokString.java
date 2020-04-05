package com.oldturok.turok.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TurokString {
	public static String[] remove_element(String[] input, int indexToDelete) {
		List result = new LinkedList();

		for (int int_ = 0; int_ < input.length; int_++) {
			if (int_ != indexToDelete) {
				result.add(input[int_]);
			}
		}

		return (String[]) result.toArray(input);
	}

	public static String strip(String str, String key) {
		if (str.startsWith(key) && str.endsWith(key)) {
			return str.substring(key.length(), str.length() - key.length());
		}

		return str;
	}
}