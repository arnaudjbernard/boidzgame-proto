package com.boidzgame.gameplay.component.container;

import java.util.List;
import java.util.Random;

import com.boidzgame.gameplay.Finger;
import com.boidzgame.gameplay.entity.Food;
import com.boidzgame.gameplay.level.Level;

public class FoodContainer extends BoidzContainer<Food> {

	private final int FOOD_PER_FINGER = 3;
	private final double FOOD_DISPERSION = 10.0d;
	private Random mRandom;

	@Override
	public void setup(Level level) {
		super.setup(level);
		this.mRandom = new Random();
	}

	@Override
	public void tick(double delay) {
		List<Finger> fingers = mLevel.touchManager.fingersList;
		for (Finger finger : fingers) {
			if (finger.age == 0.0d) {
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
