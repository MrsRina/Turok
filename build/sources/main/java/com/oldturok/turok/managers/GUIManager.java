package com.oldturok.turok.managers;

import com.oldturok.turok.Turok;
import com.oldturok.turok.modules.api.Module;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;

public class GUIManager extends GuiScreen {
    private final float scaleFactor = 1.5f;
    private final int x_pos = 2;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
        drawRect(x_pos, 2, 80, mc.fontRenderer.FONT_HEIGHT * 2, new Color(128, 128, 128).getRGB());
        mc.fontRenderer.drawString("Hacks", 25, (4 + mc.fontRenderer.FONT_HEIGHT) / 2, new Color(255, 255, 255).getRGB());
        GlStateManager.scale(1 / scaleFactor, 1 / scaleFactor, 1 / scaleFactor);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        for (int i = 0; i < getModuleList().size(); i++) {
            if (getModuleList().get(i).name != "ClickGui") {
                buttonList.add(new GuiButton(i, x_pos, (12 + mc.fontRenderer.FONT_HEIGHT * 2) + (20 * i), 119, mc.fontRenderer.FONT_HEIGHT * 2, getModuleList().get(i).name));
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        for (int i = 0; i < getModuleList().size(); i++) {
            if (button.id == i) {
                getModuleList().get(i).Toggle();
            }
        }
    }

    private ArrayList<Module> getModuleList() {
        return Turok.client.moduleManager.moduleList;
    }
}
