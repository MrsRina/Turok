package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

// Rina.
@Module.Info(name = "AutoArmor", description = "Auto armor slot.", category = Module.Category.TUROK_COMBAT)
public class AutoArmor extends Module {
	@Override
	public void onUpdate() {
		if (mc.currentScreen instanceof GuiContainer) return;

		find_best_armor(0);
		find_best_armor(1);
		find_best_armor(2);
		find_best_armor(3);
	}

	public void find_best_armor(int armor) {
		for (int slot = 0; slot < 36; slot++) {
			ItemStack items = mc.player.inventory.getStackInSlot(slot);

			if (items.getCount() > 1) {
				continue;
			}

			if (items == null) {
				continue;
			}

			if (!(item instanceof ItemArmor)) {
				continue;
			}

			ItemArmor armor_r = (ItemArmor) items.getItem();

			int armor_type = armor_r.armorType.ordinal() - 2;

			if (armor_type == 2 && mc.player.inventory.armorItemInSlot(armor_type).getItem().equals(Items.ELYTRA)) {
				continue;
			}

			ItemStack actual_armor = mc.player.inventory.armorItemInSlot(armor);

			if (actual_armor == )

			if (armor_type == armor) {
				if (actual_armor.damageReduceAmount > armor_r.damageReduceAmount) {

				}
			}
		}
	}
}