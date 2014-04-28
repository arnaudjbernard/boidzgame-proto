package com.boidzgame.gameplay.boidz.entity;

import com.boidzgame.R;
import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.DogContainer;
import com.boidzgame.gameplay.boidz.component.container.SheepContainer;
import com.boidzgame.gameplay.boidz.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.boidz.component.ticker.SheepTicker;
import com.boidzgame.gameplay.boidz.level.Level;

public class Sheep {
    public SheepTicker ticker;
    public Coordinates coordinates;
    private DrawableRenderer mRenderer;

    public void setup(Level level, SheepContainer sheepContainer, DogContainer dogContainer) {
        coordinates = new Coordinates();
        mRenderer = new DrawableRenderer(R.drawable.sheep, 10);
        ticker = new SheepTicker();
        mRenderer.setup(level, coordinates);
        ticker.setup(level, coordinates, sheepContainer, dogContainer);
    }

    public void clean() {
        mRenderer.clean();
        ticker.clean();
        coordinates = null;
        mRenderer = null;
        ticker = null;
    }
}
