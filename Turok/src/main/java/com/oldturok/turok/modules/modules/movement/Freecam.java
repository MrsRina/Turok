package com.oldturok.turok.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import com.oldturok.turok.event.events.PlayerMoveEvent;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

// Update by Rina 09/03/20.
@Module.Info(name = "Free Camera", category = Module.Category.TUROK_PLAYER, description = "Leave your body and trascend into the realm of the gods")
public class Freecam extends Module {

    private Setting<Integer> speed = register(Settings.i("Speed", 5)); // /100 in practice

    private double posX, posY, posZ;
    private float pitch, yaw;

    private EntityOtherPlayerMP clonedPlayer;

    private boolean isRidingEntity;
    private Entity ridingEntity;

    @Override
    protected void onEnable() {
        if (mc.player != null) {
            isRidingEntity = mc.player.getRidingEntity() != null;

            if (mc.player.getRidingEntity() == null) {
                posX = mc.player.posX;
                posY = mc.player.posY;
                posZ = mc.player.posZ;
            } else {
                ridingEntity = mc.player.getRidingEntity();
                mc.player.dismountRidingEntity();
            }

            pitch = mc.player.rotationPitch;
            yaw = mc.player.rotationYaw;

            clonedPlayer = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
            clonedPlayer.copyLocationAndAnglesFrom(mc.player);
            clonedPlayer.rotationYawHead = mc.player.rotationYawHead;

            mc.world.addEntityToWorld(-100, clonedPlayer);

            mc.player.motionX = 0;
            mc.player.motionY = 0;
            mc.player.motionZ = 0;

            mc.player.jumpMovementFactor = speed.getValue()/2;
            mc.player.noClip = true;

            if (mc.gameSettings.keyBindJump.isKeyDown()) mc.player.motionY += speed.getValue();
            if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.player.motionY -= speed.getValue();
        }
    }

    @Override
    protected void onDisable() {
        EntityPlayer localPlayer = mc.player;
        if (localPlayer != null) {
            mc.player.setPositionAndRotation(posX, posY, posZ, yaw, pitch);
            mc.world.removeEntityFromWorld(-100);
            clonedPlayer = null;
            posX = posY = posZ = 0.D;
            pitch = yaw = 0.f;
            mc.player.capabilities.isFlying = false;
            mc.player.capabilities.setFlySpeed(0.05f);
            mc.player.noClip = false;
            mc.player.motionX = mc.player.motionY = mc.player.motionZ = 0.f;

            if (isRidingEntity) {
                mc.player.startRiding(ridingEntity, true);
            }
        }
    }

    @Override
    public void onUpdate() {
        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.setFlySpeed(speed.getValue() / 100f);
        mc.player.noClip = true;
        mc.player.onGround = false;
        mc.player.fallDistance = 0;
    }

    @EventHandler
    private Listener<PlayerMoveEvent> moveListener = new Listener<>(event -> {
        mc.player.noClip = true;
    });

    @EventHandler
    private Listener<PlayerSPPushOutOfBlocksEvent> pushListener = new Listener<>(event -> {
        event.setCanceled(true);
    });

    @EventHandler
    private Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) {
            event.cancel();
        }
    });
}
