package com.boidzgame.gameplay.boidz.entity;

import com.boidzgame.R;
import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.boidz.component.ticker.DogTicker;
import com.boidzgame.gameplay.boidz.level.Level;

public class Dog {
    public DogTicker ticker;
    public Coordinates coordinates;
    private DrawableRenderer mRenderer;

    public void setup(Level level) {
        coordinates = new Coordinates();
        mRenderer = new DrawableRenderer(R.drawable.dog, 20);
        ticker = new DogTicker();
        mRenderer.setup(level, coordinates);
        ticker.setup(level, coordinates);
    }

    public void clean() {
        mRenderer.clean();
        ticker.clean();
        coordinates = null;
        mRenderer = null;
        ticker = null;
    }
}
