package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.gui.turok.widgets.WidgetActiveModules;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.awt.*;

// TurokHUD = Rina.
// ArmorHUD = Rina.
// Others   = Rina.
@Module.Info(name = "TurokHUD", description = "Turok HUD, ask for Rina.", category = Module.Category.TUROK_RENDER)
public class TurokHUD extends Module {
	private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

	private Setting<Boolean> armor_hud  = register(Settings.b("Show Armor HUD", true));
	private Setting<Boolean> coords_hud = register(Settings.b("Show Coordinates HUD", false));
	private Setting<Boolean> watter_hud = register(Settings.b("Show WatterMark HUD", false));
	private Setting<Boolean> count_hud  = register(Settings.b("Show Count Info HUD", false));
	
	// Array.
	public Setting<Boolean> array_rgb = register(Settings.b("Array RGB", true));
	public Setting<Integer> array_r   = register(Settings.integerBuilder("Array Red").withMinimum(1).withMaximum(255).withValue(255));
	public Setting<Integer> array_g   = register(Settings.integerBuilder("Array Green").withMinimum(1).withMaximum(255).withValue(255));
	public Setting<Integer> array_b   = register(Settings.integerBuilder("Array Blue").withMinimum(1).withMaximum(255).withValue(255));

	@Override
	public void onDisable() {
		enable();
	}

	@Override
	public void onUpdate() {
		float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
		int color_rgb      = Color.HSBtoRGB(tick_color[0], 1, 1);

		tick_color[0] += 0.1f;

		if (array_rgb.getValue()) {
			WidgetActiveModules.r = ((color_rgb >> 16) & 0xFF);
			WidgetActiveModules.g = ((color_rgb >> 8) & 0xFF);
			WidgetActiveModules.b = (color_rgb & 0xFF);
		} else {
			WidgetActiveModules.r = array_r.getValue();
			WidgetActiveModules.g = array_g.getValue();
			WidgetActiveModules.b = array_b.getValue();
		}

		if (coords_hud.getValue()) {
			TurokGUI.state_coord = true;
		} else {
			TurokGUI.state_coord = false;
		}

		enable();
	}

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