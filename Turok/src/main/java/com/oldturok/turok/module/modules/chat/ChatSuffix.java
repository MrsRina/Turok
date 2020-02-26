package com.oldturok.turok.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;

import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Info(name = "ChatSuffix", category = Module.Category.TUROK_CHAT, description = "Modifies your chat messages")
public class ChatSuffix extends Module {
    private Setting<Boolean> commands = register(Settings.b("Commands", true));
    private Setting<Boolean> iSpeakSuffix = register(Settings.b("I Speak Taco Suffix", false));
    private Setting<Boolean> iAmTurokSuffix = register(Settings.b("I Am Turok Suffix", true));
    private Setting<Boolean> turokSuffix = register(Settings.b("Turok Suffix", false));

    private final String TUROK_SUFFIX = " \u23D0 \u1d1b\u1d1c\u0280\u0473\u1d0b";
    private final String I_AM_TUROK_SUFFIX = " \u23D0 \u026a \u1d00\u1d0d \u1d1b\u1d1c\u0280\u0473\u1d0b";
    private final String I_SPEAK_TACO_SUFFIX = " \u23D0 \u026a \u0073\u1d18\u1d07\u1d00\u1d0b \u1d1b\u1d00\u1d04\u1d0f";

    boolean suffix_accept;
    String suffix;

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {         
            if (turokSuffix.getValue()) {
                suffix = TUROK_SUFFIX;
                suffix_accept = true;
            } 

            if (iAmTurokSuffix.getValue()) {
                suffix = I_AM_TUROK_SUFFIX;
                suffix_accept = true;
            }

            if (iSpeakSuffix.getValue()) {
                suffix = I_SPEAK_TACO_SUFFIX;
                suffix_accept = true;
            }

            String msg = ((CPacketChatMessage) event.getPacket()).getMessage();

            if (msg.startsWith("/") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith("&") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith("?") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith(":") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith(";") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith(".") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith(",") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith("-") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith("_") && commands.getValue()) suffix_accept = false;
            if (msg.startsWith("#") && commands.getValue()) suffix_accept = false;

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