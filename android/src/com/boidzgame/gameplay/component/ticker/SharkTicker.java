package com.boidzgame.gameplay.component.ticker;

import java.util.Random;

import android.os.Message;

import com.boidzgame.R;
import com.boidzgame.gameplay.Finger;
import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.GoldfishContainer;
import com.boidzgame.gameplay.entity.Goldfish;
import com.boidzgame.gameplay.level.Level;
import com.boidzgame.util.Position;
import com.boidzgame.util.SoundManager;

public class SharkTicker extends TickerComponent {
	@SuppressWarnings("unused")
	private static final String TAG = "ParticleTicker";
	/** viscous friction */
	private static final double fv = 1.0d;
	/** center of mass attraction force */
	private static final double hungerF = 0.7d;
	/** finger attraction force */
	private static final double fingerF = 1.0d;
	/** finger repulsion distance */
	private static final double fingerR = 20d;
	/** brown agitation when finger is down */
	private static final double fingerBrown = 500.0d;
	/** brown agitation when finger is up */
	private static final double brown = 200.0d;
	/** mass of a particle */
	private static final double mass = 1.8d;
	/** maximum speed */
	private static final double maxSpeed = 100d;
	private static final int oscillationPeriode = 20;
	private static final double oscillationAmpl = 2.0d;
	private static final double eatingDist = 150.0d;
	public static final int MESSAGE_GOLDFISH_EATEN = 0;

	/** coordinates to update on tick */
	private Coordinates mCoordinates;
	/** random for default behavior */
	private Random mRand;
	private GoldfishContainer mGoldfishContainer;

	public void setup(Level level, Coordinates coordinates, GoldfishContainer goldfishContainer) {
		super.setup(level);
		this.mCoordinates = coordinates;
		this.mGoldfishContainer = goldfishContainer;
		mRand = new Random();
		SoundManager.addSound(R.raw.shark_crunch);
	}

	@Override
	public void clean() {
		this.mCoordinates = null;
		this.mGoldfishContainer = null;
		super.clean();
	}

	private Position acc = new Position();
	private Position closestDist = new Position();
	private Finger finger;
	private double accNorm;
	private double speedNorm;
	private double coeff;
	private int oscillationCount = 0;
	private double oscillation;
	private Goldfish closest;

	@Override
	public void tick(double delay) {
		if (mLevel.touchManager.fingersList.size() > 0) {
			finger = mLevel.touchManager.fingersList.get(0);
			// play with the finger
			acc.x = finger.x - mCoordinates.positionX;
			acc.y = finger.y - mCoordinates.positionY;

			accNorm = Math.sqrt(acc.x * acc.x + acc.y * acc.y);
			coeff = 1.0;
			if (accNorm < fingerR)
				coeff = 2.0;
			acc.x -= acc.x * coeff * fingerR / accNorm;
			acc.y -= acc.y * coeff * fingerR / accNorm;

			acc.x *= fingerF;
			acc.y *= fingerF;

			// Brownian motion excited mode
			acc.x += (mRand.nextDouble() - 0.5d) * fingerBrown;
			acc.y += (mRand.nextDouble() - 0.5d) * fingerBrown;
		} else {
			// Brownian motion
			acc.x = (mRand.nextDouble() - 0.5d) * brown;
			acc.y = (mRand.nextDouble() - 0.5d) * brown;
		}

		closest = mGoldfishContainer.getClosest(mCoordinates);
		if (closest != null) {
			// eat closest one if in range
			closestDist.x = closest.coordinates.positionX - mCoordinates.positionX;
			closestDist.y = closest.coordinates.positionY - mCoordinates.positionY;
			if (closestDist.x * closestDist.x + closestDist.y * closestDist.y < eatingDist) {
				mGoldfishContainer.remove(closest);
				// Log.d(TAG, "Yummy");
				Message msg = mLevel.handler.obtainMessage(MESSAGE_GOLDFISH_EATEN);
				mLevel.handler.sendMessage(msg);
				SoundManager.playSound(R.raw.shark_crunch, 0);
			} else {
				// attraction to the closest
				acc.x += closestDist.x * hungerF;
				acc.y += closestDist.y * hungerF;
			}
		}

		oscillationCount++;
		oscillationCount %= oscillationPeriode;
		oscillation = Math.sin(2 * Math.PI * oscillationCount / oscillationPeriode)
				* oscillationAmpl;
		acc.x += -mCoordinates.speedY * oscillation;
		acc.y += mCoordinates.speedX * oscillation;
		// Log.d(TAG, "" + oscillation);

		// friction
		acc.x -= mCoordinates.speedX * fv;
		acc.y -= mCoordinates.speedY * fv;

		// dynamic
		mCoordinates.speedX = mCoordinates.speedX + acc.x * delay / mass;
		mCoordinates.speedY = mCoordinates.speedY + acc.y * delay / mass;
		speedNorm = Math.sqrt(mCoordinates.speedX * mCoordinates.speedX + mCoordinates.speedY
				* mCoordinates.speedY);
		// max speed
		if (speedNorm > maxSpeed) {
			mCoordinates.speedX = mCoordinates.speedX * maxSpeed / speedNorm;
			mCoordinates.speedY = mCoordinates.speedY * maxSpeed / speedNorm;
		}
		mCoordinates.positionX = mCoordinates.positionX + mCoordinates.speedX * delay;
		mCoordinates.positionY = mCoordinates.positionY + mCoordinates.speedY * delay;
		// Log.d(TAG, "acc " + acc.x + " - " + acc.y);
		// Log.d(TAG, "spe " + mCoordinates.speedX + " - " +
		// mCoordinates.speedY);
		// Log.d(TAG, "pos " + mCoordinates.positionX + " - " +
		// mCoordinates.positionY);
	}
}
