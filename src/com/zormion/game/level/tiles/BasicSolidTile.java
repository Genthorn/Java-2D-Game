package com.zormion.game.level.tiles;

public class BasicSolidTile extends BasicTile {

	public BasicSolidTile(int id, int x, int y, int levelColor) {
		super(id, x, y, levelColor);
		this.solid = true;
	}

}
