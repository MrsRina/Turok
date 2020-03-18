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

import java.util.Random;

@Module.Info(name = "AnnoucSpam", category = Module.Category.TUROK_CHAT)
public class AnnoucSpam extends Module {
	private Setting<Integer> tick_ = register(Settings.integerBuilder("Tick").withRange(1, 50).withValue(10).build());
	private Setting<Integer> delay = register(Settings.integerBuilder("Delay").withRange(2000, 10000).withValue(2000).build());

	public int x;
	public int z;

	public float moved;

	public int tick;

	public int no_spam;

	public boolean send_m = false;
	public boolean moving = true;

	public void update_tick() {
		tick += tick_.getValue();

		Random gerador = new Random(19700621);

		if (mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) {
			moved  = 0.1f;
			send_m = false;
		} else {
			moved  = 0.0f;
			send_m = true;
		}

		if (tick >= delay.getValue()) {
			if (send_m) {
				send("I walked " + Float.toString(moved) + ", thanks Turok.");

			} else {
				send("Im just stoped, thanks Turok. My cache money: " + Integer.toString(gerador.nextInt(5000)));


			}

			tick = 0;

			if (moving) {
				update_tick();
			}
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