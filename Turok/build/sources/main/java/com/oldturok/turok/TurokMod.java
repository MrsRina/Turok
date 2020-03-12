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
import org.lwjgl.opengl.Display;

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

// Rina.
@Mod(modid = TurokMod.TUROK_MOD_ID, name = TurokMod.TUROK_MOD_NAME, version = TurokMod.TUROK_MOD_VERSION)
public class TurokMod {
	public static final String TUROK_MOD_ID      = "Turok";
	public static final String TUROK_MOD_NAME    = "\u1d1b\u1d1c\u0280\u0473\u1d0b";
	public static final String TUROK_MOD_VERSION = "0.3.2";

	public static final EventBus EVENT_BUS = new EventManager();

	public static final Logger turok_log = LogManager.getLogger(TUROK_MOD_ID);

	private static String get_bind_file;
	private static String get_folder_tk;

	@Mod.Instance
	private static TurokMod INSTANCE;

	public static int TUROK_GUI_BUTTON = Keyboard.KEY_P;

	public TurokGUI guiManager;
	public ChatManager chatManager;

	@Mod.EventHandler
	public void preInit(FMLInitializationEvent event) {}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		turok_log.info("\n\nTurok Starting");

		// The Turok client is modify base of original client base called KAMI, you can see the README in KAMI official github.
		// Turok want not stolen codes or anything, Turok is a just modify base KAMI, Turok is very modify for dont meet Kami.
		// But with all, I say thanks and VERY thanks for 086.
		// By Rina.

		turok_log.info("\n\nTurok Modules Processing");
		ModuleManager.initialize();

		ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(EVENT_BUS::subscribe);
		MinecraftForge.EVENT_BUS.register(new ForgeEventProcessor());

		LagCompensator.INSTANCE = new LagCompensator();

		Display.setTitle("Turok " + TUROK_MOD_VERSION);

		Wrapper.init();

		guiManager  = new TurokGUI();
		chatManager = new ChatManager();

		turok_log.info("\n\nTurok GUI");
		guiManager.initializeGUI();

		// I know, you dont have.
		Friends.initFriends();
		
		SettingsRegister.register("chatPrefix", Chat.chatPrefix);

		turok_log.info("\n\nTurok Loading Turok/");
		start_load_config();

		ModuleManager.updateLookup();
		ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);

		turok_log.info("\n\nTurok Started");
	}

	public static void start_load_config() {
		try {
			TurokMod.turok_configs();
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
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

	public static void turok_configs() throws IOException {
		get_folder_tk = ("Turok/");
		get_bind_file = ("Configure.json");

		Path turok_config = Paths.get(get_folder_tk + get_bind_file);

		if (!Files.exists(turok_config)) {
			return;
		}

		Configuration.loadConfiguration(turok_config);

		JsonObject turok_gui = TurokMod.INSTANCE.guiStateSetting.getValue();
		for (Map.Entry<String, JsonElement> entry : turok_gui.entrySet()) {
			Optional<Component> optional = TurokMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> ((Frame) component).getTitle().equals(entry.getKey())).findFirst();
			
			if (optional.isPresent()) {
				JsonObject object = entry.getValue().getAsJsonObject();
				Frame frame = (Frame) optional.get();

				frame.setX(object.get("x").getAsInt());
				frame.setY(object.get("y").getAsInt());

				Docking docking = Docking.values()[object.get("docking").getAsInt()];

				if (docking.isLeft()) {
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
				System.err.println("Error in " + entry.getKey() + " no found a frame with name.");
			}
		}

		getInstance().getGuiManager().getChildren().stream().filter(component -> (component instanceof Frame) && (((Frame) component).isPinneable()) && component.isVisible()).forEach(component -> component.setOpacity(0f));
	}

	public static void turok_save_configs() throws IOException {
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

		Path turok_file   = Paths.get(get_folder_tk + get_bind_file);
		Path turok_folder = Paths.get(get_folder_tk);

		if (!Files.exists(turok_file)) {
			Files.createDirectories(turok_folder);
			Files.createFile(turok_file);
		}

		Configuration.saveConfiguration(turok_file);
		ModuleManager.getModules().forEach(Module::destroy);
	}

	public static TurokMod getInstance() {
		return INSTANCE;
	}

	public TurokGUI getGuiManager() {
		return guiManager;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}
}