package com.zormion.game.entities.spawners;

import com.zormion.game.entities.Entity;
import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public class Spawner {
	
	private Entity toSpawnEntity;
	private Level level;
	
	public Spawner(Level level, Entity toSpawnEntity) {
		this.toSpawnEntity = toSpawnEntity;
		this.level = level;
	}
	
	public void spawn(int x, int y, int amount) {
		for(int i = 0; i < amount; i++) {
			
		}
	}
}
