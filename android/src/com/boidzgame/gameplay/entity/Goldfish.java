package com.boidzgame.gameplay.entity;

import android.os.Bundle;

import com.boidzgame.R;
import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.GoldfishContainer;
import com.boidzgame.gameplay.component.renderer.DrawableRenderer;
import com.boidzgame.gameplay.component.ticker.GoldfishTicker;
import com.boidzgame.gameplay.level.Level;

public class Goldfish {
	private DrawableRenderer mRenderer;
	private GoldfishTicker mTicker;
	public Coordinates coordinates;

	public void setup(Level level, GoldfishContainer goldfishContainer, Shark shark) {
		coordinates = new Coordinates();
		mRenderer = new DrawableRenderer(R.drawable.goldfish, 10);
		mTicker = new GoldfishTicker();
		mRenderer.setup(level, coordinates);
		mTicker.setup(level, goldfishContainer, coordinates, shark);
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
