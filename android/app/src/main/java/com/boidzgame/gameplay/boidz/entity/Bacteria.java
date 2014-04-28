package com.boidzgame.gameplay.boidz.entity;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.container.BacteriaContainer;
import com.boidzgame.gameplay.boidz.component.container.FoodContainer;
import com.boidzgame.gameplay.boidz.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.boidz.component.ticker.BacteriaTicker;
import com.boidzgame.gameplay.boidz.level.Level;

public class Bacteria {

    public CircleRenderer renderer;
    public BacteriaTicker ticker;
    public Coordinates coordinates;
    public Level level;
    public FoodContainer foodContainer;
    public BacteriaContainer bacteriaContainer;
    public int foodEatenCount;
    public boolean isGood;
    public double hunger;

    public void setup(Level level, boolean isGood, double hunger, FoodContainer foodContainer,
                      BacteriaContainer bacteriaContainer) {
        this.foodContainer = foodContainer;
        this.bacteriaContainer = bacteriaContainer;
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
        ticker = new BacteriaTicker();
        renderer.setup(level, coordinates);
        ticker.setup(level, this, foodContainer, bacteriaContainer);
    }

    public void clean() {
        renderer.clean();
        ticker.clean();
        coordinates = null;
        renderer = null;
        ticker = null;
    }

    public Bacteria duplicate() {
        Bacteria newBorn = new Bacteria();
        newBorn.setup(level, isGood, 0, foodContainer, bacteriaContainer);
        newBorn.coordinates.positionX = this.coordinates.positionX;
        newBorn.coordinates.positionY = this.coordinates.positionY;
        newBorn.coordinates.speedX = this.coordinates.speedX;
        newBorn.coordinates.speedY = this.coordinates.speedY;
        return newBorn;
    }

}
