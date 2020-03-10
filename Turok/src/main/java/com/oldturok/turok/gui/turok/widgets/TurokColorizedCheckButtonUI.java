package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.turok.component.ColorizedCheckButton;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RootSmallFontRenderer;
import com.oldturok.turok.gui.turok.TurokGUI;

import java.awt.*;

import org.lwjgl.opengl.GL11;

public class TurokColorizedCheckButtonUI extends TurokCheckButtonUI <ColorizedCheckButton> {

    RootSmallFontRenderer ff = new RootSmallFontRenderer();

    public TurokColorizedCheckButtonUI() {
        backgroundColour = new Color(200, backgroundColour.getGreen(), backgroundColour.getBlue());
        backgroundColourHover = new Color(255, backgroundColourHover.getGreen(), backgroundColourHover.getBlue());
        downColourNormal = new Color(190, 190, 190);
    }

    @Override
    public void renderComponent(CheckButton component, FontRenderer aa) {
        GL11.glColor4f(backgroundColour.getRed()/255f, backgroundColour.getGreen()/255f, backgroundColour.getBlue()/255f, component.getOpacity());
        if (component.isHovered() || component.isPressed()){
            GL11.glColor4f(backgroundColourHover.getRed()/255f, backgroundColourHover.getGreen()/255f, backgroundColourHover.getBlue()/255f, component.getOpacity());
        }
        if (component.isToggled()){
            GL11.glColor3f(backgroundColour.getRed()/255f, backgroundColour.getGreen()/255f, backgroundColour.getBlue()/255f);
        }

//        RenderHelper.drawRoundedRectangle(0,0,component.getWidth(), component.getHeight(), 3f);
        GL11.glLineWidth(2.5f);
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2d(0,component.getHeight());
            GL11.glVertex2d(component.getWidth(),component.getHeight());
        }
        GL11.glEnd();

        Color idleColour = component.isToggled() ? idleColourToggle : idleColourNormal;
        Color downColour = component.isToggled() ? downColourToggle : downColourNormal;

        GL11.glColor3f(1,1,1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        ff.drawString(component.getWidth() / 2 - TurokGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? downColour : idleColour, component.getName());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }
}
