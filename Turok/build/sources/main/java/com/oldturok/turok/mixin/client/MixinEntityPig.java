package com.oldturok.turok.mixin.client;

import com.oldturok.turok.module.ModuleManager;
import net.minecraft.entity.passive.EntityPig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPig.class)
public class MixinEntityPig {

    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void canBeSteered(CallbackInfoReturnable returnable) {
        if (ModuleManager.isModuleEnabled("EntitySpeed")) {
            returnable.setReturnValue(true);
            returnable.cancel();
        }
    }

}
