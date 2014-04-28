package com.boidzgame.gameplay.boidz.component.container;

import com.boidzgame.gameplay.Finger;
import com.boidzgame.gameplay.boidz.entity.Food;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.util.MathUtil;

import java.util.Random;

public class FoodContainer extends BoidzContainer<Food> {

    private final int FOOD_PER_FINGER = 3;
    private final double FOOD_DISPERSION = 16.0d;
    private final int MAX_FOOD_AMOUNT = 201;
    private final double FINGER_MAX_DISPLACEMENT = 32.0d;
    private final double FINGER_MAX_AGE = 1.0d;
    private Random mRandom;

    @Override
    public void setup(Level level) {
        super.setup(level);
        this.mRandom = new Random();
    }

    @Override
    public void tick(double delay) {
        if (boids.size() >= MAX_FOOD_AMOUNT) {
            return;
        }
        for (Finger finger : mLevel.touchManager.removedFingersList) {
            if (finger.age < FINGER_MAX_AGE
                    && MathUtil.dist(finger.firstX, finger.firstY, finger.lastX, finger.lastY) < FINGER_MAX_DISPLACEMENT) {
                for (int i = 0; i < FOOD_PER_FINGER; i++) {
                    Food food = new Food();
                    boids.add(food);
                    food.setup(mLevel);
                    food.coordinates.width = 2.0;
                    food.coordinates.positionX = finger.x + mRandom.nextDouble() * FOOD_DISPERSION;
                    food.coordinates.positionY = finger.y + mRandom.nextDouble() * FOOD_DISPERSION;
                }
            }
        }
    }

    public void cleanFood() {
        for (Food f : boids) {
            f.clean();
        }
        boids.clear();
    }

}
