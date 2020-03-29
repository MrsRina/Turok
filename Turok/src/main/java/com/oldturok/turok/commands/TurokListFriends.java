package com.oldturok.turok.commands;

import com.oldturok.turok.TurokFriends;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.TurokChat;

public class TurokListFriends extends TurokChat {
	public TurokListFriends() {
		super("friends", "-friends | For get list friends.");
	}

	String friend_string = "";

	@Override
	public boolean Get_Message(String[] message) {
		if (message.length > 0) {
			if (TurokFriends.INSTANCE.list_friends.getValue().isEmpty()) {
				TurokMessage.send_client_msg("You have 0 friends. lol");
			} else {
				for (TurokFriends.Friend friend : TurokFriends.INSTANCE.list_friends.getValue()) {
					TurokMessage.send_client_msg("Friend: " + friend.get_user_name());
				}
			}
		} else {
			TurokMessage.send_error_msg("Try use only -friends for get a list, or use -help command friends/friend.");
		}

		return true;
	}
}
