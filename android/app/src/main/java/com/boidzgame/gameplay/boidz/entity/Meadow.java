package com.boidzgame.gameplay.boidz.entity;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.SheepContainer;
import com.boidzgame.gameplay.boidz.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.boidz.component.ticker.MeadowTicker;
import com.boidzgame.gameplay.boidz.level.Level;

public class Meadow {
    public MeadowTicker ticker;
    public Coordinates coordinates;
    public int radius;
    private CircleRenderer mRenderer;

    public void setup(Level level, SheepContainer sheepContainer) {
        coordinates = new Coordinates();
        mRenderer = new CircleRenderer(0xff008800, 0);
        ticker = new MeadowTicker();
        coordinates.width = radius;
        mRenderer.setup(level, coordinates);
        ticker.setup(level, coordinates, sheepContainer);
    }

    public void clean() {
        mRenderer.clean();
        ticker.clean();
        coordinates = null;
        mRenderer = null;
        ticker = null;
    }

}
