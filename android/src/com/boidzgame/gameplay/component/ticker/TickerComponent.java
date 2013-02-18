package com.boidzgame.gameplay.component.ticker;

import com.boidzgame.gameplay.level.Level;
import com.boidzgame.gameplay.ticking.Ticker;

public abstract class TickerComponent extends Ticker {
	protected int priority = 0;
	protected Level mLevel;

	public void setup(Level level) {
		mLevel = level;
		mLevel.tickerManager.register(this);
	}

	public void clean() {
		mLevel.tickerManager.unregister(this);
		mLevel = null;
	}

	@Override
	public abstract void tick(double delay);
}
