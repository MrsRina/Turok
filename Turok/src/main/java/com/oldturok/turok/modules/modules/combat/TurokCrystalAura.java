package com.oldturok.turok.module.modules.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.CombatRules;
import net.minecraft.util.NonNullList;
import net.minecraft.world.Explosion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemTool;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Collector;
import java.util.Collection;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.*;
import java.io.*;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import com.mojang.realmsclient.gui.ChatFormatting;

import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.module.modules.chat.AutoGG;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.GeometryMasks;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokFriends;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokChat;

import org.lwjgl.opengl.GL11;

// Thanks for original CA 086!
// This CA was made by Rina.
// The base CA was made by 006.
// This is amodify base.
@Module.Info(name = "TurokCrystalAura", description = "A good ca for good ping.", category = Module.Category.TUROK_COMBAT)
public class TurokCrystalAura extends Module {
	private Setting<Integer> min_damage  = register(Settings.integerBuilder("Damage for Place").withMinimum(0).withValue(2).withMaximum(10));
	private Setting<Boolean> ant_weaknes = register(Settings.b("Anti Wakness", false));
	private Setting<Boolean> auto_switch = register(Settings.b("Auto Switch"));
	private Setting<Boolean> ray_trace   = register(Settings.b("Ray Trace Insane"));

	private Setting<Double>  place_range = register(Settings.d("Place Range", 6));
	private Setting<Double>  break_range = register(Settings.d("Break Range", 6));

	private Setting<Double>  place_tick = register(Settings.d("Place Tick", 2));
	private Setting<Double>  break_tick = register(Settings.d("Break Tick", 2));

	private Setting<Integer> color_r = register(Settings.integerBuilder("Color Red").withMinimum(0).withMaximum(255).withValue(255));
	private Setting<Integer> color_g = register(Settings.integerBuilder("Color Green").withMinimum(0).withMaximum(255).withValue(255));
	private Setting<Integer> color_b = register(Settings.integerBuilder("Color Blue").withMinimum(0).withMaximum(255).withValue(255));
	private Setting<Integer> color_a = register(Settings.integerBuilder("Alpha Color").withMinimum(0).withMaximum(255).withValue(255));

	private Setting<TypeDraw> type = register(Settings.e("Type Draw", TypeDraw.OUTLINE));

	private BlockPos render;
	public  Entity render_ent;
	private Long system_time = - 1l;

	private static boolean toggle_pitch = false;
	private boolean switch_cool_down    = false;
	private boolean is_attacking        = false;

	int crystal_count_place;

	private int old_slot = - 1;
	private int new_slot;

	public int crystal_in_slot;

	public int prepare;
	public int mask;

	public boolean type_;

	@Override
	public void onDisable() {
		render     = null;
		render_ent = null;

		reset_rotation();

		TurokMessage.send_client_msg("TurokCrystalAura <- " + ChatFormatting.RED +  "OFF");
	}

	@Override
	public void onEnable() {
		if (mc.player != null && mc.world != null) {
			TurokMessage.send_client_msg("TurokCrystalAura -> " + ChatFormatting.GREEN + "ON");
		}
	}

	@Override
	public void onUpdate() {
		switch (type.getValue()) {
			case OUTLINE : {
				prepare = GL11.GL_LINES;
				mask    = GeometryMasks.Line.ALL;

				type_ = true;

				break;
			}

			case BOX : {
				prepare = GL11.GL_QUADS;
				mask    = GeometryMasks.Quad.ALL;

				type_ = false;

				break;
			}
		}

		EntityEnderCrystal crystal = mc.world.loadedEntityList.stream()
		.filter(crystal_entity -> crystal_entity instanceof EntityEnderCrystal)
		.map(crystal_entity -> (EntityEnderCrystal) crystal_entity)
		.min(Comparator.comparing(cry -> mc.player.getDistance(cry)))
		.orElse(null);

		if (crystal != null && mc.player.getDistance(crystal) <= break_range.getValue()) {
			if (((System.nanoTime() / 1000000) - system_time) > break_tick.getValue()) {
				if (ant_weaknes.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
					if (!is_attacking) {
						old_slot = mc.player.inventory.currentItem;

						is_attacking = true;
					}

					new_slot = - 1;

					for (int i = 0; i < 9; i++) {
						ItemStack find = mc.player.inventory.getStackInSlot(i);

						if (find != ItemStack.EMPTY) {
							if (find.getItem() instanceof ItemSword) {
								new_slot = i;
		
								break;
							}
		
							if (find.getItem() instanceof ItemTool) {
								new_slot = i;
		
								break;
							}
						}
					}

					if (new_slot != - 1) {
						mc.player.inventory.currentItem = new_slot;

						switch_cool_down = true;
					}

				}

				look_at_packet(crystal.posX, crystal.posY, crystal.posZ, mc.player);

				mc.playerController.attackEntity(mc.player, crystal);
				mc.player.swingArm(EnumHand.MAIN_HAND);

				system_time = System.nanoTime() / 1000000L;

				crystal_count_place++;
			}

			if (crystal_count_place == 2) {
				reset_rotation();

				crystal_count_place = 0;
			}
		} else {
			reset_rotation();

			if (old_slot != - 1) {
				mc.player.inventory.currentItem = old_slot;

				old_slot = - 1;
			}

			is_attacking = false;
		}

		crystal_in_slot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : - 1;

		if (crystal_in_slot == - 1) {
			for (int i = 0; i < 9; i++) {
				crystal_in_slot = i;
			}
		}

		boolean offhand = false;

		if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
			offhand = true;
		} else if (crystal_in_slot == - 1) {
			return;
		}

