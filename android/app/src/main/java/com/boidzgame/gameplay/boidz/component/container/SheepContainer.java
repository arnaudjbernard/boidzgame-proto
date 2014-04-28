package com.boidzgame.gameplay.boidz.component.container;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.entity.Sheep;
import com.boidzgame.util.Position;

public class SheepContainer extends BoidzContainer<Sheep> {
    public Position centersOfMass = new Position(0, 0);
    public Position globalSpeed = new Position(0, 0);

    public void setupSheeps(SheepContainer sheepContainer, DogContainer dogContainer) {
        for (Sheep s : boids) {
            s.setup(mLevel, sheepContainer, dogContainer);
        }
    }

    public void cleanSheeps() {
        for (Sheep s : boids) {
            s.clean();
        }
        boids.clear();
    }

    @Override
    public void tick(double delay) {
        double totalMassX = 0;
        double totalMassY = 0;
        double totalSpeedX = 0;
        double totalSpeedY = 0;
        int total = 0;
        for (int i = boids.size() - 1; i >= 0; i--) {
            Sheep sheep = boids.get(i);
            if (sheep.ticker.inFlock) {
                totalMassX += sheep.coordinates.positionX;
                totalMassY += sheep.coordinates.positionY;
                totalSpeedX += sheep.coordinates.speedX;
                totalSpeedY += sheep.coordinates.speedY;
                total++;
            }
        }
        if (total > 0) {
            centersOfMass.x = totalMassX / total;
            centersOfMass.y = totalMassY / total;
            globalSpeed.x = totalSpeedX / total;
            globalSpeed.y = totalSpeedY / total;
        }
    }

    public Sheep getClosest(Coordinates coordinates) {
        Sheep closest = null;
        double x = coordinates.positionX;
        double y = coordinates.positionY;
        double minDist = 100000;
        double dx;
        double dy;
        double dist;
        for (Sheep boid : boids) {
            dx = x - boid.coordinates.positionX;
            dy = y - boid.coordinates.positionY;
            dist = dx * dx + dy * dy;
            if (dist < minDist) {
                closest = boid;
                minDist = dist;
            }
        }
        return closest;
    }

    @Override
    public void remove(Sheep sheep) {
        sheep.clean();
        boids.remove(sheep);
    }
}
