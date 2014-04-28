package com.boidzgame.gameplay.boidz.component.ticker;

import com.boidzgame.gameplay.Finger;
import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.util.Position;

public class DogTicker extends TickerComponent {
    /**
     * viscous friction
     */
    private static final double fv = 10.0d;
    // /** center point attraction force */
    // private static final double cptF = 0.20d;
    // /** brown agitation when finger is up */
    // private static final double brown = 300.0d;
    // /** shoaling attraction force */
    // private static final double shoalingF = 0.01d;
    // /** schooling propulsion force */
    // private static final double schoolingF = 100.0d;
    // /** radius of the aggregation */
    // private static final double aggregationRadius = 200d;
    // /** distance from which the shark is seen */
    // private static final double fearDist = 100d;
    // /** repulsive force of the predator */
    // private static final double fearF = 3.0d;

    /**
     * mass of a particle
     */
    private static final double mass = 1.0d;
    /**
     * finger attraction force
     */
    private static final double fingerF = 5.0d;
    /**
     * maximum speed
     */
    private static final double maxSpeed = 60d;
    public Finger finger;
    /**
     * this particle coordinates to update on tick
     */
    private Coordinates mCoordinates;
    private Position acc = new Position();
    // private Position centerAcc = new Position();
    // private Position globalSpeedAcc = new Position();
    // private Position fearAcc = new Position();
    // private double accNorm;
    private double speedNorm;
    // private Position centerOfMass;
    // private double distFromCenter;
    // private double distFromShark;

    /**
     * random for brown agitation
     */
    // private Random mRand;
    public void setup(Level level, Coordinates coordinates) {
        super.setup(level);
        // this.mParticles = PartzActivity.instance.particles;
        this.mCoordinates = coordinates;
        // mRand = new Random();
    }

    @Override
    public void tick(double delay) {
        // Log.d(TAG, "onTick");
        acc.x = 0;
        acc.y = 0;
        double sx = mCoordinates.speedX;
        double sy = mCoordinates.speedY;
        double x = mCoordinates.positionX;
        double y = mCoordinates.positionY;

        if (finger != null) {
            acc.x = (finger.x - x) * fingerF;
            acc.y = (finger.y - y) * fingerF;
        } else {
            // friction
            acc.x -= sx * fv;
            acc.y -= sy * fv;
        }

        // dynamic
        sx += acc.x * delay / mass;
        sy += acc.y * delay / mass;

        speedNorm = Math.sqrt(sx * sx + sy * sy);

        // max speed
        if (speedNorm > maxSpeed) {
            sx = sx * maxSpeed / speedNorm;
            sy = sy * maxSpeed / speedNorm;
        }

        // cinematic
        x += sx * delay;
        y += sy * delay;

        mCoordinates.speedX = sx;
        mCoordinates.speedY = sy;
        mCoordinates.positionX = x;
        mCoordinates.positionY = y;
        // Log.d(TAG, "acc "+acc.x+" - "+acc.y);
        // Log.d(TAG,
        // "spe "+mCoordinates.speedX+" - "+mCoordinates.speedY);
        // Log.d(TAG, "pos "+coordinates.pos.x+" - "+coordinates.pos.y);
    }
}
