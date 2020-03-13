package com.oldturok.turok.gui.turok;

import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokMod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

import com.oldturok.turok.util.TurokGL; // TurokGL.

// Rina.
public class DisplayHudScreen extends GuiScreen {
    TurokHUD hud;

    public final GuiScreen lastScreen;

    public static int mouseX;
    public static int mouseY;

    Framebuffer framebuffer;

    public DisplayHudScreen(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;

        TurokHUD hud = TurokMod.getInstance().getHudManager();

        for (Component c : hud.getChildren()){
            if (c instanceof Frame){
                Frame child = (Frame) c;
                if (child.isPinneable() && child.isVisible()){
                    child.setOpacity(.5f);
                }
            }
        }

        framebuffer = new Framebuffer(Wrapper.getMinecraft().displayWidth, Wrapper.getMinecraft().displayHeight, false);
    }

    @Override
    public void onGuiClosed() {
        TurokHUD hud = TurokMod.getInstance().getHudManager();

        hud.getChildren().stream().filter(component -> (component instanceof Frame) && (((Frame) component).isPinneable()) && component.isVisible()).forEach(component -> component.setOpacity(0f));
    }

    @Override
    public void initGui() {
        hud = TurokMod.getInstance().getHudManager();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        calculateMouse();

		TurokGL.refresh_color(190, 190, 190, 50);
		RenderHelper.drawFilledRectangle(0, 0, Wrapper.getMinecraft().displayHeight, Wrapper.getMinecraft().displayWidth);

        hud.drawGUI();
        
        glEnable(GL_TEXTURE_2D);
        GlStateManager.color(1, 1, 1);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        hud.handleMouseDown(this.mouseX, this.mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        hud.handleMouseRelease(this.mouseX, this.mouseY);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        hud.handleMouseDrag(this.mouseX, this.mouseY);
    }

    @Override
    public void updateScreen() {
        if (Mouse.hasWheel()){
            int a = Mouse.getDWheel();
            if (a != 0){
                hud.handleWheel(this.mouseX, this.mouseY, a);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(lastScreen);
        else{
            hud.handleKeyDown(keyCode);
            hud.handleKeyUp(keyCode);
        }
    }

    public static int getScale(){
        int scale = Wrapper.getMinecraft().gameSettings.guiScale;
        if(scale == 0)
            scale = 1000;
        int scaleFactor = 0;
        while(scaleFactor < scale && Wrapper.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Wrapper.getMinecraft().displayHeight / (scaleFactor + 1) >= 240)
            scaleFactor++;
        if (scaleFactor == 0)
            scaleFactor = 1;
        return scaleFactor;
    }

    private void calculateMouse() {
        Minecraft minecraft = Minecraft.getMinecraft();
        int scaleFactor = getScale();
        this.mouseX = Mouse.getX() / scaleFactor;
        this.mouseY =  minecraft.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1;
    }

}
