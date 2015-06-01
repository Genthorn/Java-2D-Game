package com.zormion.game.level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    
    public Level(String levelPath) {
        this.levelPath = levelPath;
        this.loadLevelFromFile();
        
    }

    private void loadLevelFromFile() {
        try {
            this.loadTiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTiles() {
    	try {
            Scanner scanner = new Scanner(new File(levelPath));
             
            String find = scanner.nextLine();
            int tempw = 1;
            int temph = 1;
            int currentTile;
             
            if(find.equals("<width>")) tempw = scanner.nextInt();
                 
            scanner.nextLine();
            find = scanner.nextLine();
             
            if(find.equals("<height>")) temph = scanner.nextInt();
                 
            int w = width = tempw;
            int h = height = temph;
            
            this.tiles = new byte[width * height];
            
            int[] tilesScanned = new int[tiles.length];
            
            for(int i = 0; i < w * h; i++) {
                currentTile = scanner.nextInt();
                tilesScanned[i] = currentTile;
            }
            
            for (int y = 0; y < height; y++) {
            	for (int x = 0; x < width; x++) {
            		for(Tile t : Tile.tiles) {
            			if (t != null && t.getLevelNumber() == tilesScanned[x + y * width]) {
            				this.tiles[x + y * width] = t.getID();
            			}
            		}
            	}
            }
    	
            scanner.close();
             
        } catch(IOException ex) {
            ex.printStackTrace();
            System.err.println("Exception! Could not load level file!");
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