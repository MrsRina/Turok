package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.Item;

// Rina.
@Module.Info(name = "FastBP", category = Module.Category.TUROK_COMBAT)
public class FastBP extends Module {
	private Setting<Boolean> break_ = register(Settings.b("Break", true));
	private Setting<Boolean> place = register(Settings.b("Place", true));

	@Override
	public void onUpdate() {
		Item itemMainHand = mc.player.getHeldItemMainhand().getItem();
		Item itemONotMainHand = mc.player.getHeldItemOffhand().getItem();

		boolean expInMainHand = itemMainHand instanceof ItemExpBottle;
		boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;

		boolean crystalInMainHand = itemMainHand instanceof ItemEndCrystal;
		boolean crystalNotInMainHand = itemONotMainHand instanceof ItemEndCrystal;

		if (place.getValue()) {
			if (crystalInMainHand) {
				mc.rightClickDelayTimer = 0;
			} else {
				mc.rightClickDelayTimer = 1;
			}

			if (expInMainHand) {
				mc.rightClickDelayTimer = 1;
			} else {
				mc.rightClickDelayTimer = 0;
			}
		}

		if (break_.getValue()) {
			mc.playerController.blockHitDelay = 0;
		}
	}
}