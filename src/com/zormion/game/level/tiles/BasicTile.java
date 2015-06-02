package com.zormion.game.level.tiles;

import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public class BasicTile extends Tile {

    protected int tileID;

    public BasicTile(int x, int y) {
        super(((y * 32) + x) + 1, false, false);
        this.tileID = x + y * 32;
    }

    public void update() {
    }

    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileID, 0x00, 1);
    }

}
