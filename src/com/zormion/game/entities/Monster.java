package com.zormion.game.entities;

import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public class Monster extends Mob {
	
	int time = 0;
	int xa = 0;
	int ya = 0;
	
	public Monster(Level level, String name, int x, int y, int speed) {
		super(level, name, x, y, speed);
	}

	public void update() {
		time++;
		
		if(time % 60 == 0) {
			xa = -xa;
		}
		
		x += xa;
		y += ya;
	}

	public void render(Screen screen) {
		screen.render(x, y, tile, 0, 0);
	}
	
	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 7;
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMin)) {
                return true;
            }
        }
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMax)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMin, y)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMax, y)) {
                return true;
            }
        }
        return false;
	}
}
