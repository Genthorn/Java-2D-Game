package com.zormion.game.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {
	private String path;
	private int width, height;
	
	private int[] pixels;
	
	public Spritesheet(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image == null) return;
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = (pixels[i] & 0xff) / 64;
		}
	}
	
	public String getPath() {
		return path;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
}
