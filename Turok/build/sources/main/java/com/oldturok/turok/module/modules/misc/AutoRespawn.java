package com.oldturok.turok.module.modules.misc;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.command.Command;
import com.oldturok.turok.event.events.GuiScreenEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.client.gui.GuiGameOver;

@Module.Info(name = "AutoRespawn", description = "Respawn utility", category = Module.Category.TUROK_MISC)
public class AutoRespawn extends Module {

    private Setting<Boolean> respawn = register(Settings.b("Respawn", true));
    private Setting<Boolean> deathCoords = register(Settings.b("DeathCoords", false));
    private Setting<Boolean> antiGlitchScreen = register(Settings.b("Anti Glitch Screen", true));

    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener = new Listener<>(event -> {

        if (!(event.getScreen() instanceof GuiGameOver)) {
            return;
        }

        if (deathCoords.getValue() && mc.player.getHealth() <= 0) {
            Command.sendChatMessage(String.format("You died at x %d y %d z %d", (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ));
        }

        if (respawn.getValue() || (antiGlitchScreen.getValue() && mc.player.getHealth() > 0)) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }

    });

}
