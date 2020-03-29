package com.turok_mixins;

import com.oldturok.turok.module.ModuleManager;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.passive.EntityLlama;

@Mixin(EntityLlama.class)
public class TurokEntityLlama {
    @Inject(method = "canBeSteered", at = @At("RETURN"), cancellable = true)
    public void canBeSteered(CallbackInfoReturnable<Boolean> returnable) {
        if (ModuleManager.isModuleEnabled("EntitySpeed")) returnable.setReturnValue(true);
    }

}
