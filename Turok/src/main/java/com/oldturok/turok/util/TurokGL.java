package com.oldturok.turok.util;

import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;
import java.awt.*;
import java.io.*;

import de.matthiasmann.twl.utils.PNGDecoder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;

import net.minecraft.util.ResourceLocation; //
import net.minecraft.client.Minecraft;

// Coded by Rina, in 07/03/20.
// Ok.
public class TurokGL {
	public static Minecraft mc = Minecraft.getMinecraft();

	public static void refresh_color(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red / 255, green / 255, blue / 255, alpha / 255);
	}

	public static void TurokRGBA(float alpha) {
		float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};

		int color_rgb = Color.HSBtoRGB(tick_color[0], 1, 1);

		GL11.glColor4f(((color_rgb >> 16) & 0xFF) / 255, ((color_rgb >> 8) & 0xFF) / 255, (color_rgb & 0xFF) / 255, alpha / 255);
		
		tick_color[0] += 0.1f;
	}

	public static void DisableGL(int opengl) {
		GL11.glDisable(opengl);
	}

	public static void EnableGL(int opengl) {
		GL11.glEnable(opengl);
	}

	public static void FixRefreshColor() {
		DisableGL(GL11.GL_TEXTURE_2D);
        DisableGL(GL11.GL_BLEND);
	}

	public static void ImageGL(String path, int x, int y, int width, int height, float alpha) {
		try {
			BufferedImage image = ImageIO.read(new File(path));

			BufferedImage image_buffer = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
 			
 			Graphics2D image_2d = image_buffer.createGraphics();

 			image_2d.drawImage(image_buffer, 0, 0, null);
 			image_2d.dispose();
	 
			image_2d.dispose();
	 
			int image_data = GL11.glGenTextures();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ImageIO.write(image_buffer, "png", baos);

			baos.flush();

			byte[] image_in_byte = baos.toByteArray();

			baos.close();

			ByteBuffer image_byte = ByteBuffer.wrap(image_in_byte);
	 
			EnableGL(GL11.GL_TEXTURE_2D);
			EnableGL(GL11.GL_BLEND);
	 
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, image_data);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image_byte); 
 		
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(width, 0);
				GL11.glVertex2d(0, 0);
				GL11.glVertex2d(0, height);
				GL11.glVertex2d(width, height);
			}
			GL11.glEnd();
		} catch (IOException exc) {
			System.out.println("Gay");
		}
	}
}