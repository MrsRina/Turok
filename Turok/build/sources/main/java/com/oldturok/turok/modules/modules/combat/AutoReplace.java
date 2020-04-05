package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.Pair;
import com.oldturok.turok.TurokChat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

// Rina.
// Thanks who made it, i really dont know how i do.
@Module.Info(name = "AutoReplace", description = "Auto replace crystals stacks or any item.", category = Module.Category.TUROK_COMBAT)
public class AutoReplace extends Module {
	private Setting<Integer> stack_value = register(Settings.integerBuilder("Replace in").withMinimum(1).withValue(16).withMaximum(63).build());

	int tick = 0;

	@Override
	public void onUpdate() {
		if (mc.player == null) return;

		if (mc.currentScreen instanceof GuiContainer) return;

		if (tick < 2) {
			tick++;

			return;
		} else {
			tick = 0;
		}

		Pair<Integer, Integer> slots = slots_replace();

		if (slots == null) {
			return;
		}

		int inventory_slots_replaced  = slots.getKey();
		int inventory_hotbar_replaced = slots.getValue();

		mc.playerController.windowClick(0, inventory_slots_replaced, 0, ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(0, inventory_hotbar_replaced, 0, ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(0, inventory_slots_replaced, 0, ClickType.PICKUP, mc.player);
	}

	private Pair<Integer, Integer> slots_replace() {
		Pair<Integer, Integer> pair = null;

		for (Map.Entry<Integer, ItemStack> slot : hotbar().entrySet()) {
			ItemStack stack = slot.getValue();

			if (stack.isEmpty || stack.getItem() == Items.AIR) continue;

			if (!stack.isStackable()) continue;

			if (stack.stackSize >= stack.getMaxStackSize()) continue;

			if (stack.stackSize > stack_value.getValue()) continue;

			int inventory_slot = compative_replace(stack);

			if (inventory_slot == -1) continue;

			pair = new Pair<>(inventory_slot, slot.getKey());
		}

		return pair;
	}

	private int compative_replace(ItemStack stack) {
		int slot      = -1;
		int bit_stack = 999;

		for (Map.Entry<Integer, ItemStack> entry : inventory().entrySet()) {
			ItemStack stack_replace = entry.getValue();

			if (stack_replace.isEmpty || stack_replace.getItem() == Items.AIR) continue;
			if (!is_compative(stack, stack_replace)) continue;

			int current_stack = mc.player.inventoryContainer.getInventory().get(entry.getKey()).stackSize;

			if (bit_stack > current_stack) {
				bit_stack = current_stack;

				slot = entry.getKey();
			}
		}

		return slot;
	}

	private boolean is_compative(ItemStack stack, ItemStack stack_) {
		if (!stack.getItem().equals(stack_.getItem())) return false;

		if ((stack.getItem() instanceof ItemBlock) && (stack_.getItem() instanceof ItemBlock)) {
			Block block_1 = ((ItemBlock) stack_.getItem()).getBlock();
			Block block_2 = ((ItemBlock) stack_.getItem()).getBlock();

			if (!block_1.material.equals(block_2.material)) return false;
		}

		if (!stack.getDisplayName().equals(stack_.getDisplayName())) return false;

		if (stack.getItemDamage() != stack_.getItemDamage()) return false;

		return true;
	}

	private static Map<Integer, ItemStack> inventory() {
		return inventory_slots(9, 35);
	}

	private static Map<Integer, ItemStack> hotbar() {
		return inventory_slots(36, 44);
	}

	private static Map<Integer, ItemStack> inventory_slots(int current, int last) {
		Map<Integer, ItemStack> full_inventory = new HashMap<>();

		while (current <= last) {
			full_inventory.put(current, mc.player.inventoryContainer.getInventory().get(current));

			current++;
		}

		return full_inventory;
	}

	@Override
	public String getHudInfo() {
		return String.valueOf(stack_value.getValue());
	}
}