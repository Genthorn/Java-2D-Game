package com.zormion.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import com.zormion.game.Game;

public class InputHandler implements KeyListener {
	
	public InputHandler(Game game) {
		game.requestFocus();
		game.addKeyListener(this);
	}
	
	public class Key {
		private boolean isPressed = false;
		private int numTimesPressed = 0;
		
		public void toggle(boolean isPressed) {
			this.isPressed = isPressed;
			if(isPressed) numTimesPressed++;
		}
		
		public boolean isPressed() {
			return this.isPressed;
		}
		
		public int getNumTimesPressed() {
			return numTimesPressed;
		}
	}
	
	//public List<Key> keys = new ArrayList<Key>();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public void toggleKey(int keyCode, boolean isPressed) {
		if(keyCode == KeyEvent.VK_W) up.toggle(isPressed);
		if(keyCode == KeyEvent.VK_A) left.toggle(isPressed);
		if(keyCode == KeyEvent.VK_S) down.toggle(isPressed);
		if(keyCode == KeyEvent.VK_D) right.toggle(isPressed);
	}

}
