package com.boidzgame.util;

public class MathUtil {
	final public static double dist(int xa, int ya, int xb, int yb) {
		return Math.sqrt((double) ((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb)));
	}

	final public static double dist(double xa, double ya, double xb, double yb) {
		return Math.sqrt((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb));
	}

	final public static double signum(double d) {
		if (d >= 0.0d) {
			return 1.0d;
		} else {
			return -1.0d;
		}
	}
}
