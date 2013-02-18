package com.boidzgame.gameplay.component.container;

import com.boidzgame.gameplay.entity.Food;

public class FoodContainer extends BoidzContainer<Food> {

	@Override
	public void tick(double delay) {
		//TODO add food on tap
	}

	public void cleanFood() {
		for (Food f : boids) {
			f.clean();
		}
		boids.clear();
	}

}
