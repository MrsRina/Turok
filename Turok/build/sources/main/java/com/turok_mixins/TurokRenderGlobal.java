package com.turok_mixins;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderGlobal.class)
public class TurokRenderGlobal {

    @Shadow Minecraft mc;
    @Shadow public ChunkRenderContainer renderContainer;

    @Inject(method = "renderBlockLayer(Lnet/minecraft/util/BlockRenderLayer;)V", at = @At("HEAD"), cancellable = true)
    public void renderBlockLayer(BlockRenderLayer blockLayerIn, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }

}
