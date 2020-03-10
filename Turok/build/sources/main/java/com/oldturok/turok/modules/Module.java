package com.oldturok.turok.module;

import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.util.Bind;
import com.oldturok.turok.TurokMod;

import net.minecraft.client.Minecraft;

import com.google.common.base.Converter;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

public class Module {
    private final String originalName = getAnnotation().name();
    private final Setting<String> name = register(Settings.s("Name", originalName));
    private final String description = getAnnotation().description();
    private final Category category = getAnnotation().category();
    private Setting<Bind> bind = register(Settings.custom("Bind", Bind.none(), new BindConverter()).build());
    private Setting<Boolean> enabled = register(Settings.booleanBuilder("Enabled").withVisibility(aBoolean -> false).withValue(false).build());
    public boolean alwaysListening;
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public List<Setting> settingList = new ArrayList<>();

    public Module() {
        alwaysListening = getAnnotation().alwaysListening();
        registerAll(bind, enabled);
    }

    private Info getAnnotation() {
        if (getClass().isAnnotationPresent(Info.class)) {
            return getClass().getAnnotation(Info.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public void onUpdate() {}
    public void onRender() {}
    public void onWorldRender(RenderEvent event) {}

    public Bind getBind() {
        return bind.getValue();
    }

    public String getBindName() {
        return bind.getValue().toString();
    }

    public void setName(String name) {
        this.name.setValue(name);
        ModuleManager.updateLookup();
    }

    public String getOriginalName() {
        return originalName;
    }

    public enum Category
    {
        TUROK_COMBAT("TurokCombat", false),
        TUROK_MISC("TurokMisc", false),
        TUROK_MOVEMENT("TurokMovement", false),
        TUROK_PLAYER("TurokPlayer", false),
        TUROK_RENDER("TurokRender", false),
        TUROK_CHAT("TurokChat", false),
        TUROK_HIDDEN("null", true);

        boolean hidden;
        String name;

        Category(String name, boolean hidden) {
            this.name = name;
            this.hidden = hidden;
        }

        public boolean isHidden() {
            return hidden;
        }

        public String getName() {
            return name;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info
    {
        String name();
        String description() default "Descriptionless";
        Module.Category category();
        boolean alwaysListening() default false;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public void enable() {
        enabled.setValue(true);
        onEnable();
        if (!alwaysListening)
            TurokMod.EVENT_BUS.subscribe(this);
    }

    public void disable() {
        enabled.setValue(false);
        onDisable();
        if (!alwaysListening)
            TurokMod.EVENT_BUS.unsubscribe(this);
    }

    public boolean isDisabled() {
        return !isEnabled();
    }

    public void setEnabled(boolean enabled) {
        boolean prev = this.enabled.getValue();
        if (prev != enabled)
            if (enabled)
                enable();
            else
                disable();
    }

    public String getHudInfo() {
        return null;
    }

    protected final void setAlwaysListening(boolean alwaysListening) {
        this.alwaysListening = alwaysListening;
        if (alwaysListening) TurokMod.EVENT_BUS.subscribe(this);
        if (!alwaysListening && isDisabled()) TurokMod.EVENT_BUS.unsubscribe(this);
    }
    public void destroy(){};

    protected void registerAll(Setting... settings) {
        for (Setting setting : settings) {
            register(setting);
        }
    }

    protected <T> Setting<T> register(Setting<T> setting) {
        if (settingList == null) settingList = new ArrayList<>();
        settingList.add(setting);
        return SettingBuilder.register(setting, "modules." + originalName);
    }

    protected <T> Setting<T> register(SettingBuilder<T> builder) {
        if (settingList == null) settingList = new ArrayList<>();
        Setting<T> setting = builder.buildAndRegister("modules." + name);
        settingList.add(setting);
        return setting;
    }


    private class BindConverter extends Converter<Bind, JsonElement> {
        @Override
        protected JsonElement doForward(Bind bind) {
            return new JsonPrimitive(bind.toString());
        }

        @Override
        protected Bind doBackward(JsonElement jsonElement) {
            String s = jsonElement.getAsString();
            if (s.equalsIgnoreCase("None")) return Bind.none();
            boolean ctrl = false, alt = false, shift = false;

            int key = -1;
            try {
                key = Keyboard.getKeyIndex(s.toUpperCase());
            } catch (Exception ignored) {}

            if (key == 0) return Bind.none();
            return new Bind(ctrl, alt, shift, key);
        }
    }
}
