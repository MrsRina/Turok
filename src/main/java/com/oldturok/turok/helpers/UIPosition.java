package com.oldturok.turok.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class UIPosition {
    public float x_position;
    public float y_position;

    private Minecraft mc = Minecraft.getMinecraft();

    public void positionText(POSITION_ON_SCREEN position_on_screen, String text, float offset_x, float offset_y, float scale) {
        switch (position_on_screen) {
            case TOP_LEFT:
                x_position = (0 + offset_x) / scale;
                y_position = (0 + offset_y) / scale;
                break;
            case TOP_RIGHT:
                x_position = (((mc.displayWidth / 2) - (mc.fontRenderer.getStringWidth(text) * scale)) - offset_x) / scale;
                y_position = (0 + offset_y) / scale;
                break;
            case BOTTOM_LEFT:
                x_position = (0 + offset_x) / scale;
                y_position = (((mc.displayHeight / 2) - (mc.fontRenderer.FONT_HEIGHT * scale)) - offset_y) / scale;
                break;
            case BOTTOM_RIGHT:
                x_position = (((mc.displayWidth / 2) - (mc.fontRenderer.getStringWidth(text) * scale)) - offset_x) / scale;
                y_position = (((mc.displayHeight / 2) - (mc.fontRenderer.FONT_HEIGHT * scale)) - offset_y) / scale;
                break;
        }
    }

    public void GLScale(float scaleFactor) {
        GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    }

    public enum POSITION_ON_SCREEN {
        TOP_RIGHT,
        TOP_LEFT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }
}
