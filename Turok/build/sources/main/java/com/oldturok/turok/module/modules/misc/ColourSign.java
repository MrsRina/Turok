package com.oldturok.turok.module.modules.misc;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.command.Command;
import com.oldturok.turok.event.events.GuiScreenEvent;
import com.oldturok.turok.module.Module;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;

@Module.Info(name = "ColourSign", description = "Allows ingame colouring of text on signs", category = Module.Category.TUROK_MISC)
public class ColourSign extends Module {

    @EventHandler
    public Listener<GuiScreenEvent.Displayed> eventListener = new Listener<>(event -> {
        if (event.getScreen() instanceof GuiEditSign && isEnabled()) {
            event.setScreen(new TurokGuiEditSign(((GuiEditSign) event.getScreen()).tileSign));
        }
    });

    private class TurokGuiEditSign extends GuiEditSign {


        public TurokGuiEditSign(TileEntitySign teSign) {
            super(teSign);
        }

        @Override
        public void initGui() {
            super.initGui();
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            if (button.id == 0) {
                this.tileSign.signText[this.editLine] = new TextComponentString(tileSign.signText[this.editLine].getFormattedText().replaceAll("(" + Command.SECTIONSIGN() + ")(.)", "$1$1$2$2"));
            }
            super.actionPerformed(button);
        }
        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            String s = ((TextComponentString) tileSign.signText[this.editLine]).getText();
            s = s.replace("&", Command.SECTIONSIGN() + "");
            tileSign.signText[this.editLine] = new TextComponentString(s);
        }

    }
}
