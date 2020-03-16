package com.oldturok.turok.chatcmd;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.Minecraft;

public class TurokCommand {
	public String name;
	public String description;

	protected Minecraft mc = Minecraft.getMinecraft();

	public TurokCommand(String name, String description) {
		this.name        = name;
		this.description = description;
	}

	public boolean GetMessage(Stringp[] msg) {
		return false;
	}
}