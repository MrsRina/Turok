package com.oldturok.turok.managers;

import com.oldturok.turok.Turok;
import com.oldturok.turok.helpers.MultiColor;
import com.oldturok.turok.helpers.UIPosition;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.Random;

public class UIManager {
    private final Random random = new Random();
    private Minecraft minecraft;
    public UIPosition uiPosition;

    private MultiColor multiColor;

    public UIPosition.POSITION_ON_SCREEN position_on_screen = UIPosition.POSITION_ON_SCREEN.TOP_LEFT;
    public float scaleFactor = 1.5f;

    public UIManager() {
        minecraft = Minecraft.getMinecraft();
        uiPosition = new UIPosition();
    }

    public void Draw() {
        multiColor = MultiColor.getRainbow();
        uiPosition.positionText(position_on_screen, Turok.client.CLIENT_NAME, 2, 2, scaleFactor);
        uiPosition.GLScale(scaleFactor);
        minecraft.fontRenderer.drawStringWithShadow(Turok.client.CLIENT_NAME, uiPosition.x_position, uiPosition.y_position, multiColor.getRGB());
        uiPosition.GLScale(1 / scaleFactor);
    }
}