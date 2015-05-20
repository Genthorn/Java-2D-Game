package com.zormion.game.entities;

import java.util.Random;

import com.zormion.game.gfx.Colors;
import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public class Blob extends Mob {
	int time = 0;
	int xa = 0, ya = 0;
	Random random = new Random();
	
	public Blob(Level level, String name, int x, int y, int speed) {
		super(level, name, x, y, speed);
	}

	public void update() {
		time++;
		
		if(time % (random.nextInt(40) + 40) == 0) {
            xa = random.nextInt(3) - 1;
            ya = random.nextInt(3) - 1;
        }
         
        if(random.nextInt(15) == 0)  {
            xa = 0;
            ya = 0;
        }
         
        if(xa != 0 || ya != 0) {
            isMoving = true;
            move(xa, ya);
        }else {
            isMoving = false;
        }
         
        if(time > 7400) time = 0;
	}

	public void render(Screen screen) {
		screen.render(x, y,         0 + 25 * 32, Colors.get(-1, 166, 199, 222), 0x00, 1); //TOP 1
		screen.render(x, y + 8,     0 + 26 * 32, Colors.get(-1, 166, 199, 222), 0x00, 1); //BOTTOM 1
		screen.render(x + 8, y + 1, 1 + 25 * 32, Colors.get(-1, 166, 199, 222), 0x00, 1); 
		screen.render(x + 8, y + 8, 1 + 26 * 32, Colors.get(-1, 166, 199, 222), 0x00, 1);
	}
	
	public boolean hasCollided(int xa, int ya) {
		return false;
	}

}
