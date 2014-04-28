package com.boidzgame.gameplay.boidz.entity;

import com.boidzgame.R;
import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.GoldfishContainer;
import com.boidzgame.gameplay.boidz.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.boidz.component.ticker.SharkTicker;
import com.boidzgame.gameplay.boidz.level.Level;

public class Shark {
    public Coordinates coordinates;
    private DrawableRenderer mRenderer;
    private SharkTicker mTicker;

    public void setup(Level level, GoldfishContainer goldfishContainer) {
        coordinates = new Coordinates();
        mRenderer = new DrawableRenderer(R.drawable.shark, 20);
        mTicker = new SharkTicker();
        coordinates.positionX = 100;
        coordinates.positionY = 100;
        mRenderer.setup(level, coordinates);
        mTicker.setup(level, coordinates, goldfishContainer);
    }

    public void clean() {
        mRenderer.clean();
        mTicker.clean();
        coordinates = null;
        mRenderer = null;
        mTicker = null;
    }
}
