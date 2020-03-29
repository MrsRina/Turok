package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

// Rina.
@Module.Info(name = "AutoArmor", description = "Auto armor slot.", category = Module.Category.TUROK_COMBAT)
public class AutoArmor extends Module {
	public int[] best_armor_slots  = new int[4];
	public int[] best_armor_values = new int[4];

	@Override
	public void onUpdate() {
		if (mc.currentScreen instanceof GuiContainer) {
			return;
		}

		if (!(mc.currentScreen instanceof InventoryEffectRenderer)) {
			return;
		}

		verify_actual_armor();
		verify_best_armor__();
		verify_if_can_equip();
	}

	public void verify_actual_armor() {
		for (int armor = 0; armor < 4; armor++) {
			ItemStack actual_armor = mc.player.inventory.armorItemInSlot(armor);

			if (actual_armor != null && actual_armor.getItem() instanceof ItemArmor) {
				best_armor_values[armor] = ((ItemArmor) actual_armor.getItem()).damageReduceAmount;
			}

			best_armor_slots[armor] = -1;
		}
	}

	public void verify_best_armor__() {
		for (int slot = 0; slot < 36; slot++) {
			ItemStack find = mc.player.inventory.getStackInSlot(slot);

			if (find.getCount() > 1) {
				continue;
			}

			if (find == null || !(find.getItem() instanceof ItemArmor)) {
				continue;
			}

			ItemArmor find_armor = (ItemArmor) find.getItem();

			int armor_type = find_armor.armorType.ordinal() - 2;

			if (armor_type == 2 && mc.player.inventory.armorItemInSlot(armor_type).getItem().equals(Items.ELYTRA)) {
				continue;
			}

			int armor_reduce = find_armor.damageReduceAmount;

			if (armor_reduce > best_armor_values[armor_type]) {
				best_armor_slots[armor_type]  = slot;
				best_armor_values[armor_type] = armor_reduce;
			}
		}
	}

	public void verify_if_can_equip() {
		for (int armor_type = 0; armor_type < 4; armor_type++) {
			int slot = best_armor_slots[armor_type];

			if (slot == - 1) {
				continue;
			}


			ItemStack actual_armor = mc.player.inventory.armorItemInSlot(armor_type);

			if (actual_armor != null || actual_armor != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != - 1) {
				if (slot < 9) {
					slot += 36;
				}

				mc.playerController.windowClick(0, 8 - armor_type, 0, ClickType.QUICK_MOVE, mc.player);
				mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);

				break;
			} 
		}
	}
}