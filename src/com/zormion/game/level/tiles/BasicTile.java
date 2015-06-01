package com.zormion.game.level.tiles;

import com.zormion.game.gfx.Screen;
import com.zormion.game.level.Level;

public class BasicTile extends Tile {

    protected int tileID;

    public BasicTile(int levelNumber, int x, int y) {
        super(levelNumber, false, false);
        this.tileID = x + y * 32;
    }

    public void update() {
    }

    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileID, 0x00, 1);
    }

}
