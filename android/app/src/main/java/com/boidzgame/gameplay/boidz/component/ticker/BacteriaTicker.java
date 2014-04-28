package com.boidzgame.gameplay.boidz.component.ticker;

import android.util.Log;

import com.boidzgame.gameplay.Finger;
import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.BacteriaContainer;
import com.boidzgame.gameplay.boidz.component.container.FoodContainer;
import com.boidzgame.gameplay.boidz.entity.Bacteria;
import com.boidzgame.gameplay.boidz.entity.Food;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.util.MathUtil;
import com.boidzgame.util.Position;

import java.util.Iterator;
import java.util.Random;

public class BacteriaTicker extends TickerComponent {
    public static final double HUNGER_THRESHOLD = 30.0d;
    public static final double INITIAL_HUNGER_RANGE = HUNGER_THRESHOLD * 0.5d;
    private static final String TAG = "BacteriaTicker";
    /**
     * brown agitation when finger is up
     */
    private static final double brown = 300.0d;
    /**
     * viscous friction
     */
    private static final double fv = 0.1d;
    /**
     * mass of a particle
     */
    private static final double mass = 1.0d;
    /**
     * maximum speed
     */
    private static final double maxSpeed = 40d;
    /**
     * small distance to avoid divide by 0
     */
    private static final double epsDist = 1.0d;
    /**
     * food attraction
     */
    private static final double fFood = 1000d;
    /**
     * finger repulsion
     */
    private static final double fFinger = 50000d;
    /**
     * finger repulsion distance
     */
    private static final double fFingerDist = 60d;
    // private final double EATING_DIST_SQUARE = 16.0d;
    private final int REPRODUCTION_THRESHOLD = 5;
    private final double REPRODUCTION_SPEED_IMPULSE = 40.0;
    private final Position acc = new Position();
    private Bacteria mBacteria;
    private Coordinates mCoordinates;
    private FoodContainer mFoodContainer;
    private BacteriaContainer mBacteriaContainer;
    /**
     * random for brown agitation
     */
    private Random mRand;

    public void setup(Level level, Bacteria bacteria, FoodContainer foodContainer,
                      BacteriaContainer bacteriaContainer) {
        super.setup(level);
        this.mBacteria = bacteria;
        this.mCoordinates = bacteria.coordinates;
        this.mFoodContainer = foodContainer;
        this.mBacteriaContainer = bacteriaContainer;
        this.mRand = new Random();
    }

    @Override
    public void clean() {
        super.clean();
        this.mBacteria = null;
        this.mCoordinates = null;
        this.mFoodContainer = null;
        this.mBacteriaContainer = null;
    }

    @Override
    public void tick(double delay) {
        // kill too hungry bacterias
        if (mBacteria.hunger > HUNGER_THRESHOLD) {
            mBacteriaContainer.boids.remove(mBacteria);
            mBacteria.clean();
            return;
        }
        mBacteria.hunger += delay;

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
                    this.mBacteria.foodEatenCount += 1;
                    this.mBacteria.hunger = 0;
                    Log.i(TAG, "NUM");
                }
            }
        }

        // reproduce if big enough
        if (this.mBacteria.foodEatenCount > REPRODUCTION_THRESHOLD) {
            // reset this bacterium
            this.mBacteria.hunger = 0;
            this.mBacteria.foodEatenCount = 0;

            // create a new one
            Bacteria newBorn = mBacteria.duplicate();
            mBacteriaContainer.add(newBorn);

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
            if (fFingerDist > fearAccNorm) {
                acc.x -= fFinger * fearDirX / (fearAccNorm * fearAccNorm);
                acc.y -= fFinger * fearDirY / (fearAccNorm * fearAccNorm);
            }
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
        int alpha = 255 - (int) (200 * this.mBacteria.hunger / HUNGER_THRESHOLD);
        mBacteria.coordinates.width = 3.0 + 3.0 * this.mBacteria.foodEatenCount
                / REPRODUCTION_THRESHOLD;
        mBacteria.renderer.paint.setAlpha(alpha);
    }
}
