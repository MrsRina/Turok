package com.oldturok.turok.commands;

import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokFriends;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

public class TurokFriend extends TurokChat {
	public TurokFriend() {
		super("friend", "Turok users friends!");
	}

	String friend_string = "";

	@Override
	public boolean Get_Message(String[] message) {
		if (message.length > 2) {
			String type           = message[1];
			String friend_request = message[2];

			if (type.equals("add") || type.equals("new")) {
				new Thread(() -> {
					TurokFriends.Friend friend = TurokFriends.add_friend(friend_request);

					if (friend == null) {
						TurokMessage.send_error_msg("Turok friends got a error current get uuid, try again or other name.");
					} else {
						TurokFriends.INSTANCE.list_friends.getValue().add(friend);

						TurokMessage.send_client_msg(friend_request + "is your new friend!");
					}
				}).start();
			} else if (type.equals("del") || type.equals("delete") || type.equals("remove")) {
				if (!(TurokFriends.is_friend(friend_request))) {
					TurokMessage.send_error_msg("He is NOT his friend.");
				} else {
					TurokFriends.Friend friend = TurokFriends.INSTANCE.list_friends.getValue().stream().filter(friend_for_remove -> friend_for_remove.get_user_name().equalsIgnoreCase(friend_request)).findFirst().get();

					TurokFriends.INSTANCE.list_friends.getValue().remove(friend);

					TurokMessage.send_client_msg("Now him is not more your friend.");
				}
			} else {
				TurokMessage.send_error_msg("-friend add/remove name.");
			}
		}

		if (message.length > 1) {
			String type = message[1];

			if (type.equals("list")) {
				if (TurokFriends.INSTANCE.list_friends.getValue().isEmpty()) {
					TurokMessage.send_client_msg("You have 0 friends. lol");
				} else {
					for (TurokFriends.Friend friend : TurokFriends.INSTANCE.list_friends.getValue()) {
						friend_string += (friend.get_user_name() + ", ");
					}

					friend_string = friend_string.substring(0, friend_string.length() - 2);

					TurokMessage.send_client_msg("Your friends are: " + friend_string);
				}
			}

		}

		return true;
	}
}