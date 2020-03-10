package com.oldturok.turok.mixin.client;

import com.oldturok.turok.module.modules.render.ExtraTab;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.gui.GuiPlayerTabOverlay;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay {

    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"))
    public List subList(List list, int fromIndex, int toIndex) {
        return list.subList(fromIndex, ExtraTab.INSTANCE.isEnabled() ? Math.min(ExtraTab.INSTANCE.tabSize.getValue(), list.size()) : toIndex);
    }

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable returnable) {
        if (ExtraTab.INSTANCE.isEnabled()) {
            returnable.cancel();
            returnable.setReturnValue(ExtraTab.getPlayerName(networkPlayerInfoIn));
        }
    }

}
