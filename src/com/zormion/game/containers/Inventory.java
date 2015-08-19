package com.zormion.game.containers;

import com.zormion.game.gfx.Screen;
import com.zormion.game.input.Keyboard;

public class Inventory {
	
	private boolean open;
	
	public Inventory() {
		open = false;
	}
	
	public void update(Keyboard keyboard) {
		if(keyboard.E.isPressed() && open == false) open = true;
		else if(keyboard.E.isPressed() && open == true) open = false;
	}
	
	public void render(Screen screen) {
		//if(isOpen()) screen.renderRect(0xff6f6f6f, (screen.width / 2) - 125, (screen.height / 2) - 75, 250, 150);
	}
	
	public boolean isOpen() {
		return open;
	}
}
