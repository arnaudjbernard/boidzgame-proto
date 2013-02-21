package com.boidzgame.gameplay.rendering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Canvas;

public class RendererManager {
	private class RendererComparator implements Comparator<IRenderer> {
		public int compare(IRenderer lhs, IRenderer rhs) {
			return rhs.getLayer() - lhs.getLayer();
		}
	}

	private final RendererComparator mRendererComparator = new RendererComparator();
	private boolean mSorted = false;

	private double mScaleX = 1.0f;
	private double mScaleY = 1.0f;

	// Renderer
	private List<IRenderer> mRenderers = new ArrayList<IRenderer>();

	public void register(IRenderer renderer) {
		mRenderers.add(renderer);
		mSorted = false;
	}

	public void unregister(IRenderer renderer) {
		mRenderers.remove(renderer);
	}

	public void draw(int delay, Canvas canvas) {
		if (!mSorted) {
			Collections.sort(mRenderers, mRendererComparator);
			mSorted = true;
		}
		for (int i = mRenderers.size() - 1; i >= 0; i--) {
			mRenderers.get(i).draw(delay, canvas, mScaleX, mScaleY);
		}
	}

	public void setup() {
	}

	public void clean() {
	}

	public void setScaleX(double scaleX) {
		mScaleX = scaleX;
	}

	public void setScaleY(double scaleY) {
		mScaleY = scaleY;
	}
}
