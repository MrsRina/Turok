package com.oldturok.turok.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.network.play.client.CPacketChatMessage;

import com.oldturok.turok.util.TurokFancyChat;
import com.oldturok.turok.TurokMod;

// Rina.
@Module.Info(name = "ChatSuffix", category = Module.Category.TUROK_CHAT, description = "Modifies your chat messages")
public class ChatSuffix extends Module {
    private Setting <Boolean> commands     = register(Settings.b("Commands", true));
    private Setting <Boolean> i_speak_taco = register(Settings.b("I Speak Taco Suffix", false));
    private Setting <Boolean> turok        = register(Settings.b("Turok Suffix", true));

    boolean suffix_accept;
    String suffix;

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {         
            if (turok.getValue()) {
                suffix        = TurokFancyChat.TUROK_SUFFIX;
                suffix_accept = true;

                i_speak_taco.setValue(false);
            } 

            if (i_speak_taco.getValue()) {
                suffix        = TurokFancyChat.I_SPEAK_TACO_SUFFIX;
                suffix_accept = true;

                turok.setValue(false);
            }

            String msg = ((CPacketChatMessage) event.getPacket()).getMessage();

            if (msg.startsWith("/") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith("&") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith("?") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith("!") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith(":") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith(";") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith(".") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith(",") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith("-") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith("_") && commands.getValue()) suffix_accept = false; // Ignore.
            if (msg.startsWith("#") && commands.getValue()) suffix_accept = false; // Ignore.

            if (suffix_accept == true){
                msg += suffix;
            } else {
                return;
            }
                
            if (msg.length() >= 256) msg = msg.substring(0, 256);
            ((CPacketChatMessage) event.getPacket()).message = msg;
        }
    });
}