		List<BlockPos> blocks = find_crystal_blocks();
		List<Entity> entities = new ArrayList<>();

		entities.addAll(mc.world.playerEntities.stream().filter(player -> !TurokFriends.is_friend(player.getName())).collect(Collectors.toList()));

		BlockPos q    = null;
		double damage = 0;

		for (Entity player : entities) {
			if (((EntityLivingBase) player).getHealth() <= 0.0f || ((EntityLivingBase) player).isDead) {
				continue;
			}

			for (BlockPos block_pos : blocks) {
				double block_distance_player = player.getDistanceSq(block_pos);

				if (block_distance_player >= 169) {
					continue;
				}

				double calcule_damage_player = calcule_damage(block_pos.x + 0.5, block_pos.y + 1, block_pos.z + 0.5, player);

				if (calcule_damage_player < damage) {
					continue;
				}

				double self = calcule_damage(block_pos.x + 0.5, block_pos.y + 1, block_pos.z + 0.5, mc.player);
				if (self > calcule_damage_player && calcule_damage_player >= ((EntityLivingBase) player).getHealth()) {
					continue;
				}

				if (self - 0.5 > mc.player.getHealth()) {
					continue;
				}


				if (calcule_damage_player < min_damage.getValue()) {
					continue;
				}

				damage     = calcule_damage_player;
				q          = block_pos;
				render_ent = player;
			}
		}

		if (damage == 0) {
			render     = null;
			render_ent = null;

			reset_rotation();

			return;
		}

		render = q;

		if (!offhand && mc.player.inventory.currentItem != crystal_in_slot) {
			if (auto_switch.getValue()) {
				mc.player.inventory.currentItem = crystal_in_slot;

				reset_rotation();

				switch_cool_down = true;
			}

			return;
		}

		look_at_packet(q.x + 0.5, q.y - 0.5, q.z + 0.5, mc.player);
		
		EnumFacing f;

