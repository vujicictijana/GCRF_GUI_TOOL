package gcrf_tool.algorithms.test;

import gcrf_tool.algorithms.symmetric.AlgorithmSymmetric;
import gcrf_tool.algorithms.symmetric.CalculationsSymmetric;
import gcrf_tool.algorithms.symmetric.GradientDescentSymmetric;
import gcrf_tool.data.generators.ArrayGenerator;
import gcrf_tool.data.generators.GraphGenerator;

public class TestTime {

	public static void main(String[] args) {
		int nodes = 1000;
		int iterations = 50;
		
		double[][] s = GraphGenerator.generateDirectedGraphWithEdgeProbability(
				nodes, 0.5);
		double[][] sS = GraphGenerator.converteGraphToUndirected(s);
		double[] rS = ArrayGenerator.generateArray(nodes, 5);
		CalculationsSymmetric cS = new CalculationsSymmetric(sS, rS);
		double[] yS = cS.y(5, 1, 0.05);
		long start = System.currentTimeMillis();
		GradientDescentSymmetric gdS = new GradientDescentSymmetric(1, 1, 0.01,
				sS, rS, yS);
		// double[] resS = gdS.learn(iterations, false, null);
		gdS.learn(iterations, false, null);
		// AlgorithmSymmetric algS = new AlgorithmSymmetric(resS[0], resS[1],
		// sS,
		// rS, yS);
		// double r2S = algS.rSquared();
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println(elapsedTime);
	}
}
