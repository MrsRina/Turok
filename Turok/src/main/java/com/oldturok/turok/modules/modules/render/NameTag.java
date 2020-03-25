package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.gui.turok.widgets.WidgetModuleFrame;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.util.TurokColor;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.Friends;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;

import static org.lwjgl.opengl.GL11.*;

import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

@Module.Info(name = "NameTag", description = "Show which item player using.", category = Module.Category.TUROK_HIDDEN)
public class NameTag extends Module {
	private Setting<Integer> range = register(Settings.integerBuilder("Range").withMinimum(0).withMaximum(50).withValue(10));
	private Setting<Float> scale   = register(Settings.floatBuilder("Scale").withMinimum(0.5f).withMaximum(50f).withValue(3f).build());

	FontRenderer render_Font = mc.fontRenderer;
	RenderItem render_item   = mc.getRenderItem();

	@Override
	public void onWorldRender(RenderEvent event) {
		if (mc.getRenderManager().options == null) {
			return;
		}

		if (mc.world != null) {
			GlStateManager.enableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();

			mc.world.loadedEntityList.stream().filter(EntityUtil::isPlayer)
											  .filter(player -> !EntityUtil.isFakeLocalPlayer(player))
											  .filter(player -> (player instanceof EntityPlayer))
											  .filter(player -> mc.player.getDistance(player) < range.getValue())
											  .sorted(Comparator.comparing(player -> - mc.player.getDistance(player)))
											  .forEach(this::name_tag);

			GlStateManager.disableTexture2D();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}
	}

	public void name_tag(Entity player_entity) {
		String tag_name = (player_entity.getName() +  " ");
		String tag_life = (Integer.toString((int) ((EntityLivingBase) player_entity).getHealth()));
		
		int tag_name_width = render_Font.getStringWidth(tag_name) / 2;
		int tag_life_width = tag_name_width + 5;

		GlStateManager.pushMatrix();

		Vec3d player_data_interp = EntityUtil.getInterpolatedRenderPos(player_entity, mc.getRenderPartialTicks());
		
		float player_extra_y  = player_entity.height + 0.5F - (player_entity.isSneaking() ? 0.25f : 0.0f);
		float player_view_x   = mc.getRenderManager().playerViewX;
		float player_view_y   = mc.getRenderManager().playerViewY;
		float player_distance = mc.player.getDistance(player_entity);
		float player_math_cal = (player_distance / 8f) * (float) (Math.pow(1.2589254f, scale.getValue()));

		double x = player_data_interp.x;
		double y = player_data_interp.y + player_extra_y;
		double z = player_data_interp.z;

		boolean is_front = (mc.getRenderManager().options.thirdPersonView == 2);

		GlStateManager.translate(x, y, z);

		GlStateManager.rotate(- player_view_y, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate((float) (is_front ? -1 : 1) * player_view_x, 1.0f, 0.0f, 0.0f);

		GlStateManager.scale(player_math_cal, player_math_cal, player_math_cal);
		GlStateManager.scale(- 0.025f, - 0.025f, 0.025f);

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableTexture2D();

		Tessellator draw_tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder  = draw_tessellator.getBuffer();

		glTranslatef(0, - 20, 0);

		draw(GL_QUADS, draw_tessellator, bufferbuilder, tag_life_width, 0.0f, 0.5f);
		draw(GL_LINE_LOOP, draw_tessellator, bufferbuilder, tag_life_width, 0.1f, 0.1f);

		GlStateManager.enableTexture2D();
		GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);

		TurokColor color_life = new TurokColor(WidgetModuleFrame.color_module_r, 0, 0);
		
		render_Font.drawString(tag_name, - tag_name_width, 10, Friends.isFriend(player_entity.getName()) ? 0x11ee11 : 0xffffff);
		render_Font.drawString(tag_life, tag_life_width, 10, color_life.hex());

		GlStateManager.enableTexture2D();

		draw_items((EntityPlayer) player_entity, 0, - (render_Font.FONT_HEIGHT + 1) - 20);

		GlStateManager.glNormal3f(0.0f, 0.0f, 0.0f);

		glTranslatef(0, 20, 0);

		GlStateManager.scale(- 40, - 40, 40);

		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}

	public void draw(int type, Tessellator tessellator, BufferBuilder buffer, int width, float color, float alpha) {
		buffer.begin(type, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(- width - 1, 8, 0.0d).color(color, color, color, alpha).endVertex();
		buffer.pos(- width - 1, 19, 0.0d).color(color, color, color, alpha).endVertex();
		buffer.pos(width + 1, 19, 0.0d).color(color, color, color, alpha).endVertex();
		buffer.pos(width + 1, 8, 0.0d).color(color, color, color, alpha).endVertex();

		tessellator.draw();
	}

	public void draw_armor(ItemStack stack, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.pushMatrix();

		GlStateManager.translate(x - 3, y + 8, 0.0f);
		GlStateManager.scale(0.3f, 0.3f, 0.3f);
		GlStateManager.popMatrix();

		RenderHelper.enableGUIStandardItemLighting();

		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();

		render_item.zLevel = - 100f;
		render_item.renderItemIntoGUI(stack, x, y);
		render_item.renderItemOverlayIntoGUI(render_Font, stack, x, y, "");
		render_item.zLevel = 0.0f;

		GlStateManager.enableDepth();

		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
	}

	public void draw_items(EntityPlayer player, int x, int y) {
		ItemStack player_main_hand = player.getHeldItemMainhand();
		ItemStack player_off_hand  = player.getHeldItemOffhand();
		
		InventoryPlayer get_inventory = player.inventory;

		ItemStack player_helmet     = get_inventory.armorItemInSlot(3);
		ItemStack player_chestplace = get_inventory.armorItemInSlot(2);
		ItemStack player_leggins    = get_inventory.armorItemInSlot(1);
		ItemStack player_boots      = get_inventory.armorItemInSlot(0);

		ItemStack[] equipament = null;

		if (player_main_hand != null && player_off_hand != null) {
			equipament = new ItemStack[] {
				player_main_hand,
				player_off_hand,
				player_helmet,
				player_chestplace,
				player_leggins,
				player_boots
			};
		} else if (player_main_hand == null && player_off_hand != null) {
			equipament = new ItemStack[] {
				player_off_hand,
				player_helmet,
				player_chestplace,
				player_leggins,
				player_boots
			};
		} else if (player_main_hand != null && player_off_hand == null) {
			equipament = new ItemStack[] {
				player_main_hand,
				player_helmet,
				player_chestplace,
				player_leggins,
				player_boots
			};
		} else if ((player_main_hand == null && player_off_hand == null)) {
			equipament = new ItemStack[] {
				player_helmet,
				player_chestplace,
				player_leggins,
				player_boots
			};
		}

		List<ItemStack> stacks = new ArrayList();

		ItemStack[] array;

		int len = (array = equipament).length;

		for (int j_int = 0; j_int < len; j_int++) {
			ItemStack int_ = array[j_int];

			if ((int_ != null) && (int_.getItem() != null)) {
				stacks.add(int_);				
			}
		}

		int width = 16 * stacks.size() / 2;

		x -= width;

		GlStateManager.disableDepth();

		for (ItemStack stack : stacks) {
			draw_armor(stack, x, y);

			x += 16;
		}

		GlStateManager.enableDepth();
	}
}