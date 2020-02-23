package com.oldturok.turok.modules.api;

import com.oldturok.turok.Turok;
import com.oldturok.turok.helpers.MultiColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Module {
    public String name;
    public KeyBinding module;
    private boolean state;

    private MultiColor multiColor;
    private float scaleFactor = 1.2f;
    protected Minecraft minecraft = Minecraft.getMinecraft();

    public Module(String name, int keyCode) {
        module = new KeyBinding(name, keyCode, "");
        ClientRegistry.registerKeyBinding(module);

        this.name = name;
        getModuleName();
    }

    public void onGui(int offset) {
        multiColor = MultiColor.getRainbow();
        Turok.client.uiManager.uiPosition.positionText(Turok.client.uiManager.position_on_screen, getModuleName(), 2, 8, 1.2f);
        Turok.client.uiManager.uiPosition.GLScale(scaleFactor);
        minecraft.fontRenderer.drawStringWithShadow(getModuleName(), Turok.client.uiManager.uiPosition.x_position, (Turok.client.uiManager.uiPosition.y_position + 10) + (offset * 10), multiColor.getRGB());
        Turok.client.uiManager.uiPosition.GLScale( 1 / scaleFactor);
    }

    public void onToggle(boolean state){}
    public void onUpdate(){}
    public void onRender(){}
    public void onKey(){
        if (module.isKeyDown()) {
            Toggle();
        }
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state){
        this.state = state;
    }

    public void Toggle() {
        setState(!getState());
        onToggle(getState());
    }

    private String getModuleName() {
        return name + " " + "[" + Keyboard.getKeyName(module.getKeyCode()) + "]";
    }
}
