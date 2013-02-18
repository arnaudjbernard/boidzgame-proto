package com.boidzgame.gameplay.ticking;

public interface ITicker {
	public int getPriority();

	public void setPriority(int priority);

	public void tick(double delay);
}
