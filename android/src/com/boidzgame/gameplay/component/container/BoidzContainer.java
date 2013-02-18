package com.boidzgame.gameplay.component.container;

import java.util.ArrayList;
import java.util.List;

import com.boidzgame.gameplay.component.ticker.TickerComponent;

public abstract class BoidzContainer<BoidClass> extends TickerComponent {
	public List<BoidClass> boids = new ArrayList<BoidClass>();

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