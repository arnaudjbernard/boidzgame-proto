package com.boidzgame.gameplay.boidz.entity;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.boidz.level.Level;

public class Food {
    public CircleRenderer renderer;
    public Coordinates coordinates;

    public void setup(Level level) {
        coordinates = new Coordinates();
        renderer = new CircleRenderer(0xffffffcc, 0);
        renderer.setup(level, coordinates);
    }

    public void clean() {
        renderer.clean();
        coordinates = null;
        renderer = null;
    }

}
