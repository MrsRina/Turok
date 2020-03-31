package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.util.GeometryMasks;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import org.lwjgl.opengl.GL11;

import java.awt.*;

// Rina.
@Module.Info(name = "BlockHighlight", description = "For you see better the block on split.", category = Module.Category.TUROK_RENDER)
public class BlockHighlight extends Module {
	private Setting<Integer> color_r = register(Settings.integerBuilder("Color Red").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> color_g = register(Settings.integerBuilder("Color Green").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> color_b = register(Settings.integerBuilder("Color Blue").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> color_a = register(Settings.integerBuilder("Alpha Color").withMinimum(0).withMaximum(255).withValue(70));
	private Setting<Boolean> rgb     = register(Settings.b("RGB", false));

	private Setting<TypeDraw> type = register(Settings.e("Type Draw", TypeDraw.OUTLINE));
	
	public RayTraceResult result;

	int r;
	int g;
	int b;

	int prepare;
	int mask;

	boolean type_;

	@Override
	public void onUpdate() {
		float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
		int color_rgb      = Color.HSBtoRGB(tick_color[0], 1, 1);

		tick_color[0] += 0.1f;

		if (rgb.getValue()) {
			r = ((color_rgb >> 16) & 0xFF);
			g = ((color_rgb >> 8) & 0xFF);
			b = (color_rgb & 0xFF);
		} else {
			r = color_r.getValue();
			g = color_g.getValue();
			b = color_b.getValue();
		}

		if (mc.world == null || mc.player == null) {
			return;
		}

		result = mc.objectMouseOver;
		if (result == null) {
			return;
		}

		switch (type.getValue()) {
			case OUTLINE : {
				prepare = GL11.GL_LINES;
				mask    = GeometryMasks.Line.ALL;

				type_ = true;

				break;
			}

			case BOX : {
				prepare = GL11.GL_QUADS;
				mask    = GeometryMasks.Quad.ALL;

				type_ = false;

				break;
			}
		}
	}

	@Override
	public void onWorldRender(RenderEvent event) {
		if (mc.world == null || mc.player == null) {
			return;
		}

		if (result == null) {
			return;
		}

		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = result.getBlockPos();
			IBlockState block_state = mc.world.getBlockState(pos);

			TurokTessellator.prepare(prepare);

			if (type_) {
				TurokTessellator.drawLines(pos, r, g, b, color_a.getValue(), mask);
			} else {
				TurokTessellator.drawBox(pos, r, g, b, color_a.getValue(), mask);
			}

			TurokTessellator.release();
		}
	}

	public enum TypeDraw {
		OUTLINE,
		BOX
	}
}