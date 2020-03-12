package com.oldturok.turok.mixin.client;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiChat;

import com.oldturok.turok.gui.mc.TurokGuiChat;
import com.oldturok.turok.chatcmd.Chat;
import com.oldturok.turok.util.Wrapper;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat {
    @Shadow protected GuiTextField inputField;

    @Shadow public String historyBuffer;

    @Shadow public int sentHistoryCursor;

    @Shadow public abstract void initGui();

    @Inject(method = "Lnet/minecraft/client/gui/GuiChat;keyTyped(CI)V", at = @At("RETURN"))
    public void returnKeyTyped(char typedChar, int keyCode, CallbackInfo info) {
        if (!(Wrapper.getMinecraft().currentScreen instanceof GuiChat) || Wrapper.getMinecraft().currentScreen instanceof TurokGuiChat) return;
        if (inputField.getText().startsWith(Chat.getChatPrefix())) {
            Wrapper.getMinecraft().displayGuiScreen(new TurokGuiChat(inputField.getText(), historyBuffer, sentHistoryCursor));
        }
    }
}
