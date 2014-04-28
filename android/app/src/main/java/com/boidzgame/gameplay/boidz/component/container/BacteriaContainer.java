package com.boidzgame.gameplay.boidz.component.container;

import com.boidzgame.gameplay.boidz.component.ticker.BacteriaTicker;
import com.boidzgame.gameplay.boidz.entity.Bacteria;

import java.util.Random;

public class BacteriaContainer extends BoidzContainer<Bacteria> {

    public static final int MESSAGE_BACTERIA_COUNT_UPDATE = 0;


    @Override
    public void tick(double delay) {
    }

    public void setupBacterias(int goodBacteriaInitialCount, int badBacteriaInitialCount,
                               FoodContainer foodContainer, BacteriaContainer bacteriaContainer) {
        Random rand = new Random();
        for (int i = goodBacteriaInitialCount + badBacteriaInitialCount - 1; i >= 0; i--) {
            double hunger = rand.nextDouble() * BacteriaTicker.INITIAL_HUNGER_RANGE;
            boids.get(i).setup(mLevel, i >= badBacteriaInitialCount, hunger, foodContainer,
                    bacteriaContainer);
        }
    }

    public void cleanBacterias() {
        for (Bacteria b : boids) {
            b.clean();
        }
        boids.clear();
    }

    public int getGoodBacteriasCount() {
        int count = 0;
        for (Bacteria b : boids) {
            if (b.isGood)
                count++;
        }
        return count;
    }

    public int getBadBacteriasCount() {
        int count = 0;
        for (Bacteria b : boids) {
            if (!b.isGood)
                count++;
        }
        return count;
    }

}
