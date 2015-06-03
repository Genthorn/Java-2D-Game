package com.zormion.game.entities;

import com.zormion.game.Game;
import com.zormion.game.containers.Inventory;
import com.zormion.game.gfx.Font;
import com.zormion.game.gfx.Screen;
import com.zormion.game.input.Keyboard;
import com.zormion.game.level.Level;
import com.zormion.game.net.packets.Packet02Move;

public class Player extends Mob {

    private Keyboard input;
    
    private int scale = 1;
    protected boolean isSwimming = false;
    private int updateCount = 0;
    private String username;
    private Inventory inventory;
    
    public Player(String username, Level level, int x, int y, Keyboard input) {
        super(level, "Player", x, y, 1);
        this.input = input;
        this.username = username;
        this.inventory = new Inventory();
    }

    public void update() {
        int xa = 0;
        int ya = 0;
        if (input != null) {
            if (input.up.isPressed()) {
                ya--;
            }
            if (input.down.isPressed()) {
                ya++;
            }
            if (input.left.isPressed()) {
                xa--;
            }
            if (input.right.isPressed()) {
                xa++;
            }
        }
        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;

            Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving,
                    this.movingDir);
            packet.writeData(Game.game.socketClient);
        } else {
            isMoving = false;
        }
        if (level.getTile(this.x >> 3, this.y >> 3).isLiquid()) {
            isSwimming = true;
        }
        if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).isLiquid() == false) {
            isSwimming = false;
        }
        
        //inventory.update(input);
        
        updateCount++;
    }

    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 28;
        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        if (movingDir == 1) {
            xTile += 2;
        } else if (movingDir > 1) {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
        }

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;
        if (isSwimming) {
            yOffset += 4;
            
            screen.render(xOffset, yOffset + 3, 0 + 27 * 32, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, 0x01, 1);
        }
        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, flipTop, scale);
        screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, flipTop,
                scale);

        if (!isSwimming) {
            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32,
                    flipBottom, scale);
            screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)
                    * 32, flipBottom, scale);
        }
        if (username != null) {
            Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10, 1);
        }
    }
    
    public void renderNotOnServer(Screen screen) {
    	inventory.render(screen);
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
        return this.username;
    }
    
    public void setMoving(boolean isMoving) {
    	this.isMoving = isMoving;
    }
    
    public void setMovingDir(int dir) {
    	this.movingDir = dir;
    }

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}
}