		if (ray_trace.getValue()) {
			RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5d, q.z + 0.5));

			if (result == null || result.sideHit == null) {
				f = EnumFacing.UP;
			} else {
				f = result.sideHit;
			}

			if (switch_cool_down) {
				switch_cool_down = false;

				return;
			}
		} else {
			f = EnumFacing.UP;
		}

		if (System.nanoTime() / 1000000L - system_time >= place_tick.getValue()) {
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
		}

		if (is_spoofing_angles) {
			if (toggle_pitch) {
				mc.player.rotationPitch += 4.0E-4f;
				toggle_pitch            = false;
			} else {
				mc.player.rotationPitch -= 4.0E-4f;
				toggle_pitch            = true;
			}
		}
	}

	@Override
	public void onWorldRender(RenderEvent event) {
		if (render_ent != null) {
			Color color = new Color(color_r.getValue(), color_g.getValue(), color_b.getValue(), color_a.getValue());

			TurokTessellator.prepare(prepare);

			if (type_) {
				TurokTessellator.drawLines(render, color.getRGB(), mask);
			} else {
				TurokTessellator.drawBox(render, color.getRGB(), mask);
			}

			TurokTessellator.release();
		}
	}

	public void look_at_packet(double px, double py, double pz, EntityPlayer me) {
		double[] v = EntityUtil.calculateLookAt(px, py, pz, me);

		set_yaw_and_pitch((float) v[0], (float) v[1]);
	}

	public boolean can_place_crystal(BlockPos block_pos) {
		BlockPos gay_ = block_pos.add(0, 1, 0);
		BlockPos gay  = block_pos.add(0, 2, 0);

		return (mc.world.getBlockState(block_pos).getBlock() == Blocks.OBSIDIAN ||
				mc.world.getBlockState(block_pos).getBlock() == Blocks.BEDROCK) && 
				mc.world.getBlockState(gay_).getBlock() == Blocks.AIR && 
				mc.world.getBlockState(gay).getBlock() == Blocks.AIR &&
				mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(gay_)).isEmpty() &&
				mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(gay)).isEmpty();
	}

	public static BlockPos player_pos() {
		return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
	}

	public List<BlockPos> find_crystal_blocks() {
		NonNullList<BlockPos> positions = NonNullList.create();

		positions.addAll(get_sphere(player_pos(), place_range.getValue().floatValue(), place_range.getValue().intValue(), false, true, 0).stream().filter(this::can_place_crystal).collect(Collectors.toList()));

		return positions;
	}

	public List<BlockPos> get_sphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
		List<BlockPos> circle_blocks = new ArrayList<>();

		int cx = loc.getX();
		int cy = loc.getY();
		int cz = loc.getZ();

		for (int x = cx - (int) r; x <= cx + r; ++x) {
			for (int z = cz - (int) r; z <= cz + r; ++z) {
				for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
					if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
						BlockPos l = new BlockPos(x, y + plus_y, z);
						
						circle_blocks.add(l);
					}
				}
			}
		}

		return circle_blocks;
	}

	public static float calcule_damage(double pos_x, double pos_y, double pos_z, Entity player) {
		float  double_explosion_size = 12.0f;
		double distanced_size        = player.getDistance(pos_x, pos_y, pos_z) / (double) double_explosion_size;
		Vec3d  vec3d                 = new Vec3d(pos_x, pos_y, pos_z);
		double block_density         = (double) player.world.getBlockDensity(vec3d, player.getEntityBoundingBox());
		double vect                  = (1.0d - distanced_size) * block_density;
		float  damage                = (float) ((int) ((vect * vect + vect) / 2.0d * 7.0d * (double) double_explosion_size + 1.0d));
		double final_                = 1;

		if (player instanceof EntityLivingBase) {
			final_ = get_blast_reduction((EntityLivingBase) player, get_mutiplied(damage), new Explosion(mc.world, null, pos_x, pos_y, pos_z, 6f, false, true));
		}

		return (float) final_;
	}

	public static float get_blast_reduction(EntityLivingBase player, float damage, Explosion explosion) {
		if (player instanceof EntityPlayer) {
			EntityPlayer player_ = (EntityPlayer) player;
			DamageSource damage_ = DamageSource.causeExplosionDamage(explosion);

			damage = CombatRules.getDamageAfterAbsorb(damage, (float) player_.getTotalArmorValue(), (float) player_.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

			int   k = EnchantmentHelper.getEnchantmentModifierDamage(player_.getArmorInventoryList(), damage_);
			float f = MathHelper.clamp(k, 0.0f, 20.0f);

			damage *= damage * (1.0f - f / 25.0f);

			int gay_potion = 11;

			if (player.isPotionActive(Potion.getPotionById(gay_potion))) {
				damage -= damage - (damage / 4);
			}

			damage = Math.max(damage - player_.getAbsorptionAmount(), 0.0f);

			return damage;
		}

		damage = CombatRules.getDamageAfterAbsorb(damage, (float) player.getTotalArmorValue(), (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
		return damage;
	}

	public static float get_mutiplied(float damage) {
		int diff = mc.world.getDifficulty().getId();

		return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
	}

	public static float calcule_damage(EntityEnderCrystal crystal, Entity player) {
		return calcule_damage(crystal.posX, crystal.posY, crystal.posZ, player);
	}

	public static boolean is_spoofing_angles;

	public static double yaw;
	public static double pitch;

	@EventHandler
	private Listener<PacketEvent.Send> packetListener = new Listener<>(event -> {
		Packet packet = event.getPacket();

		if (packet instanceof CPacketPlayer) {
			((CPacketPlayer) packet).yaw   = (float) yaw;
			((CPacketPlayer) packet).pitch = (float) pitch;
		}
	});

	public static void set_yaw_and_pitch(float yaw_, float pitch_) {
		yaw   = yaw_;
		pitch = pitch_;

		is_spoofing_angles = true;
	}

	public static void reset_rotation() {
		if (is_spoofing_angles) {
			yaw   = mc.player.rotationYaw;
			pitch = mc.player.rotationPitch;

			is_spoofing_angles = false;
		}
	}

	public enum TypeDraw {
		OUTLINE,
		BOX
	}
}