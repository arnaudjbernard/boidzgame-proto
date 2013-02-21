package com.boidzgame.gameplay.component.ticker;

import java.util.Iterator;
import java.util.Random;

import android.util.Log;

import com.boidzgame.gameplay.Finger;
import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.BacteryContainer;
import com.boidzgame.gameplay.component.container.FoodContainer;
import com.boidzgame.gameplay.entity.Bactery;
import com.boidzgame.gameplay.entity.Food;
import com.boidzgame.gameplay.level.Level;
import com.boidzgame.util.MathUtil;
import com.boidzgame.util.Position;

public class BacteryTicker extends TickerComponent {
	private static final String TAG = "BacteryTicker";

	// private final double EATING_DIST_SQUARE = 16.0d;
	private final int REPRODUCTION_THRESHOLD = 5;

	public static final double HUNGER_THRESHOLD = 30.0d;
	public static final double INITIAL_HUNGER_RANGE = HUNGER_THRESHOLD * 0.5d;

	private final double REPRODUCTION_SPEED_IMPULSE = 40.0;
	/** brown agitation when finger is up */
	private static final double brown = 300.0d;
	/** viscous friction */
	private static final double fv = 0.01d;
	/** mass of a particle */
	private static final double mass = 1.0d;
	/** maximum speed */
	private static final double maxSpeed = 40d;
	/** small distance to avoid divide by 0 */
	private static final double epsDist = 0.01d;
	/** food attraction */
	private static final double fFood = 1000d;
	/** finger repulsion */
	private static final double fFinger = 5000d;

	private Bactery mBactery;
	private Coordinates mCoordinates;
	private FoodContainer mFoodContainer;
	private BacteryContainer mBacteryContainer;
	/** random for brown agitation */
	private Random mRand;

	public void setup(Level level, Bactery bactery, FoodContainer foodContainer,
			BacteryContainer bacteryContainer) {
		super.setup(level);
		this.mBactery = bactery;
		this.mCoordinates = bactery.coordinates;
		this.mFoodContainer = foodContainer;
		this.mBacteryContainer = bacteryContainer;
		this.mRand = new Random();
	}

	@Override
	public void clean() {
		super.clean();
		this.mBactery = null;
		this.mCoordinates = null;
		this.mFoodContainer = null;
		this.mBacteryContainer = null;
	}

	private final Position acc = new Position();

	@Override
	public void tick(double delay) {
		// kill too hungry bacteries
		if (mBactery.hunger > HUNGER_THRESHOLD) {
			mBacteryContainer.boids.remove(mBactery);
			mBactery.clean();
			return;
		}
		mBactery.hunger += delay;

		acc.x = 0;
		acc.y = 0;

		// eat nearby food
		if (mFoodContainer.boids.size() > 0) {
			Iterator<Food> foodIt = mFoodContainer.boids.iterator();
			while (foodIt.hasNext()) {
				Food food = foodIt.next();
				if (MathUtil.distSquare(food.coordinates, mCoordinates) <= mCoordinates.width
						* mCoordinates.width) {
					foodIt.remove();
					food.clean();
					this.mBactery.foodEatenCount += 1;
					this.mBactery.hunger = 0;
					Log.i(TAG, "NUM");
				}
			}
		}

		// reproduce if big enough
		if (this.mBactery.foodEatenCount > REPRODUCTION_THRESHOLD) {
			// reset this bactery
			this.mBactery.hunger = 0;
			this.mBactery.foodEatenCount = 0;

			// create a new one
			Bactery newBorn = mBactery.duplicate();
			mBacteryContainer.add(newBorn);

			// give opposite speed impulse
			double speedX = mCoordinates.speedX;
			double speedY = mCoordinates.speedY;
			double norm = MathUtil.dist(speedX, speedX, speedY, speedY);

			mCoordinates.speedX -= speedY * REPRODUCTION_SPEED_IMPULSE / norm;
			mCoordinates.speedX += speedX * REPRODUCTION_SPEED_IMPULSE / norm;

			newBorn.coordinates.speedX += speedY * REPRODUCTION_SPEED_IMPULSE / norm;
			newBorn.coordinates.speedX -= speedX * REPRODUCTION_SPEED_IMPULSE / norm;
		}
		// get scared by fingers
		for (Finger finger : mLevel.touchManager.fingersList) {
			double fearDirX = finger.x - mCoordinates.positionX;
			double fearDirY = finger.y - mCoordinates.positionY;
			double fearAccNorm = epsDist
					+ MathUtil.dist(mCoordinates.positionX, mCoordinates.positionY, finger.x,
							finger.y);
			acc.x -= fFinger * fearDirX / (fearAccNorm * fearAccNorm * fearAccNorm);
			acc.y -= fFinger * fearDirY / (fearAccNorm * fearAccNorm * fearAccNorm);
		}

		// get attracted by nearest food
		for (Food food : mFoodContainer.boids) {
			double foodDirX = food.coordinates.positionX - mCoordinates.positionX;
			double foodDirY = food.coordinates.positionY - mCoordinates.positionY;
			double foodDirNorm = epsDist
					+ MathUtil.dist(mCoordinates.positionX, mCoordinates.positionY,
							food.coordinates.positionX, food.coordinates.positionY);
			acc.x += fFood * foodDirX / (foodDirNorm * foodDirNorm);
			acc.y += fFood * foodDirY / (foodDirNorm * foodDirNorm);
		}

		// Brownian motion
		acc.x += (mRand.nextDouble() - 0.5d) * brown;
		acc.y += (mRand.nextDouble() - 0.5d) * brown;

		// dynamics

		// friction
		acc.x -= mCoordinates.speedX * fv;
		acc.y -= mCoordinates.speedY * fv;

		// dynamic
		mCoordinates.speedX = mCoordinates.speedX + acc.x * delay / mass;
		mCoordinates.speedY = mCoordinates.speedY + acc.y * delay / mass;
		double speedNorm = Math.sqrt(mCoordinates.speedX * mCoordinates.speedX
				+ mCoordinates.speedY * mCoordinates.speedY);
		// max speed
		if (speedNorm > maxSpeed) {
			mCoordinates.speedX = mCoordinates.speedX * maxSpeed / speedNorm;
			mCoordinates.speedY = mCoordinates.speedY * maxSpeed / speedNorm;
		}
		mCoordinates.positionX = mCoordinates.positionX + mCoordinates.speedX * delay;
		mCoordinates.positionY = mCoordinates.positionY + mCoordinates.speedY * delay;
		int alpha = 255 - (int) (200 * this.mBactery.hunger / HUNGER_THRESHOLD);
		mBactery.coordinates.width = 3.0 + 3.0 * this.mBactery.foodEatenCount
				/ REPRODUCTION_THRESHOLD;
		mBactery.renderer.paint.setAlpha(alpha);
	}
}
