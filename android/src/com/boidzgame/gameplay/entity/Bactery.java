package com.boidzgame.gameplay.entity;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.component.ticker.BacteryTicker;
import com.boidzgame.gameplay.level.Level;

public class Bactery {
	public CircleRenderer renderer;
	public BacteryTicker ticker;
	public Coordinates coordinates;
	public boolean isGood;

	public void setup(Level level, boolean isGood) {
		this.isGood = isGood;
		coordinates = new Coordinates();
		if(isGood)
			renderer = new CircleRenderer(0xff008800, 0);
		else
			renderer = new CircleRenderer(0xff990000, 0);
		ticker = new BacteryTicker();
		renderer.setup(level, coordinates);
		ticker.setup(level, coordinates);
	}

	public void clean() {
		renderer.clean();
		ticker.clean();
		coordinates = null;
		renderer = null;
		ticker = null;
	}

}
