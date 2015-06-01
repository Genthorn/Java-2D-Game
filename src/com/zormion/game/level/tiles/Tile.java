package com.zormion.game.level.tiles;

import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public abstract class Tile {
	public static final Tile[] tiles = new Tile[256];
	private static int id = 0;
	
	//ln = ((y * 32) + x) + 1?
													 //                ln  x   y
	public static final Tile VOID = new BasicSolidTile                (-1, 10, 10);
	public static final Tile GRASS = new BasicTile                    (3,  2,  0);
	public static final Tile FLOWER_RED = new BasicTile               (4,  3,  0);
	public static final Tile FLOWER_YELLOW = new BasicTile            (5,  4,  0);
	public static final Tile FLOWER_BLUE = new BasicTile              (6,  5,  0);
	public static final Tile BRICK_CLEAN = new BasicSolidTile         (1,  0,  0);
	public static final Tile BRICK_CRACKED = new BasicSolidTile       (2,  1,  0);
	public static final Tile BRICK_CLEAN_MOSSY = new BasicSolidTile   (33, 0,  1);
	public static final Tile BRICK_CRACKED_MOSSY = new BasicSolidTile (34, 1,  1);
	public static final Tile SAND = new BasicTile                     (35, 2,  1);
	public static final Tile SAND_CASTLE = new BasicSolidTile         (36, 3,  1);
	public static final Tile SAND_BUCKET_RED = new BasicSolidTile     (37, 4,  1);
	public static final Tile SAND_BUCKET_BLUE = new BasicSolidTile    (68, 5,  2);
	
	
	public static final Tile WATER = new AnimatedTile        (129, new int[][] { { 0, 4 }, { 1, 4 }, { 2, 4 }, { 1, 4 } }, 1000);
	
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
