package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import com.oldturok.turok.gui.turok.TurokGUI;

// TurokHUD = Rina.
// ArmorHUD = Rina.
@Module.Info(name = "TurokHUD", category = Module.Category.TUROK_RENDER)
public class TurokHUD extends Module {
	private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

	private Setting<Boolean> armor_hud = register(Settings.b("Show Armor HUD", true));

	@Override
	public void onRender() {
		if (armor_hud.getValue()) {
			GlStateManager.enableTexture2D();

			ScaledResolution resolution = new ScaledResolution(mc);
			int i = resolution.getScaledWidth() / 2;
			int iteration = 0;
			int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);

			for (ItemStack is : mc.player.inventory.armorInventory) {
				iteration++;
				if (is.isEmpty()) continue;
				int x = i - 90 + (9 - iteration) * 20 + 2;
				GlStateManager.enableDepth();

				itemRender.zLevel = 200F;
				itemRender.renderItemAndEffectIntoGUI(is, x, y);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
				itemRender.zLevel = 0F;

				GlStateManager.enableTexture2D();
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();

				String s = is.getCount() > 1 ? is.getCount() + "" : "";
				mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, 0xffffff);
			}

			GlStateManager.enableDepth();
			GlStateManager.disableLighting();
		}
	}
}