package com.zormion.game.states;

import com.zormion.game.gfx.Screen;

public abstract class State {
	private String name;
	
	public State(String name) {
		this.name = name;
	}
	
	public abstract void init();
	
	public abstract void update();
	
	public abstract void render(Screen screen);
	
	public String getName() {
		return name;
	}
}
