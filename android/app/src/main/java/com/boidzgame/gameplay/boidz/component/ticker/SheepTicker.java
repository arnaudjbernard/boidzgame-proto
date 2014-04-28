package com.boidzgame.gameplay.boidz.component.ticker;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.DogContainer;
import com.boidzgame.gameplay.boidz.component.container.SheepContainer;
import com.boidzgame.gameplay.boidz.entity.Dog;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.util.MathUtil;
import com.boidzgame.util.Position;

import java.util.Random;

public class SheepTicker extends TickerComponent {
    // private static final String TAG = "SheepTicker";

    /**
     * viscous friction
     */
    private static final double fv = 2.0d;
    /**
     * brown agitation when finger is up
     */
    private static final double brown = 600.0d;
    // /** center point attraction force */
    // private static final double cptF = 0.20d;
    /**
     * brown delay
     */
    private static final int brownDt = 2000;
    /**
     * mass of a particle
     */
    private static final double mass = 1.0d;
    /**
     * maximum speed
     */
    private static final double maxSpeed = 20d;
    /**
     * radius of the aggregation
     */
    private static final double aggregationRadius = 20d;
    /**
     * outside radius of the aggregation
     */
    private static final double aggregationOutRadius = 120d;
    /**
     * shoaling attraction force
     */
    private static final double aggregationF = 20.0d;
    /**
     * schooling propulsion force
     */
    private static final double schoolingF = 1.7d;
    /**
     * distance from which the shark is seen
     */
    private static final double fearDist = 90d;
    /**
     * repulsive force of the predator
     */
    private static final double fearF = 2.8d;
    /**
     * repulsion from the bounds
     */
    private static final double fieldSize = 400d;
    /**
     * repulsion from the bounds
     */
    private static final double boundsF = 20d;
    public Boolean inFlock = true;
    /**
     * this particle coordinates to update on tick
     */
    private Coordinates mCoordinates;
    /**
     * the sheep manager
     */
    private SheepContainer mSheepContainer;
    /**
     * the dog manager
     */
    private DogContainer mDogContainer;
    /**
     * random for brown agitation
     */
    private Random mRand;
    private Position acc = new Position();
    private Position currentBrown = new Position(0, 0);
    private int lastBrown = 0;

    public void setup(Level level, Coordinates coordinates, SheepContainer sheepContainer,
                      DogContainer dogContainer) {
        super.setup(level);
        // this.mParticles = PartzActivity.instance.particles;
        this.mCoordinates = coordinates;
        this.mSheepContainer = sheepContainer;
        this.mDogContainer = dogContainer;
        this.mRand = new Random();
    }

    @Override
    public void clean() {
        this.mCoordinates = null;
        this.mDogContainer = null;
        this.mSheepContainer = null;
        this.mRand = null;
        super.clean();
    }

    @Override
    public void tick(double delay) {
        // Log.d(TAG, "onTick");
        double x = mCoordinates.positionX;
        double y = mCoordinates.positionY;
        double sx = mCoordinates.speedX;
        double sy = mCoordinates.speedY;

        // aggregation radius
        Position centerOfMass = mSheepContainer.centersOfMass;
        double distFromCenter = MathUtil.dist(centerOfMass.x, centerOfMass.y, x, y);
        // Log.d(TAG, "distFromCenter " + distFromCenter);

        // attracted by the flock
        if (distFromCenter < aggregationRadius || distFromCenter > aggregationOutRadius) {
            acc.x = 0;
            acc.y = 0;
        } else {
            acc.x = MathUtil.signum(centerOfMass.x - x) * aggregationF * distFromCenter
                    * distFromCenter / (aggregationOutRadius * aggregationOutRadius);
            acc.y = MathUtil.signum(centerOfMass.y - y) * aggregationF * distFromCenter
                    * distFromCenter / (aggregationOutRadius * aggregationOutRadius);
        }

        // schooling
        if (distFromCenter < aggregationOutRadius) {
            inFlock = true;
            Position globalSpeed = mSheepContainer.globalSpeed;
            acc.x += schoolingF * globalSpeed.x;
            acc.y += schoolingF * globalSpeed.y;
        } else {
            inFlock = false;
        }

        // repulsion from bounds
        if (x < -fieldSize * 0.5)
            acc.x += boundsF;
        else if (x > fieldSize * 0.5)
            acc.x -= boundsF;

        if (y < -fieldSize * 0.5)
            acc.y += boundsF;
        else if (y > fieldSize * 0.5)
            acc.y -= boundsF;

        // Brownian motion
        if (lastBrown < 0) {
            lastBrown = (int) (brownDt * (mRand.nextDouble() + 0.5d));
            double rx = mRand.nextDouble() - 0.5d;
            double ry = mRand.nextDouble() - 0.5d;
            if (rx * rx + ry * ry > 0.3d) {
                currentBrown.x = rx * rx * rx * brown;
                currentBrown.y = ry * ry * ry * brown;
            } else {
                currentBrown.x = 0;
                currentBrown.y = 0;
            }
        } else {
            lastBrown -= delay;
            currentBrown.x *= 1.0d - delay / brownDt;
            currentBrown.y *= 1.0d - delay / brownDt;
        }
        acc.x += currentBrown.x;
        acc.y += currentBrown.y;

        // friction
        acc.x -= sx * fv;
        acc.y -= sy * fv;

        // fear of dogs
        double distFromDog;
        double dx;
        double dy;
        for (int i = mDogContainer.boids.size() - 1; i >= 0; i--) {
            Dog dog = mDogContainer.boids.get(i);
            dx = x - dog.coordinates.positionX;
            dy = y - dog.coordinates.positionY;
            distFromDog = Math.sqrt(dx * dx + dy * dy);
            if (distFromDog < fearDist) {
                acc.x += fearF * MathUtil.signum(dx) * (fearDist - distFromDog)
                        * (fearDist - distFromDog) / fearDist;
                acc.y += fearF * MathUtil.signum(dy) * (fearDist - distFromDog)
                        * (fearDist - distFromDog) / fearDist;
                if (distFromDog < fearDist / 3) {
                    currentBrown.x = 0;
                    currentBrown.y = 0;
                }
            }
        }

        // dynamic
        sx += acc.x * delay / mass;
        sy += acc.y * delay / mass;
        double speedNorm = Math.sqrt(sx * sx + sy * sy);
        // max speed
        if (speedNorm > maxSpeed) {
            sx = sx * maxSpeed / speedNorm;
            sy = sy * maxSpeed / speedNorm;
        }
        mCoordinates.speedX = sx;
        mCoordinates.speedY = sy;
        x += sx * delay;
        y += sy * delay;
        mCoordinates.positionX = x;
        mCoordinates.positionY = y;
        // Log.d(TAG, "acc " + acc.x + " - " + acc.y);
        // Log.d(TAG,
        // "spe " + mCoordinates.speedX + " - "
        // + mCoordinates.speedY);
        // Log.d(TAG, "pos " + mCoordinates.positionX + " - " +
        // mCoordinates.positionY);
    }
}