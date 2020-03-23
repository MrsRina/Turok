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
import com.oldturok.turok.util.TurokChatManager;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.TurokIRCManager;
import com.oldturok.turok.util.LagCompensator;
import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.util.Friends;
import com.oldturok.TurokRPC;

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

// New modfile.
// Sorry for any thing.
@Mod(modid = "turok", name = "\u1d1b\u1d1c\u0280\u0473\u1d0b", version = TurokMod.TUROK_MOD_VERSION)
public class TurokMod {
    public static final String TUROK_MOD_ID      = "turok";
    public static final String TUROK_MOD_NAME    = "\u1d1b\u1d1c\u0280\u0473\u1d0b";
    public static final String TUROK_MOD_VERSION = "0.4.1";

    public static final int TUROK_GUI_BUTTON  = Keyboard.KEY_P;

    public static String TUROK_CHAT_PREFIX = "-";

    private static final String TUROK_CONFIG_NAME_DEFAULT = "Turok.json";
    private static final String TUROK_FOLDER_NAME_DEFAULT = "Turok/";

    public static final Logger turok_log = LogManager.getLogger("turok");

    public static final EventBus EVENT_BUS = new EventManager();

    public static TurokIRCManager chat_irc;
    public TurokChatManager turok_chat_manager;
    public TurokGUI guiManager;

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

    @Mod.Instance
    private static TurokMod INSTANCE;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	//
		// The Turok client is modify base of original client base called KAMI, you can see the README in KAMI official github.
		// Turok want not stolen codes or anything, Turok is a just modify base KAMI, Turok is very modify for dont meet Kami.
		// But with all, I say thanks and VERY thanks for 086.
		// By Rina.
		//

        TurokMod.turok_log.info("\n\nTurok Started.");

        ModuleManager.initialize();

        ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(EVENT_BUS::subscribe);
        
        MinecraftForge.EVENT_BUS.register(new ForgeEventProcessor());
        
        LagCompensator.INSTANCE = new LagCompensator();

        Wrapper.init();

        guiManager = new TurokGUI();
        guiManager.initializeGUI();

        turok_chat_manager = new TurokChatManager();

        Friends.initFriends();

        load_config();

        TurokMod.turok_log.info("Maping binds.");

        ModuleManager.updateLookup();
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);

        TurokRPC.start();

        ModuleManager.getModuleByName("TurokHUD").enable();

        TurokMod.turok_log.info("Welcome to Turok.");
    }

    public static void load_config() {
        try {
            load_configs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load_configs() throws IOException {
        String turok_config_name_ = TUROK_FOLDER_NAME_DEFAULT + TUROK_CONFIG_NAME_DEFAULT;
        Path turok_config         = Paths.get(turok_config_name_);
        
        Path folder_bind = Paths.get(TUROK_FOLDER_NAME_DEFAULT);

        if (!Files.exists(folder_bind)) {
            Files.createDirectories(folder_bind);
        }

        Path file_bind = Paths.get(TUROK_FOLDER_NAME_DEFAULT + TUROK_CONFIG_NAME_DEFAULT);

        if (!Files.exists(file_bind))
            Files.createFile(file_bind);
        
        Configuration.loadConfiguration(turok_config);

        JsonObject gui = TurokMod.INSTANCE.guiStateSetting.getValue();
        for (Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            Optional<Component> optional = TurokMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> ((Frame) component).getTitle().equals(entry.getKey())).findFirst();
            
            if (optional.isPresent()) {
                JsonObject object = entry.getValue().getAsJsonObject();
                Frame frame       = (Frame) optional.get();
               
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
               
                Docking docking = Docking.values()[object.get("docking").getAsInt()];
                
                if (docking.isLeft()){
                	ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);

                } else if (docking.isRight()) {
                	ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);

                } else if (docking.isCenterVertical()) {
                	ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                }

                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            } else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }

        TurokMod.getInstance().getGuiManager().getChildren().stream().filter(component -> (component instanceof Frame) && (((Frame) component).isPinneable()) && component.isVisible()).forEach(component -> component.setOpacity(0f));
    }

    public static void save_config() {
        try {
            save_unsafe();
        }catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static void save_unsafe() throws IOException {
        JsonObject object = new JsonObject();
        TurokMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> (Frame) component).forEach(frame -> {
            JsonObject frame_object = new JsonObject();

            frame_object.add("x", new JsonPrimitive(frame.getX()));
            frame_object.add("y", new JsonPrimitive(frame.getY()));
            frame_object.add("docking", new JsonPrimitive(Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
            frame_object.add("minimized", new JsonPrimitive(frame.isMinimized()));
            frame_object.add("pinned", new JsonPrimitive(frame.isPinned()));

            object.add(frame.getTitle(), frame_object);
        });

        TurokMod.INSTANCE.guiStateSetting.setValue(object);

        Path file_bind = Paths.get(TUROK_FOLDER_NAME_DEFAULT + TUROK_CONFIG_NAME_DEFAULT);

        Configuration.saveConfiguration(file_bind);
        ModuleManager.getModules().forEach(Module::destroy);
    }

    public static boolean name_valid(String file_) {
        File file = new File(file_);
        try {
            file.getCanonicalPath();

            return true;
        } catch (IOException exc) {
            return false;
        }
    }

    public static TurokMod getInstance() {
        return INSTANCE;
    }

    public TurokGUI getGuiManager() {
        return guiManager;
    }
}