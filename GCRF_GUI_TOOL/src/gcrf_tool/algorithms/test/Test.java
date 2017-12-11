package gcrf_tool.algorithms.test;

import gcrf_tool.algorithms.asymmetric.*;
import gcrf_tool.algorithms.symmetric.*;
import gcrf_tool.generators.ArrayGenerator;
import gcrf_tool.generators.GraphGenerator;

public class Test {

	public void runAsymmetric() {
		double[][] s = GraphGenerator.generateDirectedGraph(200);
		double[] r = ArrayGenerator.generateArray(200,5);

		double alpha = 1;
		double beta = 1;
		double lr = 0.0001;
		int maxIter = 10000;

		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		double[] y = c.y(1, 2, 0.05);

		GradientDescentAsymmetric g = new GradientDescentAsymmetric(alpha,
				beta, lr, s, r, y);
		double[] params = g.learn(maxIter, true,null);

		double alphaT = params[0];
		double betaT = params[1];

		AlgorithmAsymmetric g1 = new AlgorithmAsymmetric(alphaT, betaT, s, r, y);
		System.out.println(alphaT);
		System.out.println(betaT);
		System.out.println("R^2: " + g1.rSquared());
	}

	public void testAsymmetric() {
		// double alphaT = 11.182877246565837;
		// double betaT = 3.319428679648353;
		double alphaT = 5.738379778398131;
		double betaT = 11.471458640825238;
		double[][] sT = GraphGenerator.generateDirectedGraph(200);
		double[] rT = ArrayGenerator.generateArray(200,5);

		CalculationsAsymmetric c1 = new CalculationsAsymmetric(sT, rT);
		double[] yTest = c1.y(1, 2, 0.05);

		AlgorithmAsymmetric g1 = new AlgorithmAsymmetric(alphaT, betaT, sT, rT, yTest);
		System.out.println("R^2: " + g1.rSquared());
	}

	public void runSymmetric() {
		double[][] sa = GraphGenerator.generateDirectedGraph(200);
		double[][] s = GraphGenerator.converteGraphToUndirected(sa);
		double[] r = ArrayGenerator.generateArray(200,5);

		double alpha = 1;
		double beta = 1;
		double lr = 0.0001;
		int maxIter = 10000;

		CalculationsSymmetric c = new CalculationsSymmetric(s, r);
		double[] y = c.y(5, 1, 0.05);

		GradientDescentSymmetric g = new GradientDescentSymmetric(alpha, beta,
				lr, s, r, y);
		double[] params = g.learn(maxIter, true,null);

		double alphaT = params[0];
		double betaT = params[1];

		AlgorithmSymmetric g1 = new AlgorithmSymmetric(alphaT, betaT, s, r, y);
		System.out.println(alphaT);
		System.out.println(betaT);
		System.out.println("R^2: " + g1.rSquared());
	}

	public void testSymmetric() {
		double alphaT = 12.315045670766596;
		double betaT = 2.0593994019727586;
		double[][] s = GraphGenerator.generateDirectedGraph(200);
		double[][] sT = GraphGenerator.converteGraphToUndirected(s);
		double[] rT = ArrayGenerator.generateArray(200,5);

		CalculationsSymmetric c1 = new CalculationsSymmetric(sT, rT);
		double[] yTest = c1.y(5, 1, 0.05);

		AlgorithmSymmetric g1 = new AlgorithmSymmetric(alphaT, betaT, sT, rT, yTest);
		System.out.println("R^2: " + g1.rSquared());
	}
}
