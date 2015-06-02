package com.zormion.game.level.tiles;

import java.util.Random;


public class AnimatedTile extends BasicTile {

    private int[][] animationTileCoords;
    private int currentAnimationIndex;
    private long lastIterationTime;
    private int animationSwitchDelay;

    public AnimatedTile(int[][] animationCoords, int animationSwitchDelay) {
        super(animationCoords[0][0], animationCoords[0][1]);
        this.animationTileCoords = animationCoords;
        this.currentAnimationIndex = 0;
        this.lastIterationTime = System.currentTimeMillis();
        this.animationSwitchDelay = animationSwitchDelay;
        this.liquid = true;
    }
    
    Random random = new Random();
    int index = 1;
    public void update() {
    	
    	if(index == 1) 
    		index++;
    	
        if ((System.currentTimeMillis() - lastIterationTime) >= (animationSwitchDelay)) {
            lastIterationTime = System.currentTimeMillis();
            currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length;
            this.tileID = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1] * 32));
        }
    }
}
