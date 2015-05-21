package com.zormion.game.states;

import com.zormion.game.gfx.Screen;

public abstract class State {
	public abstract void init();
	
	public abstract void update();
	
	public abstract void render(Screen screen);
}
