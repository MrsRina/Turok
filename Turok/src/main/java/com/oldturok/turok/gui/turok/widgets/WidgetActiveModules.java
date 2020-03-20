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

import org.lwjgl.opengl.GL11;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;

public class WidgetActiveModules extends AbstractComponentUI<ActiveModules> {
    public TurokHUD get_hud = (TurokHUD) ModuleManager.getModuleByName("TurokHUD");

    @Override
    public void renderComponent(ActiveModules component, FontRenderer f) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        RootFontRenderer renderer = new RootFontRenderer(1.0f);

        float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};

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
            String module_name = module.getName() + (module_info == null? "" : " - " + module_info);
            
            int module_width  = renderer.getStringWidth(module_name);
            int module_height = renderer.getFontHeight() + 1;

            int color_rgb      = Color.HSBtoRGB(tick_color[0], 1, 1);

            if (get_hud.array_rgb.getValue()) {
                renderer.drawStringWithShadow(xFunc.apply(module_width), module_y[0], ((color_rgb >> 16) & 0xFF), ((color_rgb >> 8) & 0xFF), (color_rgb & 0xFF), module_name);
            } else {
                renderer.drawStringWithShadow(xFunc.apply(module_width), module_y[0], get_hud.array_r.getValue(), get_hud.array_g.getValue(), get_hud.array_b.getValue(), module_name);
            }

            module_y[0]   += module_height;
            tick_color[0] += 0.1f;
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
