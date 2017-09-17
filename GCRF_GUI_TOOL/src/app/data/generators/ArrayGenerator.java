package app.data.generators;

import java.util.Random;

public class ArrayGenerator {

	public static double[] generateArray(int noOfNodes,int maximum) {
		double[] r = new double[noOfNodes];
		Random rand = new Random();
		for (int i = 0; i < r.length; i++) {
			r[i] = rand.nextInt(maximum) + 1 + Math.random();
		}
		return r;
	}
}
