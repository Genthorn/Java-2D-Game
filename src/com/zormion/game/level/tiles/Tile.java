package com.zormion.game.level.tiles;

import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public abstract class Tile {
	public static final Tile[] tiles = new Tile[256];
	
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, 0xff000000);
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, 0xff808080);
	public static final Tile GRASS = new BasicTile(2, 2, 0, 0xff007F0E);
	public static final Tile FLOWER = new BasicTile(4, 3, 0, 0xffFFD800);
	public static final Tile WATER = new AnimatedTile(3, new int[][] { { 0, 4 }, { 1, 4 }, { 2, 4 }, { 1, 4 } }, 0xFF0026FF, 1000);
	
	//public static final Tile WATERBORDER00 =  new BasicTile(5, 5, 0, );
	
	protected byte tileID;
    protected boolean solid;
    protected boolean emitter;
    private int levelColour;

    public Tile(int id, boolean isSolid, boolean isEmitter, int levelColour) {
        this.tileID = (byte) id;
        if (tiles[id] != null)
            throw new RuntimeException("Duplicate tile id on " + id);
        this.solid = isSolid;
        this.emitter = isEmitter;
        this.levelColour = levelColour;
        tiles[id] = this;
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

    public int getLevelColor() {
        return levelColour;
    }

    public abstract void update();

    public abstract void render(Screen screen, Level level, int x, int y);
	
}
