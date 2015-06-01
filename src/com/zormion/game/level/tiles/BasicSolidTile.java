package com.zormion.game.level.tiles;

public class BasicSolidTile extends BasicTile {

	public BasicSolidTile(int levelNumber, int x, int y) {
		super(levelNumber, x, y);
		this.solid = true;
	}

}
