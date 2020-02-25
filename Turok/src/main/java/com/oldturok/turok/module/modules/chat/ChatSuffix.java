package com.oldturok.turok.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;

import net.minecraft.network.play.client.CPacketChatMessage;
@Module.Info(name = "Chat Suffix", category = Module.Category.TUROK_CHAT, description = "Chat Suffix!")
public class ChatSuffix extends Module {

    private Setting<Boolean> commands = register(Settings.b("Commands", true));

    private final String TUROK_SUFFIX = " \u23D0 \u1355\u0e22\u0433\u0e4f\u043a";

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String msg = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (msg.startsWith("/") && commands.getValue()) return;
            if (msg.startsWith("?") && commands.getValue()) return;
            if (msg.startsWith("!") && commands.getValue()) return;
            if (msg.startsWith(":") && commands.getValue()) return;
            if (msg.startsWith(".") && commands.getValue()) return;
            if (msg.startsWith(";") && commands.getValue()) return;
            if (msg.startsWith(",") && commands.getValue()) return;
            if (msg.startsWith("#") && commands.getValue()) return;
            if (msg.startsWith("&") && commands.getValue()) return;
            if (msg.startsWith("%") && commands.getValue()) return;

            msg += TUROK_SUFFIX;
            if (msg.length() >= 256) msg = msg.substring(0, 256);
            ((CPacketChatMessage) event.getPacket()).message = msg;
        }
    });

}
