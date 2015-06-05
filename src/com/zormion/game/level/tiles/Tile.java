package com.zormion.game.level.tiles;

import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public abstract class Tile {
	public static final Tile[] tiles = new Tile[256];
	private static int id = 0;
	
	public static final Tile VOID = new BasicSolidTile                (10, 10);
	public static final Tile GRASS = new BasicTile                    (2,  0);
	public static final Tile FLOWER_RED = new BasicTile               (3,  0);
	public static final Tile FLOWER_YELLOW = new BasicTile            (4,  0);
	public static final Tile FLOWER_BLUE = new BasicTile              (5,  0);
	public static final Tile BRICK_CLEAN = new BasicSolidTile         (0,  0);
	public static final Tile BRICK_CRACKED = new BasicSolidTile       (1,  0);
	public static final Tile BRICK_CLEAN_MOSSY = new BasicSolidTile   (0,  1);
	public static final Tile BRICK_CRACKED_MOSSY = new BasicSolidTile (1,  1);
	public static final Tile SAND = new BasicTile                     (2,  1);
	public static final Tile SAND_CASTLE = new BasicSolidTile         (3,  1);
	public static final Tile SAND_BUCKET_RED = new BasicSolidTile     (4,  1);
	public static final Tile SAND_BUCKET_BLUE = new BasicSolidTile    (3,  2);
	
	public static final Tile WATER = new AnimatedTile(new int[][] { { 0, 4 }, { 1, 4 }, { 2, 4 }, { 1, 4 } }, 1000);
	public static final Tile WATER_BORDER_GRASS_TOP_LEFT = new BasicTile(0,  5);
	public static final Tile WATER_BORDER_GRASS_TOP_MIDDLE = new BasicTile(1,  5);
	public static final Tile WATER_BORDER_GRASS_TOP_RIGHT = new BasicTile(2, 5);
	public static final Tile WATER_BORDER_GRASS_MIDDLE_LEFT = new BasicTile(0, 6);
	public static final Tile WATER_BORDER_GRASS_MIDDLE_RIGHT = new BasicTile(2, 6);
	public static final Tile WATER_BORDER_GRASS_BOTTOM_LEFT = new BasicTile(0, 7);
	public static final Tile WATER_BORDER_GRASS_BOTTOM_MIDDLE = new BasicTile(1, 7);
	public static final Tile WATER_BORDER_GRASS_BOTTOM_RIGHT = new BasicTile(2, 7);
	
	public static final Tile GRASS_WATER_DIAGONAL_BOTTOM_RIGHT = new BasicTile(3, 5);
	public static final Tile GRASS_WATER_DIAGONAL_BOTTOM_LEFT = new BasicTile(4, 5);
	public static final Tile GRASS_WATER_DIAGONAL_TOP_RIGHT = new BasicTile(3, 6);
	public static final Tile GRASS_WATER_DIAGONAL_TOP_LEFT = new BasicTile(4, 6);
	
	protected byte tileID;
    protected boolean solid;
    protected boolean emitter;
    protected boolean liquid;
    private int levelNumber;

    public Tile(int levelNumber, boolean isSolid, boolean isEmitter) {
        this.tileID = (byte) id;
        if (tiles[id] != null)
            throw new RuntimeException("Duplicate tile id on " + id);
        this.solid = isSolid;
        this.emitter = isEmitter;
        this.levelNumber = levelNumber;
        tiles[id] = this;
        
        id++;
    }

    public byte getID() {
        return tileID;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isEmitter() {
        return emitter;
    }
    
    public boolean isLiquid() {
    	return liquid;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public abstract void update();

    public abstract void render(Screen screen, Level level, int x, int y);
	
}
