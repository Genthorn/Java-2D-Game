package com.zormion.game.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	private BufferedImage image;
	private int width, height;
	private String path;
	private int[] pixels;
	
	public Texture(String path) {
		try {
			this.image = ImageIO.read(new File("strawberry.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		width = image.getWidth();
		height = image.getHeight();
		this.path = path;
		pixels = new int[width * height];
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getPath() {
		return path;
	}

	public int[] getPixels() {
		return pixels;
	}
}
