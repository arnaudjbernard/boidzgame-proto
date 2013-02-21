package com.boidzgame.gameplay.entity;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.container.BacteryContainer;
import com.boidzgame.gameplay.component.container.FoodContainer;
import com.boidzgame.gameplay.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.component.ticker.BacteryTicker;
import com.boidzgame.gameplay.level.Level;

public class Bactery {

	public CircleRenderer renderer;
	public BacteryTicker ticker;
	public Coordinates coordinates;
	public Level level;
	public FoodContainer foodContainer;
	public BacteryContainer bacteryContainer;
	public int foodEatenCount;
	public boolean isGood;
	public double hunger;

	public void setup(Level level, boolean isGood, double hunger, FoodContainer foodContainer,
			BacteryContainer bacteryContainer) {
		this.foodContainer = foodContainer;
		this.bacteryContainer = bacteryContainer;
		this.foodEatenCount = 0;
		this.isGood = isGood;
		this.hunger = hunger;
		this.level = level;

		coordinates = new Coordinates();
		coordinates.width = 4;
		if (isGood)
			renderer = new CircleRenderer(0xff008800, 0);
		else
			renderer = new CircleRenderer(0xff990000, 0);
		ticker = new BacteryTicker();
		renderer.setup(level, coordinates);
		ticker.setup(level, this, foodContainer, bacteryContainer);
	}

	public void clean() {
		renderer.clean();
		ticker.clean();
		coordinates = null;
		renderer = null;
		ticker = null;
	}

	public Bactery duplicate() {
		Bactery newBorn = new Bactery();
		newBorn.setup(level, isGood, 0, foodContainer, bacteryContainer);
		newBorn.coordinates.positionX = this.coordinates.positionX;
		newBorn.coordinates.positionY = this.coordinates.positionY;
		newBorn.coordinates.speedX = this.coordinates.speedX;
		newBorn.coordinates.speedY = this.coordinates.speedY;
		return newBorn;
	}

}
