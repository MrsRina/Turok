package com.oldturok.turok.module.modules.chat;


import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import com.oldturok.turok.TurokMod;

import com.oldturok.turok.event.events.GuiScreenEvent;
import com.oldturok.turok.event.events.PacketEvent;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.Module;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.block.Block;
import net.minecraft.init.Items;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Queue;
import java.util.Map;

@Module.Info(name = "AnnoucSpam", category = Module.Category.TUROK_CHAT)
public class AnnoucSpam extends Module {
	private Setting<Integer> tick_ = register(Settings.integerBuilder("Tick").withRange(1, 10).withValue(10).build());
	private Setting<Integer> delay = register(Settings.integerBuilder("Delay").withRange(500, 5000).withValue(1).build());

	public int x;
	public int z;

	public int old_x;
	public int old_z;

	public int moved;

	public int tick;

	public void update_tick() {
		tick += tick_.getValue();

		x = (int) mc.player.posX;
		z = (int) mc.player.posZ;

		old_x = x;
		old_z = z;

		if (old_x != mc.player.posX || old_z != mc.player.posZ) {
			moved++;
		}

		if (tick >= delay.getValue()) {
			send("I walked " + Integer.toString(moved) + ", thanks Turok.");

			tick = 0;

			update_tick();
		}
	}

	@Override
	public void onUpdate() {
		update_tick();
	}

	public void send(String message) {
		mc.player.connection.sendPacket(new CPacketChatMessage(message));
	}
}