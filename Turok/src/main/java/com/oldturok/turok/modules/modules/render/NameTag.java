package com.oldturok.turok.module.modules.player;

import com.oldturok.turok.util.TurokEnchantManager;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.Friends;
import com.oldturok.turok.util.TurokGL;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.*;
import net.minecraft.init.Enchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

// Rina.
@Module.Info(name = "NameTag", description = "A name tag unique for Turok.", category = Module.Category.TUROK_PLAYER)
public class NameTag extends Module {
	private Setting<Integer> range = register(Settings.integerBuilder("Range").withMinimum(1).withValue(10).withMaximum(30));
	private Setting<Float> scale_  = register(Settings.floatBuilder("Scale").withMinimum(1.0f).withValue(2.0f).withMaximum(30.0f));

	private Setting<Boolean> health = register(Settings.b("Show Health", true));
	private Setting<Boolean> echant = register(Settings.b("Show Enchants", true));
	
	public FontRenderer font_render_In = mc.fontRenderer;
	public RenderItem render_item   = mc.getRenderItem();

	public ItemStack[] array;
	public ItemStack[] actual      = null;
	public List<ItemStack> stacks  = new ArrayList();
	public TurokEnchantManager[] enchants = {
		new TurokEnchantManager(Enchantments.PROJECTILE_PROTECTION, "Ppr"),
		new TurokEnchantManager(Enchantments.BANE_OF_ARTHROPODS, "Ban"),
		new TurokEnchantManager(Enchantments.BLAST_PROTECTION, "Bla"),
		new TurokEnchantManager(Enchantments.FIRE_PROTECTION, "Fpr"),
		new TurokEnchantManager(Enchantments.FEATHER_FALLING, "Fea"),
		new TurokEnchantManager(Enchantments.VANISHING_CURSE, "Van"),
		new TurokEnchantManager(Enchantments.LUCK_OF_THE_SEA, "Luc"),
		new TurokEnchantManager(Enchantments.AQUA_AFFINITY, "Aqu"),
		new TurokEnchantManager(Enchantments.DEPTH_STRIDER, "Dep"),
		new TurokEnchantManager(Enchantments.BINDING_CURSE, "Bin"),
		new TurokEnchantManager(Enchantments.FROST_WALKER, "Fro"),
		new TurokEnchantManager(Enchantments.RESPIRATION, "Res"),
		new TurokEnchantManager(Enchantments.FIRE_ASPECT, "Fia"),
		new TurokEnchantManager(Enchantments.PROTECTION, "Pro"),
		new TurokEnchantManager(Enchantments.EFFICIENCY, "Eff"),
		new TurokEnchantManager(Enchantments.UNBREAKING, "Unb"),
		new TurokEnchantManager(Enchantments.SILK_TOUCH, "Sil"),
		new TurokEnchantManager(Enchantments.SHARPNESS, "Sha"),
		new TurokEnchantManager(Enchantments.KNOCKBACK, "Knb"),
		new TurokEnchantManager(Enchantments.SWEEPING, "Swe"),
		new TurokEnchantManager(Enchantments.LOOTING, "Loo"),
		new TurokEnchantManager(Enchantments.FORTUNE, "For"),
		new TurokEnchantManager(Enchantments.MENDING, "Men"),
		new TurokEnchantManager(Enchantments.THORNS, "Thr"),
		new TurokEnchantManager(Enchantments.POWER, "Pow"),
		new TurokEnchantManager(Enchantments.PUNCH, "Pun"),
		new TurokEnchantManager(Enchantments.FLAME, "Fla"),
		new TurokEnchantManager(Enchantments.LURE, "Lur"),
	};

