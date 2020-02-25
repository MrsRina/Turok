package com.oldturok.turok.module.modules.combat;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;

import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.Item;

// Rina.
@Module.Info(name = "FastCE", category = Module.Category.TUROK_COMBAT)
public class FastCE extends Module {
	private Setting<Boolean> crystal = register(Settings.b("Crystal", true));
	private Setting<Boolean> expbottle = register(Settings.b("Exp Bottles", true));

	@Override
	public void onUpdate() {
		Item itemMainHand = mc.player.getHeldItemMainhand().getItem();
		Item itemONotMainHand = mc.player.getHeldItemOffhand().getItem();

		boolean expInMainHand = itemMainHand instanceof ItemExpBottle;
		boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;

		boolean crystalInMainHand = itemMainHand instanceof ItemEndCrystal;
		boolean crystalNotInMainHand = itemONotMainHand instanceof ItemEndCrystal;

		if (crystal.getValue()) {
			if (crystalInMainHand | crystalNotInMainHand) {
				mc.rightClickDelayTimer = 0;
			}
		}

		if (expbottle.getValue()) {
			if (expInMainHand | expNotInMainHand) {
				mc.rightClickDelayTimer = 0;
			}
		}
	}
}