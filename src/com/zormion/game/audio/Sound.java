package com.zormion.game.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	private Clip sound;
	
	public Sound(String path) {
		try {
			
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
			sound = AudioSystem.getClip();
			sound.open(inputStream);
		
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
			
		}
	}
	
	public void play() {
		try {
	      new Thread() {
	        public void run() {
	        	sound.start();
	        }
	      }.start();
	    }
	    catch (Throwable e) {
	      e.printStackTrace();
	    }
	}
	
	public void loop(int loops) {
	
		sound.loop(loops);
	}
	
	public void stop() {
		sound.stop();
	}
	
	public boolean isSoundOver() {
		if(sound.isRunning()) return true;
		
		return false;
	}
}
