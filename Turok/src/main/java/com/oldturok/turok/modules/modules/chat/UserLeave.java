package com.oldturok.turok.module.modules.chat;

import com.oldturok.turok.module.Module;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.net.URL;

@Module.Info(name = "IRC", category = Module.Category.TUROK_CHAT)
public class UserLeave extends Module {
	public final int TUROK_IRC_URL = "https://raw.githack.com/SirRina/Turok/master/tsocial_save.json";

	public Users_On[] users_on;

	public class Users_On {
		public String uuid;
	}

	@Override
	public onEnable() {
		HttpsURLConnection connection = (HttpsURLConnection) new URL(TUROK_IRC_URL).openConnection();

		connection.connect();

		this.users_on = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), CustomUser[].class);
		connection.disconnect();
	}
}