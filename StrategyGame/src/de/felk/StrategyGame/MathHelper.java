package de.felk.StrategyGame;

public class MathHelper {

	public static int floor(double x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	public static long getRandomSeed() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

}
