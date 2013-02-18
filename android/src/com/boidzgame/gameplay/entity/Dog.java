package com.boidzgame.gameplay.entity;

import com.boidzgame.R;
import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.component.ticker.DogTicker;
import com.boidzgame.gameplay.level.Level;

public class Dog {
	private DrawableRenderer mRenderer;
	public DogTicker ticker;
	public Coordinates coordinates;

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
