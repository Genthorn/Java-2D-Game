package com.zormion.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zormion.game.entities.Entity;
import com.zormion.game.entities.PlayerMP;
import com.zormion.game.gfx.Screen;
import com.zormion.game.level.tiles.Tile;

public class Level {

    private byte[] tiles;
    public int width;
    public int height;
    private List<Entity> entities = new ArrayList<Entity>();
    private String levelPath;
    
    private BufferedImage image;

    public Level(String levelPath) {
        if (levelPath != null) {
            this.levelPath = levelPath;
            this.loadLevelFromFile();
        } else {
            this.width = 64;
            this.height = 64;
            tiles = new byte[width * height];
            this.generateLevel();
        }
    }

    private void loadLevelFromFile() {
        try {
            this.image = ImageIO.read(new File(this.levelPath));
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
            tiles = new byte[width * height];
            this.loadTiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTiles() {
        int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileCheck: for (Tile t : Tile.tiles) {
                    if (t != null && t.getLevelColor() == tileColours[x + y * width]) {
                        this.tiles[x + y * width] = t.getID();
                        break tileCheck;
                    }
                }
            }
        }
    }

    public void generateLevel() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x * y % 10 < 7) {
                    tiles[x + y * width] = Tile.GRASS.getID();
                } else {
                    tiles[x + y * width] = Tile.STONE.getID();
                }
            }
        }
    }

    public synchronized List<Entity> getEntities() {
        return this.entities;
    }

    public void update() {
    	
    	
        for (Entity e : getEntities()) {
            e.update();
        }

        for (Tile t : Tile.tiles) {
        	if (t == null) break;
            t.update();
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0)
            xOffset = 0;
        if (xOffset > ((width << 3) - screen.width))
            xOffset = ((width << 3) - screen.width);
        if (yOffset < 0)
            yOffset = 0;
        if (yOffset > ((height << 3) - screen.height))
            yOffset = ((height << 3) - screen.height);

        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
            for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
                getTile(x, y).render(screen, this, x << 3, y << 3);
            }
        }
    }

    public void renderEntities(Screen screen) {
        for (Entity e : getEntities()) {
            e.render(screen);
        }
    }


    public synchronized void addEntity(Entity entity) {
        this.getEntities().add(entity);
    }

    public synchronized void removePlayerMP(String username) {
        int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        this.getEntities().remove(index);
    }

    private int getPlayerMPIndex(String username) {
        int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    public synchronized void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
        int index = getPlayerMPIndex(username);
        PlayerMP player = (PlayerMP) this.getEntities().get(index);
        player.x = x;
        player.y = y;
        player.setMoving(isMoving);
        player.setNumSteps(numSteps);
        player.setMovingDir(movingDir);
    }
    
    public Tile getTile(int x, int y) {
    	if (0 > x || x >= width || 0 > y || y >= height)
    		return Tile.VOID;
    	return Tile.tiles[tiles[x + y * width]];
    }
}