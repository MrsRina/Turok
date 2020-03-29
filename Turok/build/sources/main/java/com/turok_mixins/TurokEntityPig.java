package com.turok_mixins;

import com.oldturok.turok.module.ModuleManager;

import net.minecraft.entity.passive.EntityPig;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPig.class)
public class TurokEntityPig {
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void canBeSteered(CallbackInfoReturnable returnable) {
        if (ModuleManager.isModuleEnabled("EntitySpeed")) {
            returnable.setReturnValue(true);
            returnable.cancel();
        }
    }

}
