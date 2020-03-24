package com.oldturok.turok;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.events.ReadyEvent;

public class TurokIRC extends ListenerAdapter {
	public void onReady(ReadyEvent event) {
		event.getJDA().geTextChannelById("691741912633180220").sendMessage("hiiii").queue();
	}
}