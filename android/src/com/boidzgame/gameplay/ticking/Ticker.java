package com.boidzgame.gameplay.ticking;

public abstract class Ticker implements ITicker {
	private int mPriority;

	public int getPriority() {
		return mPriority;
	}

	public void setPriority(int priority) {
		mPriority = priority;
	}

	/**
	 * @param delay time elapsed since last computation
	 */
	public abstract void tick(double delay);
}
