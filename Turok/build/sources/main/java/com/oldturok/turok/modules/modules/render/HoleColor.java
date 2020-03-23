package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.modules.combat.TurokCrystalAura;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.GeometryMasks;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import static com.oldturok.turok.module.modules.combat.TurokCrystalAura.get_player_pos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.awt.*;
import java.io.*;

// Rina.
@Module.Info(name = "HoleColor", description = "For draw block into holes.", category = Module.Category.TUROK_RENDER)
public class HoleColor extends Module {
	private ConcurrentHashMap<BlockPos, Boolean> safe_holes;
	private final BlockPos[] barrier_ = {
		new BlockPos(0, -1, 0),
		new BlockPos(0, 0, -1),
		new BlockPos(1, 0, 0),
		new BlockPos(0, 0, 1),
		new BlockPos(-1, 0, 0)
	};

	private Setting<Double> range = register(Settings.d("Range", 10.0d));

	private Setting<Integer> color_r = register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(0));
	private Setting<Integer> color_g = register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(0));
	private Setting<Integer> color_b = register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(0));
	private Setting<Integer> a       = register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(255));
	private Setting<Boolean> rgb     = register(Settings.b("RGB", true));

	private Setting<TypeHole> type = register(Settings.e("Hole Type", TypeHole.OUTLINE));

	public int prepare;
	public int mask;

	@Override
	public void onUpdate() {
		switch (type.getValue()) {
			case OUTLINE : {
				prepare = GL11.GL_LINES;
				mask    = GeometryMasks.Line.ALL;

				break;
			}

			case BOX : {
				prepare = GL11.GL_QUADS;
				mask    = GeometryMasks.Quad.ALL;

				break;
			}
		}

		if (safe_holes == null) {
			safe_holes = new ConcurrentHashMap<>();
		} else {
			safe_holes.clear();
		}

		int range_ = (int) Math.ceil(range.getValue());

		TurokCrystalAura crystal_function = (TurokCrystalAura) ModuleManager.getModuleByName("TurokCrystalAura");
		List<BlockPos> block_pos = crystal_function.get_sphere(get_player_pos(), range_, range_, false, true, 0);

		for (BlockPos pos : block_pos) {
			if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) continue;
			if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) continue;
			if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) continue;

			boolean safe = true;
			boolean bedrock = true;

			for (BlockPos offset : barrier_) {
				Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
				if (block != Blocks.BEDROCK) bedrock = false;
				if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
					safe = false;
					break;
				}
			}

			if (safe) safe_holes.put(pos, bedrock);
		}
	}

	@Override
	public void onWorldRender(final RenderEvent event) {
		if (mc.player == null || safe_holes == null) return;
		if (safe_holes.isEmpty()) return;

		TurokTessellator.prepare(prepare);
		safe_holes.forEach((block_pos, bedrock) -> {
			int r = 0;
			int g = 0;
			int b = 0;

			if (rgb.getValue()) {
				float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
				int color_rgb      = Color.HSBtoRGB(tick_color[0], 1, 1);

				tick_color[0] += 0.1f;

				r = ((color_rgb >> 16) & 0xFF);
				g = ((color_rgb >> 8) & 0xFF);
				b = (color_rgb & 0xFF);
			} else {
				r = color_r.getValue();
				g = color_g.getValue();
				b = color_b.getValue();
			}

			draw(block_pos, r, g, b);
			draw(block_pos, r, g, b);
			draw(block_pos, r, g, b);
		});
		TurokTessellator.release();
	}

	private void draw(BlockPos block_pos, int r, int g, int b) {
		Color color = new Color(r, g, b, a.getValue());
		TurokTessellator.drawLines(block_pos, color.getRGB(), mask);
	}

	public enum TypeHole {
		OUTLINE,
		BOX;
	}
}