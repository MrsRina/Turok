package com.oldturok.turok.mixin.client;

import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import com.oldturok.turok.module.ModuleManager;
import net.minecraft.network.PacketBuffer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(C00Handshake.class)
public class MixinC00Handshake {

    @Shadow int protocolVersion;
    @Shadow String ip;
    @Shadow int port;
    @Shadow EnumConnectionState requestedState;

    @Inject(method = "writePacketData", at = @At(value = "HEAD"), cancellable = true)
    public void writePacketData(PacketBuffer buf, CallbackInfo info) {
        if (ModuleManager.isModuleEnabled("FakeVanilla")) {
            info.cancel();
            buf.writeVarInt(protocolVersion);
            buf.writeString(ip);
            buf.writeShort(port);
            buf.writeVarInt(requestedState.getId());
        }
    }

}
