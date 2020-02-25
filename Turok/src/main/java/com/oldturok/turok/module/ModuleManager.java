package com.oldturok.turok.module;

import com.oldturok.turok.TurokMod;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.modules.ClickGUI;
import com.oldturok.turok.util.ClassFinder;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class ModuleManager {

    public static ArrayList<Module> modules = new ArrayList<>();

    static HashMap<String, Module> lookup = new HashMap<>();

    public static void updateLookup() {
        lookup.clear();
        for (Module m : modules)
            lookup.put(m.getName().toLowerCase(), m);
    }

    public static void initialize() {
        Set<Class> classList = ClassFinder.findClasses(ClickGUI.class.getPackage().getName(), Module.class);
        classList.forEach(aClass -> {
            try {
                Module module = (Module) aClass.getConstructor().newInstance();
                modules.add(module);
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        TurokMod.log.info("Modules initialised");
        getModules().sort(Comparator.comparing(Module::getName));
    }

    public static void onUpdate() {
        modules.stream().filter(module -> module.alwaysListening || module.isEnabled()).forEach(module -> module.onUpdate());
    }

    public static void onRender() {
        modules.stream().filter(module -> module.alwaysListening || module.isEnabled()).forEach(module -> module.onRender());
    }

    public static void onWorldRender(RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("turok");

        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        Vec3d renderPos = EntityUtil.getInterpolatedPos(Wrapper.getPlayer(), event.getPartialTicks());

        RenderEvent e = new RenderEvent(TurokTessellator.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();

        modules.stream().filter(module -> module.alwaysListening || module.isEnabled()).forEach(module -> {
            Minecraft.getMinecraft().profiler.startSection(module.getName());
            module.onWorldRender(e);
            Minecraft.getMinecraft().profiler.endSection();
        });

        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
//        GlStateManager.popMatrix();
        TurokTessellator.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();

        Minecraft.getMinecraft().profiler.endSection();
    }

    public static void onBind(int eventKey) {
        if (eventKey == 0) return;
        modules.forEach(module -> {
            if (module.getBind().isDown(eventKey)) {
                module.toggle();
            }
        });
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }

    public static Module getModuleByName(String name) {
        return lookup.get(name.toLowerCase());
    }

    public static boolean isModuleEnabled(String moduleName) {
        Module m = getModuleByName(moduleName);
        if (m == null) return false;
        return m.isEnabled();
    }
}
