package com.oldturok.turok.commands;

import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokFriends;
import com.oldturok.turok.TurokChat;
import com.oldturok.turok.TurokMod;

public class TurokFriend extends TurokChat {
	public TurokFriend() {
		super("friend", "-friend add/del/remove name | For add or remove friend.");
	}

	@Override
	public boolean Get_Message(String[] message) {
		if (message.length > 2) {
			String type           = message[1];
			String friend_request = message[2];

			if (type.equals("add") || type.equals("new")) {
				new Thread(() -> {
					TurokFriends.Friend friend = TurokFriends.add_friend(friend_request);

					if (friend == null) {
						TurokMessage.send_error_msg("Turok friends got a error with UUID request: 0x0" + TurokFriends.error);
					} else {
						TurokMessage.send_client_msg("0x0" + TurokFriends.error);
					
						TurokFriends.INSTANCE.list_friends.getValue().add(friend);

						TurokMessage.send_client_msg(friend_request + "is your new friend!");
					}

					TurokFriends.error = 0;
				}).start();
			} else if (type.equals("del") || type.equals("remove")) {
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

		if (message.length == 0) {
			TurokMessage.send_client_msg("For add friend or remove friend.");
		}

		return true;
	}
}