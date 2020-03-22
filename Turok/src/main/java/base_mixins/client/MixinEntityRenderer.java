package base_mixins.client;

import com.oldturok.turok.module.modules.render.NoHurtEffect;
import com.oldturok.turok.module.modules.render.Brightness;
import com.oldturok.turok.module.ModuleManager;

import com.google.common.base.Predicate;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    private boolean nightVision = false;

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;rayTraceBlocks(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;"))
    public RayTraceResult rayTraceBlocks(WorldClient world, Vec3d start, Vec3d end) {
        if (ModuleManager.isModuleEnabled("BypassCamera"))
            return null;
        else
            return world.rayTraceBlocks(start, end);
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffect(float ticks, CallbackInfo info) {
        if (NoHurtEffect.shouldDisable()) info.cancel();
    }

    @Redirect(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    public boolean isPotionActive(EntityPlayerSP player, Potion potion) {
        return (nightVision = Brightness.shouldBeActive()) || player.isPotionActive(potion);
    }

    @Redirect(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;getNightVisionBrightness(Lnet/minecraft/entity/EntityLivingBase;F)F"))
    public float getNightVisionBrightnessMixin(EntityRenderer renderer, EntityLivingBase entity, float partialTicks) {
        if (nightVision) return Brightness.getCurrentBrightness();
        return renderer.getNightVisionBrightness(entity, partialTicks);
    }
}