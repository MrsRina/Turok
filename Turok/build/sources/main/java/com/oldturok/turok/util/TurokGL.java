package com.oldturok.turok.util;

import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import java.io.*;

import de.matthiasmann.twl.utils.PNGDecoder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;

// Coded by Rina, in 07/03/20.
// Ok.
public class TurokGL {
	public static void refresh_color(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red / 255, green / 255, blue / 255, alpha / 255);
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

	public static void ImageBackground(String path, int x, int y, int width, int height, float alpha) {
		try {
			FileInputStream image_stream = new FileInputStream(path);
			PNGDecoder image_decoder = new PNGDecoder(image_stream);

			ByteBuffer image_buffer = BufferUtils.createByteBuffer(4 * image_decoder.getWidth() * image_decoder.getHeight());
	
			image_decoder.decodeFlipped(image_buffer, 4 * image_decoder.getWidth(), PNGDecoder.Format.RGBA);
	
			image_buffer.flip();
	
			int image_data = GL11.glGenTextures();
	
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, image_data);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
	
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image_buffer);
	
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(x + width, y);
				GL11.glVertex2d(x, y);
				GL11.glVertex2d(x, y + height);
				GL11.glVertex2d(x + width, y + height);
			}
			GL11.glEnd();
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
}