package gcrf_tool.algorithms.test;

import java.text.DecimalFormat;

import gcrf_tool.algorithms.asymmetric.*;
import gcrf_tool.file.Writer;
import gcrf_tool.generators.*;

public class TestAcyclic {
	public double[] testTrain(int no, int maxIter) {
		double[][] s = GraphGenerator.generateDirectedAcyclicGraph(5);
		double[] r = ArrayGenerator.generateArray(200, 5);
		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		double[] y = c.y(5, 1, 0.05);
		double alpha = 0.1;
		double beta = 0.1;
		double lr = 0.0001;

		GradientDescentAsymmetric g = new GradientDescentAsymmetric(alpha,
				beta, lr, s, r, y);
		return g.learn(maxIter, true, null);
	}

	public void testGCRF(int no, int maxIter) {
		double[] params = testTrain(no, maxIter);
		double alpha = params[0];
		double beta = params[1];
		// double alpha = 3.5;
		// double beta = 1;
		//
		System.out.println();

		double[][] sTest = GraphGenerator.generateDirectedAcyclicGraph(5);
		double[] rTest = ArrayGenerator.generateArray(200, 5);
		CalculationsAsymmetric c = new CalculationsAsymmetric(sTest, rTest);
		double[] yTest = c.y(5, 1, 0.05);

		AlgorithmAsymmetric g = new AlgorithmAsymmetric(alpha, beta, sTest,
				rTest, yTest);
		System.out.println("R^2:" + g.rSquared());
	}

	public void testTrainAndTestMore(int no, int maxIter, int times,
			String fileName, boolean same) {
		DecimalFormat df = new DecimalFormat("#.######");
		double[][] s = null;
		double[][] sT = null;
		double[] r = null;

		double alpha = 1;
		double beta = 1;
		double lr = 0.0001;

		double r2A = 0;
		String path = "acyclicSame";
		if (!same) {
			path = "acyclicNotSame";
		}
		Writer.createFolder(path);

		String[] text = new String[times];
		String textA = "";
		for (int i = 1; i <= times; i++) {
			s = GraphGenerator.generateDirectedAcyclicGraph(5);
			r = ArrayGenerator.generateArray(200, 5);
			Writer.writeGraph(s, path + "/graph" + i + ".txt");
			if (same) {
				r2A = runAsymmetric(alpha, beta, lr, s, r, maxIter);
			} else {
				sT = GraphGenerator.generateDirectedAcyclicGraph(5);
				Writer.writeGraph(sT, path + "/graphTest" + i + ".txt");
				r2A = runAsymmetricDif(alpha, beta, lr, s, r, maxIter, sT);
			}
			textA = i + ": R^2 Asymmetric Acyclic = " + df.format(r2A);
			System.out.println(textA);
			text[i - 1] = textA;
		}
		Writer.write(text, path + "/" + fileName);
	}

	private double runAsymmetric(double alpha, double beta, double lr,
			double[][] s, double[] r, int maxIter) {
		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		double[] y = c.y(5, 1, 0.05);

		GradientDescentAsymmetric g = new GradientDescentAsymmetric(alpha,
				beta, lr, s, r, y);
		double[] params = g.learn(maxIter, false, null);

		double alphaT = params[0];
		double betaT = params[1];

		AlgorithmAsymmetric g1 = new AlgorithmAsymmetric(alphaT, betaT, s, r, y);
		return g1.rSquared();
	}

	private double runAsymmetricDif(double alpha, double beta, double lr,
			double[][] s, double[] r, int maxIter, double[][] sT) {
		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		double[] y = c.y(5, 1, 0.05);

		GradientDescentAsymmetric g = new GradientDescentAsymmetric(alpha,
				beta, lr, s, r, y);
		double[] params = g.learn(maxIter, false, null);

		double alphaT = params[0];
		double betaT = params[1];

		System.out.println();
		double[] rT = ArrayGenerator.generateArray(200, 5);

		CalculationsAsymmetric c1 = new CalculationsAsymmetric(sT, rT);
		double[] yT = c1.y(5, 1, 0.05);
		AlgorithmAsymmetric g1 = new AlgorithmAsymmetric(alphaT, betaT, sT, rT,
				yT);
		return g1.rSquared();
	}
}
