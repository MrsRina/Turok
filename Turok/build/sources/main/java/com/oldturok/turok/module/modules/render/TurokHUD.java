package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

import com.oldturok.turok.gui.turok.TurokGUI;

// TurokHUD = Rina.
// ArmorHUD = Raindrop.
@Module.Info(name = "TurokHUD", category = Module.Category.TUROK_RENDER)
public class TurokHUD extends Module {
	private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

	private Setting<Boolean> array_hud = register(Settings.b("ArraysHUD", true));
	private Setting<Boolean> coords_hd = register(Settings.b("CoordsHUD", true));
	private Setting<Boolean> armor_hud = register(Settings.b("ArmorHUD", true));
	private Setting<Boolean> count_hud = register(Settings.b("CountHUD", true));
	private Setting<Boolean> users_hud = register(Settings.b("UsersHUD", true));

	private boolean show_array;
	private boolean show_coords;
	private boolean show_count;
	private boolean show_users;

	@Override
	public void onDisable() {
		show_array  = false;
		show_coords = true;
		show_count  = true;
		show_users  = true;
	}

	@Override
	public void onUpdate() {
		if (array_hud.getValue()) {
			show_array = true;
		} else {
			show_array = false;
		}

		if (coords_hd.getValue()) {
			show_coords = true;
		} else {
			show_coords = false;
		}

		if (count_hud.getValue()) {
			show_count = true;
		} else {
			show_count = false;
		}

		if (users_hud.getValue()) {
			show_users = true;
		} else {
			show_users = false;
		}
	}

	@Override
	public void onRender() {
		if (show_users) {
			TurokGUI.frames.add(TurokGUI.frame_users);
		} else {
			try {
				TurokGUI.frames.remove(TurokGUI.frame_users);
			} catch (Exception exc) {
				return;
			}
		}

		if (show_array) {
			TurokGUI.frames.add(TurokGUI.frame_array);
		} else {
			try {
				TurokGUI.frames.remove(TurokGUI.frame_array);
			} catch (Exception exc) {
				return;
			}
		}

		if (show_coords) {
			TurokGUI.frames.add(TurokGUI.frame_coords);
		} else {
			try {
				TurokGUI.frames.remove(TurokGUI.frame_coords);
			} catch (Exception exc) {
				return;
			}
		}

		if (show_count) {
			TurokGUI.frames.add(TurokGUI.frame_coords);
		} else {
			try {
				TurokGUI.frames.remove(TurokGUI.frame_counts);
			} catch (Exception exc) {
				return;
			}
		}

		for (Frame frame1 : TurokGUI.frames) {
			frame1.setY(TurokGUI.x);
			frame1.setY(TurokGUI.y);

			TurokGUI.nexty = Math.max(TurokGUI.y + frame1.getHeight() + 10, TurokGUI.nexty);
			TurokGUI.x += (frame1.getWidth() + 10);
			if (TurokGUI.x * TurokGUI.get_scale() > mc.displayWidth / 1.2f) {
				TurokGUI.y = TurokGUI.nexty;
				TurokGUI.nexty = TurokGUI.y;
				TurokGUI.x = 10;
			}
		}

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