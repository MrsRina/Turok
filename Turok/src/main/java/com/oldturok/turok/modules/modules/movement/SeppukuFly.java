package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.util.BlockInteractionHelper;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.module.Module;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.List;

//
// About this code, is not a paste, i modify somethings and the seppuku is public.
// Thanks Memmez for source.
// The seppuku is a good fly, I cant make a better.
// Enjoy.
//

// 
// All rights reserver for who make it embed, and who make it in the Seppuku too.
// I really dont pasted, the rights its on.
// Enjoy.
//
@Module.Info(name = "TurokSeppukuFly",  description = "A seppuku fly embed in Turok!", category = Module.Category.TUROK_MOVEMENT)
public class SeppukuFly extends Module {
    public final Setting<Float> fly_speed;
    public final Setting<Boolean> fly_no_kick;

    private int fly_teleport_id;

    private List<CPacketPlayer> fly_packets;

    @EventHandler
    public Listener<InputUpdateEvent> listener;

    @EventHandler
    public Listener<PacketEvent.Send> sendListener;

    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;

    public SeppukuFly() {
        this.fly_speed   = this.register((Setting<Float>)Settings.floatBuilder("Speed").withValue(0.1f).withMaximum(5.0f).withMinimum(0.0f).build());
        this.fly_no_kick = this.register(Settings.b("NoKick", true));
        this.fly_packets = new ArrayList<CPacketPlayer>();

        final CPacketPlayer[] fly_bounds      = new CPacketPlayer[1];
        final double[] fly_speed_y            = new double[1];
        final double[] fly_speed_y_2          = new double[1];
        final double[] fly_n                  = new double[1];
        final double[][] fly_direcional_speed = new double[1][1];

        final int[] fly_i = new int[1];
        final int[] fly_j = new int[1];
        final int[] fly_k = new int[1];

        this.listener = new Listener<InputUpdateEvent>(event -> {
            if (this.fly_teleport_id <= 0) {
                fly_bounds[0] = (CPacketPlayer) new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, 0.0, Minecraft.getMinecraft().player.posZ, Minecraft.getMinecraft().player.onGround);
                this.fly_packets.add(fly_bounds[0]);
                Minecraft.getMinecraft().player.connection.sendPacket((Packet) fly_bounds[0]);
                return;
            } else {
                SeppukuFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (SeppukuFly.mc.world.getCollisionBoxes((Entity)SeppukuFly.mc.player, SeppukuFly.mc.player.getEntityBoundingBox().expand(-0.0625, 0.0, -0.0625)).isEmpty()) {
                    fly_speed_y[0] = 0.0;
                    if (SeppukuFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (this.fly_no_kick.getValue()) {
                            fly_speed_y_2[0] = ((SeppukuFly.mc.player.ticksExisted % 20 == 0) ? -0.03999999910593033 : 0.06199999898672104);
                        } else {
                            fly_speed_y_2[0] = 0.06199999898672104;
                        }
                    } else if (SeppukuFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        fly_speed_y_2[0] = -0.062;
                    } else {
                        if (SeppukuFly.mc.world.getCollisionBoxes((Entity)SeppukuFly.mc.player, SeppukuFly.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                            if (SeppukuFly.mc.player.ticksExisted % 4 == 0) {
                                fly_n[0] = (this.fly_no_kick.getValue() ? -0.04f : 0.0f);
                            } else {
                                fly_n[0] = 0.0;
                            }
                        } else {
                            fly_n[0] = 0.0;
                        }
                        fly_speed_y_2[0] = fly_n[0];
                    }

                    fly_direcional_speed[0] = BlockInteractionHelper.directionSpeed(this.fly_speed.getValue());
                    if (SeppukuFly.mc.gameSettings.keyBindJump.isKeyDown() || SeppukuFly.mc.gameSettings.keyBindSneak.isKeyDown() || SeppukuFly.mc.gameSettings.keyBindForward.isKeyDown() || SeppukuFly.mc.gameSettings.keyBindBack.isKeyDown() || SeppukuFly.mc.gameSettings.keyBindRight.isKeyDown() || SeppukuFly.mc.gameSettings.keyBindLeft.isKeyDown()) {
                        if (fly_direcional_speed[0][0] != 0.0 || fly_speed_y_2[0] != 0.0 || fly_direcional_speed[0][1] != 0.0) {
                            if (SeppukuFly.mc.player.movementInput.jump && (SeppukuFly.mc.player.moveStrafing != 0.0f || SeppukuFly.mc.player.moveForward != 0.0f)) {
                                SeppukuFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                                this.move(0.0, 0.0, 0.0);
                                for (fly_i[0] = 0; fly_i[0] <= 3; ++fly_i[0]) {
                                    SeppukuFly.mc.player.setVelocity(0.0, fly_speed_y_2[0] * fly_i[0], 0.0);
                                    this.move(0.0, fly_speed_y_2[0] * fly_i[0], 0.0);
                                }
                            } else if (SeppukuFly.mc.player.movementInput.jump) {
                                SeppukuFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                                this.move(0.0, 0.0, 0.0);
                                for (fly_j[0] = 0; fly_j[0] <= 3; ++fly_j[0]) {
                                    SeppukuFly.mc.player.setVelocity(0.0, fly_speed_y_2[0] * fly_j[0], 0.0);
                                    this.move(0.0, fly_speed_y_2[0] * fly_j[0], 0.0);
                                }
                            } else {
                                for (fly_k[0] = 0; fly_k[0] <= 2; ++fly_k[0]) {
                                    SeppukuFly.mc.player.setVelocity(fly_direcional_speed[0][0] * fly_k[0], fly_speed_y_2[0] * fly_k[0], fly_direcional_speed[0][1] * fly_k[0]);
                                    this.move(fly_direcional_speed[0][0] * fly_k[0], fly_speed_y_2[0] * fly_k[0], fly_direcional_speed[0][1] * fly_k[0]);
                                }
                            }
                        }
                    } else if (this.fly_no_kick.getValue() && SeppukuFly.mc.world.getCollisionBoxes((Entity)SeppukuFly.mc.player, SeppukuFly.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                        SeppukuFly.mc.player.setVelocity(0.0, (SeppukuFly.mc.player.ticksExisted % 2 == 0) ? 0.03999999910593033 : -0.03999999910593033, 0.0);
                        this.move(0.0, (SeppukuFly.mc.player.ticksExisted % 2 == 0) ? 0.03999999910593033 : -0.03999999910593033, 0.0);
                    }
                }

                return;
            }
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);

        final CPacketPlayer[] packet = new CPacketPlayer[1];

        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                event.cancel();
            }

            if (event.getPacket() instanceof CPacketPlayer) {
                packet[0] = (CPacketPlayer)event.getPacket();
                if (this.fly_packets.contains(packet[0])) {
                    this.fly_packets.remove(packet[0]);
                } else {
                    event.cancel();
                }
            }

            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);

        final SPacketPlayerPosLook[] packet2 = new SPacketPlayerPosLook[1];
        this.receiveListener                 = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                packet2[0] = (SPacketPlayerPosLook)event.getPacket();
                if (Minecraft.getMinecraft().player.isEntityAlive()) {
                    if (Minecraft.getMinecraft().world.isBlockLoaded(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)) && !(Minecraft.getMinecraft().currentScreen instanceof GuiDownloadTerrain)) {
                        if (this.fly_teleport_id <= 0) {
                            this.fly_teleport_id = packet2[0].getTeleportId();
                        } else {
                            event.cancel();
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }

    public void onEnable() {
        if (SeppukuFly.mc.world != null) {
            this.fly_teleport_id = 0;

            this.fly_packets.clear();

            final CPacketPlayer fly_bounds = (CPacketPlayer)new CPacketPlayer.Position(SeppukuFly.mc.player.posX, 0.0, SeppukuFly.mc.player.posZ, SeppukuFly.mc.player.onGround);
            
            this.fly_packets.add(fly_bounds);
            
            SeppukuFly.mc.player.connection.sendPacket((Packet)fly_bounds);
        }
    }

    private void move(final double x, final double y, final double z) {
        final Minecraft mc      = Minecraft.getMinecraft();
        final CPacketPlayer pos = (CPacketPlayer)new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z, mc.player.onGround);
        
        this.fly_packets.add(pos);
        
        mc.player.connection.sendPacket((Packet) pos);
        
        final CPacketPlayer fly_bounds = (CPacketPlayer)new CPacketPlayer.Position(mc.player.posX + x, 0.0, mc.player.posZ + z, mc.player.onGround);
        
        this.fly_packets.add(fly_bounds);

        mc.player.connection.sendPacket((Packet) fly_bounds);

        ++this.fly_teleport_id;

        mc.player.connection.sendPacket((Packet) new CPacketConfirmTeleport(this.fly_teleport_id - 1));
        mc.player.connection.sendPacket((Packet) new CPacketConfirmTeleport(this.fly_teleport_id));
        mc.player.connection.sendPacket((Packet) new CPacketConfirmTeleport(this.fly_teleport_id + 1));
    }
}