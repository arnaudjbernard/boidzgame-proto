package com.boidzgame.gameplay.boidz.component.container;

import com.boidzgame.gameplay.boidz.component.ticker.TickerComponent;

import java.util.LinkedList;
import java.util.List;

public abstract class BoidzContainer<BoidClass> extends TickerComponent {
    public List<BoidClass> boids = new LinkedList<BoidClass>();

    public BoidzContainer() {
        super();
    }

    public void add(BoidClass boid) {
        boids.add(boid);
    }

    public void remove(BoidClass boid) {
        boids.remove(boid);
    }

    @Override
    public abstract void tick(double delay);
}