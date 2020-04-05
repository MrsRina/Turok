package com.oldturok.turok.util;

import net.minecraft.enchantment.Enchantment;

public class TurokEnchantManager{
	private Enchantment enchant;
	private String name;

	public TurokEnchantManager(Enchantment enchant, String name) {
		this.enchant = enchant;
		this.name    = name;
	}

	public Enchantment get_enchant() {
		return this.enchant;
	}

	public String get_name() {
		return this.name;
	}
}