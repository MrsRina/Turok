package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.turok.component.ActiveModules;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.module.modules.render.TurokHUD;
import com.oldturok.turok.gui.turok.RootFontRenderer;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.Wrapper;

import com.mojang.realmsclient.gui.ChatFormatting;

import org.lwjgl.opengl.GL11;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;

public class WidgetActiveModules extends AbstractComponentUI<ActiveModules> {
    public static int r = 255;
    public static int g = 255;
    public static int b = 255;

    @Override
    public void renderComponent(ActiveModules component, FontRenderer f) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        RootFontRenderer renderer = new RootFontRenderer(1.0f);

        List<Module> mods = ModuleManager.getModules().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> renderer.getStringWidth(module.getName() + (module.getHudInfo() == null ? "" : module.getHudInfo() + " "))))
                .collect(Collectors.toList());

        final int[] module_y = {2};

        if (component.getParent().getY() < 26 && Wrapper.getPlayer().getActivePotionEffects().size() > 0 && component.getParent().getOpacity() == 0)
            module_y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());

        boolean lAlign = component.getAlignment() == AlignedComponent.Alignment.LEFT;

        Function<Integer, Integer> xFunc;

        switch (component.getAlignment()) {
            case RIGHT:
                xFunc = i -> component.getWidth() - i;
                break;

            case LEFT:
            default:
                xFunc = i -> 0;
                break;
        }

        mods.stream().forEach(module -> {
            String module_info = module.getHudInfo();
            String module_name = module.getName() + (module_info == null? "" : " " + ChatFormatting.GRAY + module_info);
            
            int module_width  = renderer.getStringWidth(module_name);
            int module_height = renderer.getFontHeight() + 1;

            renderer.drawStringWithShadow(xFunc.apply(module_width), module_y[0], r, g, b, module_name);

            module_y[0]   += module_height;
        });

        component.setHeight(module_y[0]);

        GL11.glEnable(GL11.GL_CULL_FACE);
        glDisable(GL_BLEND);
    }

    @Override
    public void handleSizeComponent(ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }
}
