package com.boidzgame.gameplay.entity;

import com.boidzgame.R;
import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.DogContainer;
import com.boidzgame.gameplay.component.container.SheepContainer;
import com.boidzgame.gameplay.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.component.ticker.SheepTicker;
import com.boidzgame.gameplay.level.Level;

public class Sheep {
	private DrawableRenderer mRenderer;
	public SheepTicker ticker;
	public Coordinates coordinates;

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
