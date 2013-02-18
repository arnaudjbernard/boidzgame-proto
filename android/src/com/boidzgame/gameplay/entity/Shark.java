package com.boidzgame.gameplay.entity;

import android.os.Bundle;

import com.boidzgame.R;
import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.GoldfishContainer;
import com.boidzgame.gameplay.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.component.ticker.SharkTicker;
import com.boidzgame.gameplay.level.Level;

public class Shark {
	private DrawableRenderer mRenderer;
	private SharkTicker mTicker;
	public Coordinates coordinates;

	public void setup(Level level, GoldfishContainer goldfishContainer) {
		coordinates = new Coordinates();
		mRenderer = new DrawableRenderer(R.drawable.shark, 20);
		mTicker = new SharkTicker();
		coordinates.positionX = 100;
		coordinates.positionY = 100;
		mRenderer.setup(level, coordinates);
		mTicker.setup(level, coordinates, goldfishContainer);
	}

	public void clean() {
		mRenderer.clean();
		mTicker.clean();
		coordinates = null;
		mRenderer = null;
		mTicker = null;
	}

	public void save(Bundle outState) {
		// TODO Auto-generated method stub
	}

	public void restore(Bundle outState) {
		// TODO Auto-generated method stub
	}
}
