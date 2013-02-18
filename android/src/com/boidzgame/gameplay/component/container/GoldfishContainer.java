package com.boidzgame.gameplay.component.container;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.entity.Goldfish;
import com.boidzgame.gameplay.entity.Shark;
import com.boidzgame.util.Position;

public class GoldfishContainer extends BoidzContainer<Goldfish> {
	public Position centersOfMass = new Position();
	public Position globalSpeed = new Position();

	public GoldfishContainer() {
		super();
		priority = -1;
	}

	@Override
	public void tick(double delay) {
		double totalMassX = 0;
		double totalMassY = 0;
		double totalSpeedX = 0;
		double totalSpeedY = 0;
		for (Goldfish particle : boids) {
			totalMassX += particle.coordinates.positionX;
			totalMassY += particle.coordinates.positionY;
			totalSpeedX += particle.coordinates.speedX;
			totalSpeedY += particle.coordinates.speedY;
		}
		int length = boids.size();
		centersOfMass.x = totalMassX / length;
		centersOfMass.y = totalMassY / length;
		globalSpeed.x = totalSpeedX / length;
		globalSpeed.y = totalSpeedY / length;
	}

	public Goldfish getClosest(Coordinates coordinates) {
		Goldfish closest = null;
		double x = coordinates.positionX;
		double y = coordinates.positionY;
		double minDist = Double.MAX_VALUE;
		double dx;
		double dy;
		double dist;
		for (Goldfish boid : boids) {
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

	public void setupParticles(GoldfishContainer goldfishContainer, Shark shark) {
		for (Goldfish particle : boids) {
			particle.setup(mLevel, goldfishContainer, shark);
		}
	}

	public void cleanParticles() {
		for (Goldfish particle : boids) {
			particle.clean();
		}
		boids.clear();
	}

	@Override
	public void remove(Goldfish goldfish) {
		goldfish.clean();
		boids.remove(goldfish);
	}
}
