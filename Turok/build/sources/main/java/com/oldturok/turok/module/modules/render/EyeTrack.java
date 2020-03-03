package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;

import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.util.GeometryMasks;
import com.oldturok.turok.util.TurokTessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

// Rina.
@Module.Info(name = "EyeTrack", category = Module.Category.TUROK_RENDER)
public class EyeTrack extends Module {
	private Setting<Integer> r = register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(0));
	private Setting<Integer> g = register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(0));
	private Setting<Integer> b = register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(255));

	@Override
	public void onWorldRender(RenderEvent event) {
		mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> mc.player != entity).map(entity -> (EntityLivingBase) entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> (true && entity instanceof EntityPlayer)).forEach(this::drawLine);
	}

	private void drawLine(EntityLivingBase event_) {
		RayTraceResult result = event_.rayTrace(6, Minecraft.getMinecraft().getRenderPartialTicks());
		if (result == null) 
			return;
		Vec3d eyes = event_.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks());

		GlStateManager.enableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();

		double posX = eyes.x - mc.getRenderManager().renderPosX;
		double posY = eyes.y - mc.getRenderManager().renderPosY;
		double posZ = eyes.z - mc.getRenderManager().renderPosZ;

		double posX_ = result.hitVec.x - mc.getRenderManager().renderPosX;
		double posY_ = result.hitVec.y - mc.getRenderManager().renderPosY;
		double posZ_ = result.hitVec.z - mc.getRenderManager().renderPosZ;

		GL11.glColor4f(r.getValue()/255, g.getValue()/255, b.getValue()/255, 1.0f);

		GlStateManager.glLineWidth(1.5f);

		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3d(posX, posY, posZ);
			GL11.glVertex3d(posX_, posY_, posZ_);
			GL11.glVertex3d(posX_, posY_, posZ_);
			GL11.glVertex3d(posX_, posY_, posZ_);
		}
		GL11.glEnd();

		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			TurokTessellator.prepare(GL11.GL_QUADS);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			BlockPos block_pos = result.getBlockPos();

			float x = block_pos.x - 0.01f;
			float y = block_pos.y - 0.01f;
			float z = block_pos.z - 0.01f;

			TurokTessellator.drawBox(TurokTessellator.getBufferBuilder(), x, y, z, 1.01f, 1.01f, 1.01f, r.getValue(), g.getValue(), b.getValue(), 70, GeometryMasks.Quad.ALL);
			TurokTessellator.release();
		}

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
	}
}