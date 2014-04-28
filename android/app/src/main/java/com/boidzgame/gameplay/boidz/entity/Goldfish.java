package com.boidzgame.gameplay.boidz.entity;

import com.boidzgame.R;
import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.GoldfishContainer;
import com.boidzgame.gameplay.boidz.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.boidz.component.ticker.GoldfishTicker;
import com.boidzgame.gameplay.boidz.level.Level;

public class Goldfish {
    public Coordinates coordinates;
    private DrawableRenderer mRenderer;
    private GoldfishTicker mTicker;

    public void setup(Level level, GoldfishContainer goldfishContainer, Shark shark) {
        coordinates = new Coordinates();
        mRenderer = new DrawableRenderer(R.drawable.goldfish, 10);
        mTicker = new GoldfishTicker();
        mRenderer.setup(level, coordinates);
        mTicker.setup(level, goldfishContainer, coordinates, shark);
    }

    public void clean() {
        mRenderer.clean();
        mTicker.clean();
        coordinates = null;
        mRenderer = null;
        mTicker = null;
    }
}
