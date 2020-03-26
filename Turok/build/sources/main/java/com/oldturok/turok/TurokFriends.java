package com.oldturok.turok;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.google.common.base.Converter;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.UUID;
import java.net.URL;

public class TurokFriends {
	public static final TurokFriends INSTANC = new TurokFriends();

	public static Setting<ArrayList<Friend>> list_friends;

	public TurokFriends() {}

	public static void init_friends_list() {
		list_friends = Settings.custom("Friends", new ArrayList<Friend>(), new FriendListConverter()).buildAndRegister("friends");
	}

	public static Boolean is_friend(String name) {
		return list_friends.getValue().stream().anyMatch(friend -> friend.user_name.equalsIgnoreCase(name));
	}

	public static class FriendListConverter extends Converter<ArrayList<Friend>, JsonElement> {
		public FriendListConverter() {}

		@Override
		protected JsonElement doForward(ArrayList<Friend> list) {
			StringBuilder present = new StringBuilder();

			for (Friend friend : list) {
				present.append(String.format("%s;%s$", friend.user_name, friend.uuid.toString()));
			}

			return new JsonPrimitive(present.toString());
		}

		@Override
		protected ArrayList<Friend> doBackward(JsonElement jsonElement) {
			String string  = jsonElement.getAsString();
			String[] pairs = string.split(Pattern.quote("$"));

			ArrayList<Friend> friends = new ArrayList<>();

			for (String pair : pairs) {
				try {
					String[] split   = pair.split(";");
					String user_name = split[0];

					UUID uuid = UUID.fromString(split[1]);
					friends.add(new Friend(user_name_uuid(uuid, user_name), uuid));
				} catch (Exception exc) {}
			}

			return friends;
		}

		public static String user_name_uuid(UUID uuid, String saved) {
			String src = get_source("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());

			if (src == null) {
				return saved;
			}

			try {
				JsonElement object = new JsonParser().parse(src);

				return object.getAsJsonObject().get("name").getAsString();
			} catch (Exception exc) {
				exc.printStackTrace();

				System.err.println(src);

				return saved;
			}
		}

		public static String get_source(String link) {
			try {
				URL url = new URL(link);

				URLConnection connect = url.openConnection();

				BufferedReader buffer_read = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				StringBuilder buffer = new StringBuilder();

				String input;

				while ((input = buffer_read.readLine()) != null) {
					buffer.append(input);
				}

				buffer_read.close();

				return buffer.toString();
			} catch (Exception exc) {
			return null;
			}
		}
	}

	public static class Friend {
		String user_name;
		UUID uuid;

		public Friend(String user_name, UUID uuid) {
			this.user_name = user_name;
			this.uuid      = uuid;
		}

		public String get_user_name() {
			return user_name;
		}
	}
}