package com.boidzgame.gameplay.component.container;

import java.util.ArrayList;
import java.util.List;

import com.boidzgame.R;
import com.boidzgame.gameplay.Finger;
import com.boidzgame.gameplay.entity.Dog;
import com.boidzgame.util.SoundManager;

public class DogContainer extends BoidzContainer<Dog> {

	public void setupDogs() {
		for (Dog d : boids) {
			d.setup(mLevel);
		}
		SoundManager.addSound(R.raw.dog_bark);
	}

	public void cleanDogs() {
		for (Dog d : boids) {
			d.clean();
		}
		boids.clear();
	}

	List<Dog> fingerlessDogs = new ArrayList<Dog>();
	List<Finger> takenFingers = new ArrayList<Finger>();

	@Override
	public void tick(double delay) {
		List<Finger> fingers = mLevel.touchManager.fingersList;
		fingerlessDogs.clear();

		// remove depleted fingers
		// compute takenFingers and fingerlessDogs
		for (int i = boids.size() - 1; i >= 0; i--) {
			Dog dog = boids.get(i);
			if (!fingers.contains(dog.ticker.finger)) {
				dog.ticker.finger = null;
				fingerlessDogs.add(dog);
			} else {
				if (dog.ticker.finger == null) {
					fingerlessDogs.add(dog);
				} else {
					takenFingers.add(dog.ticker.finger);
				}
			}
		}

		// if you are the closest fingerless dog to a finger, it becomes yours
		for (int i = fingers.size() - 1; i >= 0; i--) {
			Finger finger = fingers.get(i);
			if (takenFingers.contains(finger)) {
				continue;
			}
			Dog closest = null;
			double minDist = Double.MAX_VALUE;
			for (int j = fingerlessDogs.size() - 1; j >= 0; j--) {
				Dog dog = fingerlessDogs.get(j);
				double xDist = dog.coordinates.positionX - finger.x;
				double yDist = dog.coordinates.positionY - finger.y;
				double dist = xDist * xDist + yDist * yDist;
				if (dist < minDist) {
					closest = dog;
					minDist = dist;
				}
			}
			if (closest != null) {
				// Note: this may not be the closest dogless finger to this dog
				closest.ticker.finger = finger;
				SoundManager.playSound(R.raw.dog_bark, 0);
			}
		}
	}
}
