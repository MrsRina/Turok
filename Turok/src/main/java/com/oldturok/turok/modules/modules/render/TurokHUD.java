package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.gui.turok.widgets.WidgetActiveModules;
import com.oldturok.turok.gui.turok.widgets.WidgetModuleFrame;
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

// Made by Rina, ArmorHUD by default owner.
@Module.Info(name = "TurokHUD", description = "Turok HUD, ask for Rina.", category = Module.Category.TUROK_RENDER)
public class TurokHUD extends Module {
	private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

	private Setting<Boolean> effect_pinnable = register(Settings.b("Effect", true));
	private Setting<Boolean> wattermark_hud  = register(Settings.b("Wattermark", true));
	private Setting<Boolean> users_hud       = register(Settings.b("Users", true));
	private Setting<Boolean> coords_hud      = register(Settings.b("Coords", true));
	private Setting<Boolean> totem_hud       = register(Settings.b("Totems", true));
	private Setting<Boolean> gapple_hud      = register(Settings.b("Gapples", true));
	private Setting<Boolean> crystal_hud     = register(Settings.b("Crystals", true));
	private Setting<Boolean> exp_hud         = register(Settings.b("BottlesXp", true));
	private Setting<Boolean> armor_hud       = register(Settings.b("ArmorHUD", true));
	
	// Array.
	public Setting<Boolean> array_rgb = register(Settings.b("ArrayRGB", true));
	public Setting<Integer> array_r   = register(Settings.integerBuilder("ArrayRed").withMinimum(1).withMaximum(255).withValue(255));
	public Setting<Integer> array_g   = register(Settings.integerBuilder("ArrayGreen").withMinimum(1).withMaximum(255).withValue(255));
	public Setting<Integer> array_b   = register(Settings.integerBuilder("ArrayBlue").withMinimum(1).withMaximum(255).withValue(255));

	boolean wattermark_enable;
	boolean users_enable;
	boolean coords_enable;
	boolean totem_enable;
	boolean gapple_enable;
	boolean crystal_enable;
	boolean exp_enable;

	int wattermark_tick;
	int users_tick;
	int coords_tick;
	int totem_tick;
	int gapple_tick;
	int crystal_tick;
	int exp_tick;

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

		if (effect_pinnable.getValue()) {
			WidgetModuleFrame.effect = true;
		} else {
			WidgetModuleFrame.effect = false;
		}

		if (wattermark_enable) {
			wattermark_tick += 1;

			if (wattermark_tick >= 5) {
				wattermark_tick = 6;
			} else {
				TurokGUI.frame_wattermark.setX(0);
				TurokGUI.frame_wattermark.setY(10);
			}
		} else {
			wattermark_tick = 0;

			TurokGUI.frame_wattermark.setX(12000);
			TurokGUI.frame_wattermark.setY(12000);
		}

		if (users_enable) {
			users_tick += 1;

			if (users_tick >= 5) {
				users_tick = 6;
			} else {
				TurokGUI.frame_users.setX(0);
				TurokGUI.frame_users.setY(50);
			}
		} else {
			users_tick = 0;

			TurokGUI.frame_users.setX(12000);
			TurokGUI.frame_users.setY(12000);
		}

		if (coords_enable) {
			coords_tick += 1;

			if (coords_tick >= 5) {
				coords_tick = 6;
			} else {
				TurokGUI.frame_coords.setX(0);
				TurokGUI.frame_coords.setY(100);
			}
		} else {
			coords_tick = 0;

			TurokGUI.frame_coords.setX(12000);
			TurokGUI.frame_coords.setY(12000);
		}

		if (totem_enable) {
			totem_tick += 1;

			if (totem_tick >= 5) {
				totem_tick = 6;
			} else {
				TurokGUI.frame_counts_totem.setX(0);
				TurokGUI.frame_counts_totem.setY(200);
			}
		} else {
			totem_tick = 0;

			TurokGUI.frame_counts_totem.setX(12000);
			TurokGUI.frame_counts_totem.setY(12000);
		}

		if (gapple_enable) {
			gapple_tick += 1;

			if (gapple_tick >= 5) {
				gapple_tick = 6;
			} else {
				TurokGUI.frame_counts_gapple.setX(0);
				TurokGUI.frame_counts_gapple.setY(300);
			}
		} else {
			gapple_tick = 0;

			TurokGUI.frame_counts_gapple.setX(12000);
			TurokGUI.frame_counts_gapple.setY(12000);
		}

		if (crystal_enable) {
			crystal_tick += 1;

			if (crystal_tick >= 5) {
				crystal_tick = 6;
			} else {
				TurokGUI.frame_counts_crystal.setX(0);
				TurokGUI.frame_counts_crystal.setY(400);
			}
		} else {
			crystal_tick = 0;

			TurokGUI.frame_counts_crystal.setX(12000);
			TurokGUI.frame_counts_crystal.setY(12000);
		}

		if (exp_enable) {
			exp_tick += 1;

			if (exp_tick >= 5) {
				exp_tick = 6;
			} else {
				TurokGUI.frame_counts_exp.setX(0);
				TurokGUI.frame_counts_exp.setY(500);
			}
		} else {
			exp_tick = 0;

			TurokGUI.frame_counts_exp.setX(12000);
			TurokGUI.frame_counts_exp.setY(12000);
		}

		compare();
		enable();
	}

	public void compare() {
		if (wattermark_hud.getValue()) {
			wattermark_enable = true;
		} else {
			wattermark_enable = false;
		}

		if (users_hud.getValue()) {
			users_enable = true;
		} else {
			users_enable = false;
		}

		if (coords_hud.getValue()) {
			coords_enable = true;
		} else {
			coords_enable = false;
		}

		if (totem_hud.getValue()) {
			totem_enable = true;
		} else {
			totem_enable = false;
		}

		if (gapple_hud.getValue()) {
			gapple_enable = true;
		} else {
			gapple_enable = false;
		}

		if (crystal_hud.getValue()) {
			crystal_enable = true;
		} else {
			crystal_enable = false;
		}

		if (exp_hud.getValue()) {
			exp_enable = true;
		} else {
			exp_enable = false;
		}
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

				if (is.isEmpty()) {
					continue;
				}

				int x = i - 90 + (9 - iteration) * 20 + 2;

				GlStateManager.enableDepth();

				itemRender.zLevel = 200f;
				itemRender.renderItemAndEffectIntoGUI(is, x, y);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
				itemRender.zLevel = 0f;

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