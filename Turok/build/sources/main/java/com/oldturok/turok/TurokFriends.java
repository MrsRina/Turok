package com.oldturok.turok;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.Minecraft;

import com.mojang.util.UUIDTypeAdapter;

import com.google.common.base.Converter;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.regex.Pattern;
import java.util.*;
import java.net.*;
import java.io.*;

public class TurokFriends {
	public static final TurokFriends INSTANCE = new TurokFriends();

	public static Setting<ArrayList<Friend>> list_friends;

	public static Minecraft mc = Minecraft.getMinecraft();

	public TurokFriends() {}

	public static void init_friends_list() {
		list_friends = Settings.custom("Friends", new ArrayList<Friend>(), new FriendListConverter()).buildAndRegister("friends");
	}

	public static Boolean is_friend(String name) {
		return list_friends.getValue().stream().anyMatch(friend -> friend.user_name.equalsIgnoreCase(name));
	}

	public static Friend add_friend(String name) {
		ArrayList<NetworkPlayerInfo> info_map = new ArrayList<NetworkPlayerInfo> (mc.getConnection().getPlayerInfoMap());
		NetworkPlayerInfo profile             = info_map.stream().filter(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(name)).findFirst().orElse(null);

		if (profile == null) {
			String uuid_name = request_uuid("[\"" + name + "\"]");

			if (uuid_name == null || uuid_name.isEmpty()) {
				return null;
			} else {
				JsonElement element = new JsonParser().parse(uuid_name);

				if (element.getAsJsonArray().size() == 0) {
					return null;
				} else {
					try {
						String id   = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
						String user = element.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();

						Friend friend = new Friend(user, UUIDTypeAdapter.fromString(id));

						return friend;
					} catch (Exception exc) {
						exc.printStackTrace();

						return null;
					}
				}
			}
		}

		Friend friend_ = new Friend(profile.getGameProfile().getName(), profile.getGameProfile().getId());

		return null;
	}

	public static String request_uuid(String data) {
		try {
			String query = "https://api.mojang.com/profiles/minecraft";
			String json  = data;

			URL url = new URL(query);

			HttpURLConnection connect = (HttpURLConnection) url.openConnection();

			connect.setConnectTimeout(5000);

			connect.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			connect.setDoOutput(true);
			connect.setDoInput(true);

			connect.setRequestMethod("POST");

			OutputStream output = connect.getOutputStream();
			output.write(json.getBytes("UTF-8"));
			output.close();

			InputStream input = new BufferedInputStream(connect.getInputStream());

			String return_ = convert_to(input);

			input.close();

			connect.disconnect();

			return return_;
		} catch (Exception exc) {
			return null;
		}
	}

	public static String convert_to(InputStream input) {
		Scanner scanned = new Scanner(input).useDelimiter("\\A");

		String resp = scanned.hasNext() ? scanned.next() : "/";

		return resp;
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