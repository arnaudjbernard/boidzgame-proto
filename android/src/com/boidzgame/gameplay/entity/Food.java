package com.boidzgame.gameplay.entity;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.level.Level;

public class Food {
	public CircleRenderer renderer;
	public Coordinates coordinates;

	public void setup(Level level) {
		coordinates = new Coordinates();
		renderer = new CircleRenderer(0xff008800, 0);
		renderer.setup(level, coordinates);
	}

	public void clean() {
		renderer.clean();
		coordinates = null;
		renderer = null;
	}

}
