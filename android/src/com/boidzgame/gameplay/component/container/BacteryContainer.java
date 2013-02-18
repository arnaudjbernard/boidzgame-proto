package com.boidzgame.gameplay.component.container;

import com.boidzgame.gameplay.entity.Bactery;

public class BacteryContainer extends BoidzContainer<Bactery> {

	public static final int MESSAGE_BACTERY_COUNT_UPDATE = 0;

	@Override
	public void tick(double delay) {

	}

	public void setupBacteries(int goodBacteryInitialCount, int badBacteryInitialCount) {
		for (int i = goodBacteryInitialCount + badBacteryInitialCount; i >= 0; i--) {
			boids.get(i).setup(mLevel, i >= badBacteryInitialCount);
		}
	}

	public void cleanBacteries() {
		for (Bactery b : boids) {
			b.clean();
		}
		boids.clear();
	}

	public int getGoodBacteriesCount() {
		int count = 0;
		for (Bactery b : boids) {
			if(b.isGood)
				count++;
		}
		return count;
	}

	public int getBadBacteriesCount() {
		int count = 0;
		for (Bactery b : boids) {
			if(!b.isGood)
				count++;
		}
		return count;
	}

}
