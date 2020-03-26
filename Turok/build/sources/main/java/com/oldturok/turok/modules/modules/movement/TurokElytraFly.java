package com.oldturok.turok.module.modules.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

// Rina.
// Modify.
@Module.Info(name = "TurokElytraFly", description = "A Turok module for get fly into elytra.", category = Module.Category.TUROK_MOVEMENT)
public class TurokElytraFly extends Module {
    private Setting<Enum_fly_mode> fly_mode = register(Settings.e("Mode Fly", Enum_fly_mode.BOOST));
    
    @Override
    public void onUpdate() {
        if (mc.player.capabilities.isFlying) {
            mc.player.setVelocity(0.0, -0.003, 0.0);
            mc.player.capabilities.setFlySpeed(0.915f);
        }

        if (mc.player.onGround) {
            mc.player.capabilities.allowFlying = false;
        }

        if (!mc.player.isElytraFlying()) {
            return;
        }

        switch (fly_mode.getValue()) {
            case BOOST: {
                if (mc.player.isInWater()) {
                    mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    EntityPlayerSP player7;
                    EntityPlayerSP player = player7 = mc.player;

                    player7.motionY += 0.08;

                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    EntityPlayerSP player8;
                    EntityPlayerSP player2 = player8 = mc.player;

                    player8.motionY -= 0.04;
                }

                if (mc.gameSettings.keyBindForward.isKeyDown()) {
                    float yaw = (float) Math.toRadians(mc.player.rotationYaw);
                    
                    EntityPlayerSP player9;                    
                    EntityPlayerSP player3 = player9 = mc.player;
                    
                    player9.motionX -= MathHelper.sin(yaw) * 0.05f;
                    
                    EntityPlayerSP player10;
                    EntityPlayerSP player4 = player10 = mc.player;
                    
                    player10.motionZ += MathHelper.cos(yaw) * 0.05f;
                    break;
                }

                if (mc.gameSettings.keyBindBack.isKeyDown()) {
                    float yaw = (float) Math.toRadians(mc.player.rotationYaw);

                    EntityPlayerSP player11;
                    EntityPlayerSP player5 = player11 = mc.player;

                    player11.motionX += MathHelper.sin(yaw) * 0.05f;

                    EntityPlayerSP player12;
                    EntityPlayerSP player6 = player12 = mc.player;

                    player12.motionZ -= MathHelper.cos(yaw) * 0.05f;
                    break;
                }
                break;
            }

            case FLY: {
                mc.player.capabilities.setFlySpeed(0.915f);
                mc.player.capabilities.isFlying = true;

                if (mc.player.capabilities.isCreativeMode) {
                    return;
                }

                mc.player.capabilities.allowFlying = true;
                break;
            }
        }
    }
    
    @Override
    protected void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);

        if (mc.player.capabilities.isCreativeMode) {
            return;
        }

        mc.player.capabilities.allowFlying = false;
    }
    
    private enum Enum_fly_mode {
        BOOST, 
        FLY;
    }
}