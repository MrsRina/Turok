package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.command.Command;
import com.oldturok.turok.module.Module;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

// Rina.
@Module.Info(name = "AutoReplace", category = Module.Category.TUROK_COMBAT)
public class AutoReplace extends Module {
	private Setting<Boolean> crystal = register(Settings.b("Crystal", true));

	boolean item = false;
	boolean move = false;

	@Override
	public void onUpdate() {
		if (mc.currentScreen instanceof GuiContainer) return;
	}
}