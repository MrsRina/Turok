package com.oldturok.turok.module.modules.combat;

import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.event.events.RenderEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import com.oldturok.turok.util.EntityUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.oldturok.turok.util.Friends;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import com.oldturok.turok.util.Wrapper;
import net.minecraft.init.MobEffects;
import java.util.Comparator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.oldturok.turok.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.oldturok.turok.command.Command;

// FastCrysalAura, the best than kami old.
// By Rina.
@Module.Info(name = "InsaneCrystalAura", category = Module.Category.TUROK_COMBAT)
public class InsaneCrystalAura extends Module {
    private Setting<Integer> dano_minimo = register(Settings.integerBuilder("Min Dmg").withMinimum(0).withMaximum(16).withValue(2));
    
    private Setting<Integer> cor_red   = register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(0));
    private Setting<Integer> cor_green = register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(0));
    private Setting<Integer> cor_blue  = register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(190));
    private Setting<Integer> cor_alfa  = register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(70));

    private Setting<Boolean> auto_switch     = register(Settings.b("Auto Switch"));
    private Setting<Boolean> colocar         = register(Settings.b("Place", true));
    private Setting<Boolean> raytrace        = register(Settings.b("RayTrace", false));
    private Setting<Boolean> explodir        = register(Settings.b("Explode", true));
    private Setting<Boolean> varios          = register(Settings.b("Multiple Places", true));
    private Setting<Double> distancia        = register(Settings.d("Place Range", 6.0));
    private Setting<Double> alcance          = register(Settings.d("Range", 6.0));
    private Setting<Boolean> ant_fraqueza    = register(Settings.b("Anti Weakness", false));
    private Setting<Double> tempo_de_colocar = register(Settings.d("Place Delay", 0.0));
    private Setting<Double> tempo_de_ataque  = register(Settings.d("Hit Delay", 0.0));
    private Setting<Boolean> prefixo_chat    = register(Settings.b("Chat", true));

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

    private long system_time = -1l;

    @EventHandler
    private Listener<PacketEvent.Send> packetListener = new Listener<PacketEvent.Send>(event -> {
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && is_spoofing_angles) {
            ((CPacketPlayer)packet).yaw = (float) player_yaw;
            ((CPacketPlayer)packet).pitch = (float) player_pitch;
        }

        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);

    @Override
    public void onEnable() {
        if (prefixo_chat.getValue()) {
            Command.sendChatMessage("InsaneCrystalAura <- " + ChatFormatting.GREEN + "Enabled!");
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
            Command.sendChatMessage("InsaneCrystalAura -> " + ChatFormatting.RED + "Disabled!");
        } else {
            return; 
        }

    }

    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(d -> mc.player.getDistance(d))).orElse(null);
        if (explodir.getValue() && crystal != null && mc.player.getDistance(crystal) <= alcance.getValue()) {
            if (System.nanoTime() / 1000000L - system_time >= tempo_de_ataque.getValue()) {
                if (ant_fraqueza.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!attack) {
                        old_slot = Wrapper.getPlayer().inventory.currentItem;
                        attack = true;
                    }

                    new_slot = -1;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
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
            }

            if (!varios.getValue()) {
                return;
            }

            if (places == 3) {
                places = 0;
                return;
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
        double damage = 0.5;
        for (Entity entity_two : entities) {
            if (entity_two != mc.player) {
                if (((EntityLivingBase)entity_two).getHealth() <= 0.0f) {
                    continue;
                }

                for (BlockPos blockPos : blocks) {
                    double b = entity_two.getDistanceSq(blockPos);
                    if (b >= distancia.getValue() * distancia.getValue()) {
                        continue;
                    }

                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity_two);
                    if (d <= damage) {
                        continue;
                    }

                    double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, mc.player);
                    if (self > d && d >= ((EntityLivingBase) entity_two).getHealth()) {
                        continue;
                    }

                    if (self - 0.5 > mc.player.getHealth()) {
                        continue;
                    }

                    if (d < dano_minimo.getValue()) {
                        continue;
                    }

                    damage = d;
                    q = blockPos;
                    render_ent = entity_two;
                }
            }
        }

        if (damage == 0.5) {
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
                final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
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
                final EntityPlayerSP player = mc.player;
                player.rotationPitch -= 4.0E-4f;
                toggle_pitch = true;
            }
        }
    }

    @Override
    public void onWorldRender(final RenderEvent event) {
        if (render != null) {
            TurokTessellator.prepare(7);
            TurokTessellator.drawBox(render, cor_red.getValue(), cor_green.getValue(), cor_blue.getValue(), cor_alfa.getValue(), 63);
            TurokTessellator.release();
        }
    }

    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && mc.world.getEntitiesWithinAABB((Class) Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB((Class) Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private List<BlockPos> find_crystal_blocks() {
        final NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(getPlayerPos(), alcance.getValue().floatValue(), alcance.getValue().intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return (List<BlockPos>) positions;
    }

    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion((World) mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
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

    private static void setYawAndPitch(final float player_yaw_, final float player_pitch_) {
        player_yaw = player_yaw_;
        player_pitch = player_pitch_;
        is_spoofing_angles = true;
    }

    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    private static float getDamageMultiplied(final float damage) {
        final int diff = InsaneCrystalAura.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
}