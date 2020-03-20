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
import com.oldturok.turok.util.Friends;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokChat;

import org.lwjgl.opengl.GL11;

// Update by Rina 04/03/20.
// A TurokCA for pings more highs >> 250 ms..
// By Rina.
@Module.Info(name = "Turok Crystal Aura", description = "A aura for break crystals, fast and good for high ping.", category = Module.Category.TUROK_COMBAT)
public class TurokCrystalAura extends Module {
    private Setting<Integer> dano_minimo = register(Settings.integerBuilder("Min Dmg").withMinimum(0).withMaximum(16).withValue(2));
    
    private Setting<Integer> cor_red   = register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(200));
    private Setting<Integer> cor_green = register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(200));
    private Setting<Integer> cor_blue  = register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(200));
    private Setting<Integer> cor_alfa  = register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(70));

    private Setting<Boolean> auto_switch      = register(Settings.b("Auto Switch"));
    private Setting<Boolean> colocar          = register(Settings.b("Place", true));
    private Setting<Boolean> raytrace         = register(Settings.b("RayTrace", false));
    private Setting<Boolean> explodir         = register(Settings.b("Explode", true));
    private Setting<Boolean> varios           = register(Settings.b("More Fast", true));
    private Setting<Double> distancia         = register(Settings.d("Place Range", 6.0));
    private Setting<Double> quebrar_distancia = register(Settings.d("Break Range", 6.0));
    private Setting<Double> alcance           = register(Settings.d("Range", 6.0));
    private Setting<Boolean> linha_cubo       = register(Settings.b("Outline", true));
    private Setting<Boolean> ant_fraqueza     = register(Settings.b("Anti Weakness", false));
    private Setting<Double> tempo_de_colocar  = register(Settings.d("Place Delay", 0.0));
    private Setting<Double> tempo_de_ataque   = register(Settings.d("Hit Delay", 0.0));
    private Setting<Boolean> prefixo_chat     = register(Settings.b("Chat", true));

    private static boolean is_spoofing_angles;
    private static boolean toggle_pitch;
    private static double player_yaw;
    private static double player_pitch;
    private Entity render_ent;
    private BlockPos render;

    private boolean switch_cooldown = false;
    private boolean attack = false;

    private int old_slot = -1;
    private int new_slot;

    private int places;

    private AutoGG target_autogg = (AutoGG) ModuleManager.getModuleByName("AutoGG");
    public static String player_target;

    private long system_time = -1l;

    @EventHandler
    private Listener<PacketEvent.Send> packetListener = new Listener<PacketEvent.Send>(event -> {
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && is_spoofing_angles) {
            ((CPacketPlayer) packet).yaw = (float) player_yaw;
            ((CPacketPlayer) packet).pitch = (float) player_pitch;
        }

        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);

    @Override
    public void onEnable() {
        if (prefixo_chat.getValue()) {
            TurokMessage.send_msg("TurokCrystalAura -> " + ChatFormatting.GREEN + "ON");
        } else {
            return;
        }
    }

    @Override
    public void onDisable() {
        render     = null;
        render_ent = null;
        reset_rotation();

        if (prefixo_chat.getValue()) {
            TurokMessage.send_msg("TurokCrystalAura <- " + ChatFormatting.RED + "OFF");
        } else {
            return; 
        }

    }

    @Override
    public void onUpdate() {
        EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(d -> mc.player.getDistance(d))).orElse(null);
        if (explodir.getValue() && crystal != null && mc.player.getDistance(crystal) <= quebrar_distancia.getValue()) {
            if (System.nanoTime() / 1000000L - system_time >= tempo_de_ataque.getValue()) {
                if (ant_fraqueza.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!attack) {
                        old_slot = Wrapper.getPlayer().inventory.currentItem;
                        attack = true;
                    }

                    new_slot = -1;
                    for (int i = 0; i < 9; ++i) {
                        ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
                        if (stack != ItemStack.EMPTY) {
                            if (stack.getItem() instanceof ItemSword) {
                                new_slot = i;
                                break;
                            }
                            if (stack.getItem() instanceof ItemTool) {
                                new_slot = i;
                                break;
                            }
                        }
                    }

                    if (new_slot != -1) {
                        Wrapper.getPlayer().inventory.currentItem = new_slot;
                        switch_cooldown = true;
                    }
                }

                lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
                mc.playerController.attackEntity(mc.player, crystal);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                system_time = System.nanoTime() / 1000000L;


            if (places == 3) {
                places = 0;
            }

            }
        } else {
            reset_rotation();
            if (old_slot != -1) {
                Wrapper.getPlayer().inventory.currentItem = old_slot;
                old_slot = -1;
            }
            attack = false;
        }

        int crystal_slot = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? mc.player.inventory.currentItem : -1;
        if (crystal_slot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystal_slot = l;
                    break;
                }
            }
        }

        boolean off_hand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            off_hand = true;
        } else if (crystal_slot == -1) {
            return;
        }

        List<BlockPos> blocks = find_crystal_blocks();
        List<Entity> entities = new ArrayList<Entity>();

        entities.addAll((Collection<? extends Entity>) mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));

        BlockPos q = null;
        double damage = 0;
        for (Entity entity_two : entities) {
            if (entity_two != mc.player) {
                if (((EntityLivingBase) entity_two).getHealth() <= 0.0f || ((EntityLivingBase) entity_two).isDead) {
                    continue;
                }

                for (BlockPos blockPos : blocks) {
                    double b = entity_two.getDistanceSq(blockPos);
                    if (b >= distancia.getValue() * distancia.getValue()) {
                        continue;
                    }

                    double d = calculate_damage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity_two);
                    if (d <= damage) {
                        continue;
                    }

                    double self = calculate_damage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, mc.player);
                    if (self > d && d >= ((EntityLivingBase) entity_two).getHealth()) {
                        continue;
                    }

                    if (self - 0.5 > mc.player.getHealth()) {
                        continue;
                    }

                    if (d < dano_minimo.getValue()) {
                        continue;
                    }

                    player_target = ((EntityLivingBase) entity_two).getName();

                    damage = d;
                    q = blockPos;
                    render_ent = entity_two;
                }
            }
        }

        if (varios.getValue()) {
            places++;
        }

        if (damage == 0) {
            player_target = null;
            render = null;
            render_ent = null;
            reset_rotation();
            return;
        }

        render = q;
        if (colocar.getValue()) {
            if (!off_hand && mc.player.inventory.currentItem != crystal_slot) {
                if (auto_switch.getValue()) {
                    mc.player.inventory.currentItem = crystal_slot;
                    reset_rotation();
                    switch_cooldown = true;
                }

                return;
            }

            lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer) mc.player);
            EnumFacing f;
            if (raytrace.getValue()) {
                RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
                if (result == null || result.sideHit == null) {
                    f = EnumFacing.UP;
                } else {
                    f = result.sideHit;
                }
            } else {
                f = EnumFacing.DOWN;
            }
            
            if (switch_cooldown) {
                switch_cooldown = false;
                return;
            }
            if (System.nanoTime() / 1000000L - system_time >= tempo_de_colocar.getValue()) {
                mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItemOnBlock(q, f, off_hand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++places;
                system_time = System.nanoTime() / 1000000L;
            }
        }

        if (is_spoofing_angles) {
            if (toggle_pitch) {
                EntityPlayerSP player = mc.player;
                player.rotationPitch += 4.0E-4f;
                toggle_pitch = false;

            } else {
                EntityPlayerSP player = mc.player;
                player.rotationPitch -= 4.0E-4f;
                toggle_pitch = true;
            }
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (render != null) {
            int prepare = 0;
            int mask = 0;

            if (linha_cubo.getValue()) {
                prepare = GL11.GL_LINES;
                mask = GeometryMasks.Line.ALL;
            } else {
                prepare = GL11.GL_QUADS;
                mask = GeometryMasks.Quad.ALL;
            }

            TurokTessellator.prepare(prepare);

            if (linha_cubo.getValue()) {
                TurokTessellator.drawLines(render, cor_red.getValue(), cor_green.getValue(), cor_blue.getValue(), cor_alfa.getValue(), mask);
            } else {
                TurokTessellator.drawBox(render, cor_red.getValue(), cor_green.getValue(), cor_blue.getValue(), cor_alfa.getValue(), mask);
            }

            TurokTessellator.release();
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        set_yaw_and_pitch((float) v[0], (float) v[1]);
    }

    private boolean can_place_crystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && mc.world.getEntitiesWithinAABB((Class) Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB((Class) Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public static BlockPos get_player_pos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private List<BlockPos> find_crystal_blocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(get_sphere(get_player_pos(), alcance.getValue().floatValue(), alcance.getValue().intValue(), false, true, 0).stream().filter(this::can_place_crystal).collect(Collectors.toList()));
        return (List<BlockPos>) positions;
    }

    public List<BlockPos> get_sphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static float calculate_damage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = get_blast_reduction((EntityLivingBase) entity, get_damage_multiplied(damage), new Explosion((World) mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float get_blast_reduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static void reset_rotation() {
        if (is_spoofing_angles) {
            player_yaw = mc.player.rotationYaw;
            player_pitch = mc.player.rotationPitch;
            is_spoofing_angles = false;
        }
    }

    private static void set_yaw_and_pitch(float player_yaw_, float player_pitch_) {
        player_yaw = player_yaw_;
        player_pitch = player_pitch_;
        is_spoofing_angles = true;
    }

    public static float calculate_damage(EntityEnderCrystal crystal, Entity entity) {
        return calculate_damage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    private static float get_damage_multiplied(float damage) {
        int diff = TurokCrystalAura.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
}