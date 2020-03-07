package com.oldturok.turok.module.modules.misc;

import com.oldturok.turok.event.events.PacketEvent;
import com.oldturok.turok.module.Module;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import net.minecraft.network.play.client.CPacketCloseWindow;

// Rina.
// Yes.
@Module.Info(name = "ExtraSlots", category = Module.Category.TUROK_RENDER)
public class ExtraSlots extends Module {
	@EventHandler
	private Listener<PacketEvent.Send> listener = new Listener<>(event -> {
		if (envet.getPacet() instanceof CPacketCloseWindow) {
			event.cancel();
		}
	});
}