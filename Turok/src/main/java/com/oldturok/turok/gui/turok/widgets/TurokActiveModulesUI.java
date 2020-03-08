package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.command.Command;
import com.oldturok.turok.gui.turok.component.ActiveModules;
import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RootFontRenderer;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.Wrapper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;

public class TurokActiveModulesUI extends AbstractComponentUI<ActiveModules> {
    @Override
    public void renderComponent(ActiveModules component, FontRenderer f) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        RootFontRenderer renderer = new RootFontRenderer(1.0f);
        List<Module> mods = ModuleManager.getModules().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> renderer.getStringWidth(module.getName()+(module.getHudInfo()==null?"":module.getHudInfo()+" "))*(component.sort_up?-1:1)))
                .collect(Collectors.toList());

        final int[] y = {2};

        if (component.getParent().getY() < 26 && Wrapper.getPlayer().getActivePotionEffects().size()>0 && component.getParent().getOpacity() == 0)
            y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());

        final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};

        boolean lAlign = component.getAlignment() == AlignedComponent.Alignment.LEFT;
        Function<Integer, Integer> xFunc;
        switch (component.getAlignment()) {
            case RIGHT:
                xFunc = i -> component.getWidth() - i;
                break;
            case CENTER:
                xFunc = i -> component.getWidth() / 2 - i / 2;
                break;
            case LEFT:
            default:
                xFunc = i -> 0;
                break;
        }

        mods.stream().forEach(module -> {
            int rgb = Color.HSBtoRGB(hue[0], 1, 1);
            String s = module.getHudInfo();
            String text = module.getName() + (s == null?"" : " " + Command.SECTIONSIGN() + "7" + s);
            int textwidth = renderer.getStringWidth(text);
            int textheight = renderer.getFontHeight()+1;
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            renderer.drawStringWithShadow(xFunc.apply(textwidth), y[0], 255, 255, 255, text);
            hue[0] +=.02f;
            y[0] += textheight;
        });

        component.setHeight(y[0]);

        GL11.glEnable(GL11.GL_CULL_FACE);
        glDisable(GL_BLEND);
    }

    @Override
    public void handleSizeComponent(ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }
}
