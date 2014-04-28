package com.boidzgame.gameplay.boidz.component.ticker;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.GoldfishContainer;
import com.boidzgame.gameplay.boidz.entity.Shark;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.util.Position;

import java.util.Random;

public class GoldfishTicker extends TickerComponent {
    @SuppressWarnings("unused")
    private static final String TAG = "ParticleTicker";
    /**
     * viscous friction
     */
    private static final double fv = 2.0d;
    /**
     * center point attraction force
     */
    private static final double cptF = 0.20d;
    /**
     * brown agitation when finger is up
     */
    private static final double brown = 300.0d;
    /**
     * shoaling attraction force
     */
    private static final double shoalingF = 0.01d;
    /**
     * schooling propulsion force
     */
    private static final double schoolingF = 100.0d;
    /**
     * mass of a particle
     */
    private static final double mass = 1.0d;
    /**
     * maximum speed
     */
    private static final double maxSpeed = 1000d;
    /**
     * radius of the aggregation
     */
    private static final double aggregationRadius = 200d;
    /**
     * distance from which the shark is seen
     */
    private static final double fearDist = 100d;
    /**
     * repulsive force of the predator
     */
    private static final double fearF = 3.0d;
    // /** other particles */
    // private Particle[] mParticles;
    /**
     * this particle coordinates to update on tick
     */
    private Coordinates mCoordinates;
    /**
     * random for brown agitation
     */
    private Random mRand;
    private GoldfishContainer mGoldfishContainer;
    private Position acc = new Position();
    private Position centerAcc = new Position();
    private Position globalSpeedAcc = new Position();
    private Position fearAcc = new Position();
    private double accNorm;
    private double speedNorm;
    private Position centerOfMass;
    private double distFromCenter;
    private double distFromShark;
    private Shark mShark;

    public void setup(Level level, GoldfishContainer goldfishContainer, Coordinates coordinates,
                      Shark shark) {
        super.setup(level);
        // this.mParticles = PartzActivity.instance.particles;
        this.mCoordinates = coordinates;
        this.mGoldfishContainer = goldfishContainer;
        this.mShark = shark;
        this.mRand = new Random();
    }

    @Override
    public void clean() {
        mRand = null;
        this.mShark = null;
        this.mGoldfishContainer = null;
        this.mCoordinates = null;
        super.clean();
    }

    @Override
    public void tick(double delay) {
        // Log.d(TAG, "onTick");
        centerOfMass = mGoldfishContainer.centersOfMass;
        // play with the finger
        acc.x = centerOfMass.x - mCoordinates.positionX;
        acc.y = centerOfMass.y - mCoordinates.positionY;

        // aggregation radius
        distFromCenter = Math.sqrt(acc.x * acc.x + acc.y * acc.y);
        if (distFromCenter < aggregationRadius) {
            acc.x = 0;
            acc.y = 0;
        }
        acc.x *= shoalingF;
        acc.y *= shoalingF;

        globalSpeedAcc.x = mGoldfishContainer.globalSpeed.x;
        globalSpeedAcc.y = mGoldfishContainer.globalSpeed.y;
        accNorm = Math.sqrt(globalSpeedAcc.x * globalSpeedAcc.x + globalSpeedAcc.y
                * globalSpeedAcc.y);
        accNorm = accNorm > 0.01 ? accNorm : 0.01;
        acc.x += schoolingF * globalSpeedAcc.x / accNorm;
        acc.y += schoolingF * globalSpeedAcc.y / accNorm;

        // attraction to the center
        centerAcc.x = 0.0d - mCoordinates.positionX;
        centerAcc.y = 0.0d - mCoordinates.positionY;
        accNorm = Math.sqrt(centerAcc.x * centerAcc.x + centerAcc.y * centerAcc.y);
        accNorm = accNorm > 0.01 ? accNorm : 0.01;
        acc.x += centerAcc.x * cptF;
        acc.y += centerAcc.y * cptF;

        // Brownian motion
        acc.x += (mRand.nextDouble() - 0.5d) * brown;
        acc.y += (mRand.nextDouble() - 0.5d) * brown;

        // friction
        double flockBonus = (1 + 15 / (10 + mGoldfishContainer.boids.size()));
        acc.x -= mCoordinates.speedX * fv * flockBonus;
        acc.y -= mCoordinates.speedY * fv * flockBonus;

        // fear of shark
        fearAcc.x = mCoordinates.positionX - mShark.coordinates.positionX;
        fearAcc.y = mCoordinates.positionY - mShark.coordinates.positionY;
        distFromShark = Math.sqrt(fearAcc.x * fearAcc.x + fearAcc.y * fearAcc.y);
        if (distFromShark < fearDist) {
            acc.x += fearAcc.x * fearF * (fearDist - distFromShark) / fearDist;
            acc.y += fearAcc.y * fearF * (fearDist - distFromShark) / fearDist;
        }
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
