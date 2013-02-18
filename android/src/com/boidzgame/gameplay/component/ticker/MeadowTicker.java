package com.boidzgame.gameplay.component.ticker;

import android.os.Message;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.SheepContainer;
import com.boidzgame.gameplay.entity.Sheep;
import com.boidzgame.gameplay.level.Level;
import com.boidzgame.util.MathUtil;

public class MeadowTicker extends TickerComponent {
	public static final int MESSAGE_SHEEP_IN = 0;
	/** this particle coordinates to update on tick */
	private Coordinates mCoordinates;
	private SheepContainer mSheepContainer;

	public void setup(Level level, Coordinates coordinates, SheepContainer sheepContainer) {
		super.setup(level);
		// this.mParticles = PartzActivity.instance.particles;
		this.mSheepContainer = sheepContainer;
		this.mCoordinates = coordinates;
	}

	@Override
	public void clean() {
		this.mCoordinates = null;
		this.mSheepContainer = null;
		super.clean();
	}

	@Override
	public void tick(double delay) {
		for (int i = mSheepContainer.boids.size() - 1; i >= 0; i--) {
			Sheep sheep = mSheepContainer.boids.get(i);
			if (MathUtil.dist(mCoordinates.positionX, mCoordinates.positionY,
					sheep.coordinates.positionX, sheep.coordinates.positionY) < this.mCoordinates.width) {
				mSheepContainer.remove(sheep);
				Message msg = mLevel.handler.obtainMessage(MESSAGE_SHEEP_IN);
				mLevel.handler.sendMessage(msg);
			}
		}
	}
}