	@Override
	public void onWorldRender(RenderEvent event) {
		if (mc.getRenderManager().options == null) {
			return;
		}

		enable_gl();

		if (mc.world != null) {
			mc.world.loadedEntityList.stream()

			.filter(EntityUtil::isPlayer)
			.filter(player -> !EntityUtil.isFakeLocalPlayer(player))
			.filter(player -> mc.player.getDistance(player) < range.getValue())
			.sorted(Comparator.comparing(player -> - mc.player.getDistance(player)))
			.forEach(this::draw);
		}

		disable_gl();
	}

	public void draw(EntityPlayer player) {
		String player_stats    = player.getName() + (health.getValue() ? " " + "|" + Math.round(((EntityLivingBase) player).getHealth() + (player instanceof EntityPlayer ? ((EntityPlayer) player).getAbsorptionAmount() : 0)) : "");
		int width_player_stats = font_render_In.getStringWidth(player_stats) / 2;

		GlStateManager.pushMatrix();

		Vec3d player_position = EntityUtil.getInterpolatedRenderPos(player, mc.getRenderPartialTicks());
		
		double x = player_position.x;
		double y = player_position.y + player.height + 0.5f - (player.isSneaking() ? 0.25f : 0.0f);
		double z = player_position.z;

		GlStateManager.translate(x, y, z);

		GlStateManager.rotate(- mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate((float) (mc.getRenderManager().options.thirdPersonView == 2 ? -1 : 1) * mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);

		float distance = mc.player.getDistance(player);
		float scale   = (distance / 8f) * (float) (Math.pow(1.2589254f, scale_.getValue()));

		GlStateManager.scale(scale, scale, scale);
		GlStateManager.scale(- 0.025f, - 0.025f, 0.025f);

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableTexture2D();

		Tessellator tessellator      = Tessellator.getInstance();
		BufferBuilder buffer_builder = tessellator.getBuffer();

		GlStateManager.disableDepth();

		glTranslatef(0, -20, 0);

		buffer_builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		buffer_builder.pos(- width_player_stats - 1, 8, 0.0f).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
		buffer_builder.pos(- width_player_stats - 1, 19, 0.0f).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
		buffer_builder.pos(width_player_stats + 1, 19, 0.0f).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
		buffer_builder.pos(width_player_stats + 1, 8, 0.0f).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();

		tessellator.draw();

		buffer_builder.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

		buffer_builder.pos(- width_player_stats - 1, 8, 0.0d).color(.1f, .1f, .1f, .1f).endVertex();
		buffer_builder.pos(- width_player_stats - 1, 19, 0.0d).color(.1f, .1f, .1f, .1f).endVertex();
		buffer_builder.pos(width_player_stats + 1, 19, 0.0d).color(.1f, .1f, .1f, .1f).endVertex();
		buffer_builder.pos(width_player_stats + 1, 8, 0.0d).color(.1f, .1f, .1f, .1f).endVertex();
		
		tessellator.draw();

		GlStateManager.enableTexture2D();
		GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);

		if (!player.isSneaking()) {
			int color = (Friends.isFriend(player.getName()) ? 0x00bfff : 0xffffff);

			font_render_In.drawString(player_stats, - width_player_stats, 10, color);
		} else {
			InventoryPlayer inventory = player.inventory;

			ItemStack player_main_hand = player.getHeldItemMainhand(); // Main hand.
			ItemStack player_off_hand  = player.getHeldItemOffhand();  // Off Hand.
			
			ItemStack player_helmet     = inventory.armorItemInSlot(3); // Helmet.
			ItemStack player_chestplace = inventory.armorItemInSlot(2); // Chestplace.
			ItemStack player_leggins    = inventory.armorItemInSlot(1); // Leggins.
			ItemStack player_boots      = inventory.armorItemInSlot(0); // Boots.

			if (player_main_hand != null && player_off_hand != null) {
				actual = new ItemStack[] {
					player_main_hand,
					player_off_hand,
					player_helmet,
					player_chestplace,
					player_leggins,
					player_boots
				};
			} else if ((player_main_hand == null && player_off_hand != null)) {
				actual = new ItemStack[] {
					player_off_hand,
					player_helmet,
					player_chestplace,
					player_leggins,
					player_boots
				};
			} else if (player_main_hand != null && player_off_hand == null) {
				actual = new ItemStack[] {
					player_main_hand,
					player_helmet,
					player_chestplace,
					player_leggins,
					player_boots
				};
			} else {
				actual = new ItemStack[] {
					player_helmet,
					player_chestplace,
					player_leggins,
					player_boots
				};
			}

			int count = (array = actual).length;

			for (int int_ = 0; int_ < count; int_++) {
				ItemStack array_update = array[int_];

				if ((array_update != null) && (array_update.getItem() != null)) {
					stacks.add(array_update);
				}
			}

			int width = 16 * stacks.size() / 2;

			int player_x = - width;
			int player_y = - (font_render_In.FONT_HEIGHT + 1) - 20;

			GlStateManager.disableDepth();

			for (ItemStack stack : stacks) {
				player_x += 16;

				GlStateManager.pushMatrix();

				float scale1 = 0.3f;

				GlStateManager.translate(player_x - 3, player_y + 8, 0.0f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);
				GlStateManager.popMatrix();

				RenderHelper.enableGUIStandardItemLighting();

				render_item.zLevel = - 100.0f;

				GlStateManager.disableDepth();

				render_item.renderItemIntoGUI(stack, player_x, player_y);
				render_item.renderItemOverlayIntoGUI(font_render_In, stack, player_x, player_y, null);

				GlStateManager.enableDepth();
				GlStateManager.scale(0.75f, 0.75f, 0.75f);

				if (stack.isItemStackDamageable()) {
					float green = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
					float red   = 1 - green;

					int damage = 100 - (int) (red * 100);

					GlStateManager.disableDepth();

					mc.fontRenderer.drawStringWithShadow(damage + "", player_x + 8 - mc.fontRenderer.getStringWidth(damage + "") / 2, player_y - 11, ColourHolder.toHex((int) (red * 255), (int) (green * 255), 0));
					
					GlStateManager.enableDepth();
				}

				GlStateManager.scale(1.33f, 1.33f, 1.33f);

				TurokEnchantManager[] array_1;

				int count_1 = (array_1 = enchants).length; for (int int_ = 0; int_ < count_1; int_++) {
					TurokEnchantManager enchant = array_1[int_];

					int level = EnchantmentHelper.getEnchantmentLevel(enchant.get_enchant(), stack);

					String level_display = "" + level;

					if (level > 10) {
						level_display = "10+";
					}

					if (level > 0) {
						float scale_2 = 0.32F;

						GlStateManager.translate(player_x - 1, player_y + 2, 0.0f);
						GlStateManager.scale(0.42f, 0.42f, 0.42f);
						GlStateManager.disableDepth();
						GlStateManager.disableLighting();

						TurokGL.refresh_color(255, 255, 255, 255);

						font_render_In.drawString("\u00a7f" + enchant.get_name() + " " + level_display, 20 - font_render_In.getStringWidth("\u00a7f" + enchant.get_name() + " " + level_display) / 2, 0, Color.WHITE.getRGB(), true);
					
						TurokGL.refresh_color(255, 255, 255, 255);

						GlStateManager.enableLighting();
						GlStateManager.enableDepth();

						GlStateManager.scale(2.42f, 2.42f, 2.42f);

						GlStateManager.translate(- player_x + 1, - player_y, 0.0f);

						player_y += (int) ((font_render_In.FONT_HEIGHT + 3) * 0.28f);
					}
				}

				render_item.zLevel = 0.0f;

				RenderHelper.disableStandardItemLighting();
				GlStateManager.enableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.disableLighting();
				GlStateManager.popMatrix();
			}
		}
	}

	public void enable_gl() {
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
	}

	public void disable_gl() {
		RenderHelper.disableStandardItemLighting();

		GlStateManager.disableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}
}