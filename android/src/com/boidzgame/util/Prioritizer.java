package com.boidzgame.util;

public class Prioritizer<Template> {
	public int priority;
	public Template content;

	public Prioritizer(Template content, int priority) {
		this.content = content;
		this.priority = priority;
	}
}