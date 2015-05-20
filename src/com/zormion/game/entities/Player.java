package com.zormion.game.entities;

import com.zormion.game.Game;
import com.zormion.game.audio.Sound;
import com.zormion.game.gfx.Colors;
import com.zormion.game.gfx.Font;
import com.zormion.game.gfx.Screen;
import com.zormion.game.input.InputHandler;
import com.zormion.game.level.Level;
import com.zormion.game.net.packets.Packet02Move;

public class Player extends Mob {
	
	private InputHandler input;
	private int color = Colors.get(-1, 111, 145, 543);
	public boolean isSwimming = false;
	private int updateCount = 0;
	protected String username;
	private Sound walkingSound;
	private int healthMax = 50, manaMax = 50;
	
	public Player(String username, Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input = input;
		this.x = x;
		this.y = y;
		this.username = username;
		//this.walkingSound = new Sound("res/sounds/");
	}

	public void update() {
		int xa = 0, ya = 0;
		if(input != null) {
			if (input.up.isPressed()) {
				ya -= 1;
			}
		
			if (input.down.isPressed()) {
				ya += 1;
			}
		
			if (input.left.isPressed()) {
				xa -= 1;
			}
		
			if (input.right.isPressed()) {
				xa += 1;
			}
		}
		
		if(xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
			
			Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
			packet.writeData(Game.game.socketClient);
			
		} else {
			isMoving = false;
		}
		
		if(level.getTile(this.x >> 3, this.y >> 3).getID() == 3) {
			isSwimming = true;
		}
		
		if(isSwimming && level.getTile(this.x >> 3, this.y >> 3).getID() != 3) {
			isSwimming = false;
		}
		
		updateCount++;
		
	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = speed * 3;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;
		
		if(movingDir == 1) {
			xTile += 2;
		} else if(movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}
		
		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;
		
		if(isSwimming) {
			int waterColor = 0;
			yOffset += 4;
			if(updateCount % 60 < 15) {
				waterColor = Colors.get(-1, -1, 255, -1);
			} else if(updateCount % 60 < 30) {
				waterColor = Colors.get(-1, 255, 115, -1);
			} else if(30 <= updateCount % 60 && updateCount % 60 < 45) {
				waterColor = Colors.get(-1, 115, -1, 255);
			} else {
				waterColor = Colors.get(-1, 225, 115, -1);
			}
			
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColor, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColor, 0x01, 1);
			
		}
		
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, flipTop, 1); // upper body part 1
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, color, flipTop, 1); // upper body part 2
		
		if(!isSwimming) {
			screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, color, flipBottom, 1); // lower body part 1
			screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, color, flipBottom, 1); // lower body part 2
		}
		
		if(username != null) Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10, Colors.get(-1, -1, -1, 555));
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
	
	public String getUsername() {
		return username;
	}
}
