package com.oldturok.turok;

import com.oldturok.turok.setting.converter.Convertable;
import com.oldturok.turok.setting.SettingsRegister;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.TurokMod;
import com.google.gson.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.io.*;

public class TurokConfig {
	public static JsonObject load(Boolean type) {
		if (type) {
			return create_config_frames(SettingsRegister.ROOT);
		} else {
			return create_config_binds(SettingsRegister.ROOT);
		}
	}

	public static JsonObject create_config_frames(SettingsRegister setting) {
		JsonObject object = new JsonObject();

		for (Map.Entry<String, Setting> entry : setting.settingHashMap.entrySet()) {
			Setting setting_register = entry.getValue();

			if (!(setting_register instanceof Convertable)) {
				continue;
			}

			if (setting_register.getName().equals("frames")) {
				object.add(entry.getKey(), (JsonElement) ((Convertable) setting_register).converter().convert(setting_register.getValue()));
			}
		}

		return object;
	}

	public static JsonObject create_config_binds(SettingsRegister setting) {
		JsonObject object = new JsonObject();

		for (Map.Entry<String, SettingsRegister> entry : setting.registerHashMap.entrySet()) {
			object.add(entry.getKey(), create_config_binds(entry.getValue()));
		}

		for (Map.Entry<String, Setting> entry : setting.settingHashMap.entrySet()) {
			Setting setting_register = entry.getValue();

			if (!(setting_register instanceof Convertable)) {
				continue;
			}

			object.add(entry.getKey(), (JsonElement) ((Convertable) setting_register).converter().convert(setting_register.getValue()));

			if (setting_register.getName().equals("frames")) {
				object.remove(entry.getKey());
			}
		}

		return object;
	}

	public static void start_save(OutputStream stream, boolean type) throws IOException {
		Gson gson_gson = new GsonBuilder().setPrettyPrinting().create();

		String string_json = "";

		if (type) {
			string_json = gson_gson.toJson(load(type));
		} else {
			string_json = gson_gson.toJson(load(type));
		}

		BufferedWriter buffer_file = new BufferedWriter(new OutputStreamWriter(stream));

		buffer_file.write(string_json);
		buffer_file.close();
	}

	public static void save_frames(Path path, boolean type) throws IOException {
		start_save(Files.newOutputStream(path), type);
	}

	public static void save_binds(Path path, boolean type) throws IOException {
		start_save(Files.newOutputStream(path), type);
	}

	public static void load_frames(Path path) throws IOException {
		InputStream stream = Files.newInputStream(path);

		load_config_frames(stream);

		stream.close();
	}

	public static void load_binds(Path path) throws IOException {
		InputStream stream = Files.newInputStream(path);

		load_config_binds(stream);

		stream.close();
	}

	public static void load_config_frames(InputStream stream) {
		try {
			start_load(new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject());
		} catch (IllegalStateException exc) {
			TurokMod.turok_log.error("Failed to load the frames file. Try delet it.");

			start_load(new JsonObject());
		}
	}

	public static void load_config_binds(InputStream stream) {
		try {
			start_load(new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject());
		} catch (IllegalStateException exc) {
			TurokMod.turok_log.error("Failed to load the binds file. Try delet it.");

			start_load(new JsonObject());
		}
	}

	public static void start_load(JsonObject json) {
		load_configs(SettingsRegister.ROOT, json);
	}

	public static void load_configs(SettingsRegister setting, JsonObject json) {
		for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
			String key = entry.getKey();

			JsonElement element = entry.getValue();

			if (setting.registerHashMap.containsKey(key)) {
				load_configs(setting.subregister(key), element.getAsJsonObject());
			} else {
				Setting setting_register = setting.getSetting(key);

				if (setting_register == null) {
					continue;
				}

				setting_register.setValue(((Convertable) setting_register).converter().reverse().convert(element));
			}
		}
	}
}