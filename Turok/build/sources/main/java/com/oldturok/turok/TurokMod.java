package com.oldturok.turok;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.setting.config.Configuration;
import com.oldturok.turok.event.ForgeEventProcessor;
import com.oldturok.turok.setting.SettingsRegister;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.LagCompensator;
import com.oldturok.turok.chatcmd.ChatManager;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.chatcmd.Chat;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.util.Friends;

import com.google.common.base.Converter;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.zero.alpine.EventManager;
import me.zero.alpine.EventBus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Arrays;
import java.util.Map;
import java.io.*;

// Rina main module.
// Modify in 05/03/20.
@Mod(modid = TurokMod.MODID, name = TurokMod.MODNAME, version = TurokMod.MODVER)
public class TurokMod {

    public static final String MODID = "turok";
    public static final String MODNAME = "\u1d1b\u1d1c\u0280\u0473\u1d0b";
    public static final String MODVER = "0.3.1";

    public static final int TUROK_GUI_BUTTON = Keyboard.KEY_P;

    private static final String TUROK_CONFIG_NAME_DEFAULT = "Turok.json";

    public static final Logger log = LogManager.getLogger("Turok");

    public static final EventBus EVENT_BUS = new EventManager();

    @Mod.Instance
    private static TurokMod INSTANCE;

    public TurokGUI guiManager;
    public CommandManager commandManager;
    private Setting<JsonObject> guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter<JsonObject, JsonObject>() {
        @Override
        protected JsonObject doForward(JsonObject jsonObject) {
            return jsonObject;
        }

        @Override
        protected JsonObject doBackward(JsonObject jsonObject) {
            return jsonObject;
        }
    }).buildAndRegister("");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        TurokMod.log.info("\n\nInitializing Turok " + MODVER);

        ModuleManager.initialize();

        ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(EVENT_BUS::subscribe);
        MinecraftForge.EVENT_BUS.register(new ForgeEventProcessor());
        LagCompensator.INSTANCE = new LagCompensator();

        Wrapper.init();

        guiManager = new TurokGUI();
        guiManager.initializeGUI();

        commandManager = new CommandManager();

        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        TurokMod.log.info("Settings loaded");

        ModuleManager.updateLookup();
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);

        TurokMod.log.info("Turok Mod initialized!\n");
    }

    public static String getConfigName() {
        Path config = Paths.get("TurokCache_.txt");
        String turokConfigName = TUROK_CONFIG_NAME_DEFAULT;
        try(BufferedReader reader = Files.newBufferedReader(config)) {
            turokConfigName = reader.readLine();
            if (!isFilenameValid(turokConfigName)) turokConfigName = TUROK_CONFIG_NAME_DEFAULT;
        } catch (NoSuchFileException e) {
            try(BufferedWriter writer = Files.newBufferedWriter(config)) {
                writer.write(TUROK_CONFIG_NAME_DEFAULT);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return turokConfigName;
    }

    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigurationUnsafe() throws IOException {
        String turokConfigName = getConfigName();
        Path turokConfig = Paths.get(turokConfigName);
        if (!Files.exists(turokConfig)) return;
        Configuration.loadConfiguration(turokConfig);

        JsonObject gui = TurokMod.INSTANCE.guiStateSetting.getValue();
        for (Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            Optional<Component> optional = TurokMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> ((Frame) component).getTitle().equals(entry.getKey())).findFirst();
            if (optional.isPresent()) {
                JsonObject object = entry.getValue().getAsJsonObject();
                Frame frame = (Frame) optional.get();
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
                Docking docking = Docking.values()[object.get("docking").getAsInt()];
                if (docking.isLeft()) ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                else if (docking.isRight()) ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                else if (docking.isCenterVertical()) ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            } else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }
        TurokMod.getInstance().getGuiManager().getChildren().stream().filter(component -> (component instanceof Frame) && (((Frame) component).isPinneable()) && component.isVisible()).forEach(component -> component.setOpacity(0f));
    }

    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfigurationUnsafe() throws IOException {
        JsonObject object = new JsonObject();
        TurokMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> (Frame) component).forEach(frame -> {
            JsonObject frameObject = new JsonObject();
            frameObject.add("x", new JsonPrimitive(frame.getX()));
            frameObject.add("y", new JsonPrimitive(frame.getY()));
            frameObject.add("docking", new JsonPrimitive(Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
            frameObject.add("minimized", new JsonPrimitive(frame.isMinimized()));
            frameObject.add("pinned", new JsonPrimitive(frame.isPinned()));
            object.add(frame.getTitle(), frameObject);
        });
        TurokMod.INSTANCE.guiStateSetting.setValue(object);

        Path outputFile = Paths.get(getConfigName());
        if (!Files.exists(outputFile))
            Files.createFile(outputFile);
        Configuration.saveConfiguration(outputFile);
        ModuleManager.getModules().forEach(Module::destroy);
    }

    public static boolean isFilenameValid(String file) {
        File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static TurokMod getInstance() {
        return INSTANCE;
    }

    public TurokGUI getGuiManager() {
        return guiManager;
    }

    public ChatManager getCommandManager() {
        return commandManager;
    }
}