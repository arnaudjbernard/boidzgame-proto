package com.boidzgame.gameplay.rendering;

import android.graphics.Canvas;

public abstract class Renderer implements IRenderer {
	private int mLayer;

	public int getLayer() {
		return mLayer;
	}

	public void setLayer(int layer) {
		mLayer = layer;
	}

	public abstract void draw(int delay, Canvas canvas, double scaleX, double scaleY);
}
