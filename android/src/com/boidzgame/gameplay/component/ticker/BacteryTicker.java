package com.boidzgame.gameplay.component.ticker;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.level.Level;

public class BacteryTicker extends TickerComponent {
	
	private Coordinates mCoordinates;

	public void setup(Level level, Coordinates coordinates) {
		super.setup(level);
		this.mCoordinates = coordinates;
	}

	@Override
	public void tick(double delay) {
		// TODO eat nearby food
		// TODO reproduce if big enough
		// TODO get scared by fingers
		// TODO get attracted by nearest food
		// TODO dynamics
	}

}
