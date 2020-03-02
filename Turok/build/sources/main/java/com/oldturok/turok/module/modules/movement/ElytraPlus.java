package com.oldturok.turok.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

/**
 * By CaptainWavedash.
 */
@Module.Info(name = "Elytra+", description = "nyoom", category = Module.Category.TUROK_MOVEMENT)
public class ElytraPlus extends Module
{
    private Setting<ElytraFlightMode> mode;
    
    public ElytraPlus() {
        this.mode = this.register(Settings.e("Mode", ElytraFlightMode.BOOST));
    }
    
    @Override
    public void onUpdate() {
        if (ElytraPlus.mc.player.capabilities.isFlying) {
            ElytraPlus.mc.player.setVelocity(0.0, -0.003, 0.0);
            ElytraPlus.mc.player.capabilities.setFlySpeed(0.915f);
        }
        if (ElytraPlus.mc.player.onGround) {
            ElytraPlus.mc.player.capabilities.allowFlying = false;
        }
        if (!ElytraPlus.mc.player.isElytraFlying()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                if (ElytraPlus.mc.player.isInWater()) {
                    ElytraPlus.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraPlus.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }
                if (ElytraPlus.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player7;
                    final EntityPlayerSP player = player7 = ElytraPlus.mc.player;
                    player7.motionY += 0.08;
                }
                else if (ElytraPlus.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP player8;
                    final EntityPlayerSP player2 = player8 = ElytraPlus.mc.player;
                    player8.motionY -= 0.04;
                }
                if (ElytraPlus.mc.gameSettings.keyBindForward.isKeyDown()) {
                    final float yaw = (float)Math.toRadians(ElytraPlus.mc.player.rotationYaw);
                    final EntityPlayerSP player9;
                    final EntityPlayerSP player3 = player9 = ElytraPlus.mc.player;
                    player9.motionX -= MathHelper.sin(yaw) * 0.05f;
                    final EntityPlayerSP player10;
                    final EntityPlayerSP player4 = player10 = ElytraPlus.mc.player;
                    player10.motionZ += MathHelper.cos(yaw) * 0.05f;
                    break;
                }
                if (ElytraPlus.mc.gameSettings.keyBindBack.isKeyDown()) {
                    final float yaw = (float)Math.toRadians(ElytraPlus.mc.player.rotationYaw);
                    final EntityPlayerSP player11;
                    final EntityPlayerSP player5 = player11 = ElytraPlus.mc.player;
                    player11.motionX += MathHelper.sin(yaw) * 0.05f;
                    final EntityPlayerSP player12;
                    final EntityPlayerSP player6 = player12 = ElytraPlus.mc.player;
                    player12.motionZ -= MathHelper.cos(yaw) * 0.05f;
                    break;
                }
                break;
            }
            case FLY: {
                ElytraPlus.mc.player.capabilities.setFlySpeed(0.915f);
                ElytraPlus.mc.player.capabilities.isFlying = true;
                if (ElytraPlus.mc.player.capabilities.isCreativeMode) {
                    return;
                }
                ElytraPlus.mc.player.capabilities.allowFlying = true;
                break;
            }
        }
    }
    
    @Override
    protected void onDisable() {
        ElytraPlus.mc.player.capabilities.isFlying = false;
        ElytraPlus.mc.player.capabilities.setFlySpeed(0.05f);
        if (ElytraPlus.mc.player.capabilities.isCreativeMode) {
            return;
        }
        ElytraPlus.mc.player.capabilities.allowFlying = false;
    }
    
    private enum ElytraFlightMode
    {
        BOOST, 
        FLY;
    }
}
