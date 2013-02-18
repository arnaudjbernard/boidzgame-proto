package com.boidzgame.gameplay.entity;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.SheepContainer;
import com.boidzgame.gameplay.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.component.ticker.MeadowTicker;
import com.boidzgame.gameplay.level.Level;

public class Meadow {
	private CircleRenderer mRenderer;
	public MeadowTicker ticker;
	public Coordinates coordinates;
	public int radius;

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
