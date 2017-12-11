package gcrf_tool.algorithms.test;

import java.text.DecimalFormat;

import gcrf_tool.algorithms.asymmetric.*;
import gcrf_tool.file.Writer;
import gcrf_tool.generators.ArrayGenerator;
import gcrf_tool.generators.GraphGenerator;

public class TestAsymmetric {

	public void testDataGenerator() {
		double[][] s = GraphGenerator.generateDirectedGraph(3);
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.length; j++) {
				System.out.print(s[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		double[] r = ArrayGenerator.generateArray(200, 5);
		for (int i = 0; i < r.length; i++) {
			System.out.println(r[i]);
		}

		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		System.out.println();

		double[] y = c.y(5, 1, 0.05);

		System.out.println();
		for (int i = 0; i < y.length; i++) {
			System.out.println(y[i]);
		}
	}

	public void showMatrix(double[][] s) {
		DecimalFormat df = new DecimalFormat("#.##");
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.length; j++) {
				System.out.print(df.format(s[i][j]) + "\t");
			}
			System.out.println();
		}
	}

	public double[] testTrain(int no, int maxIter, boolean show) {
		double[][] s = GraphGenerator.generateDirectedGraph(no);
		double[] r = ArrayGenerator.generateArray(200, 5);
		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		double[] y = c.y(5, 1, 0.05);
		double alpha = 0.1;
		double beta = 0.1;
		double lr = 0.0001;

		GradientDescentAsymmetric g = new GradientDescentAsymmetric(alpha,
				beta, lr, s, r, y);
		return g.learn(maxIter, show, null);
	}

	public void testNewGCRF(int no, int maxIter) {
		double[] params = testTrain(no, maxIter, true);
		double alpha = params[0];
		double beta = params[1];
		// double alpha = 3.5;
		// double beta = 1;
		//
		System.out.println();

		double[][] sTest = GraphGenerator.generateDirectedGraph(500);
		double[] rTest = ArrayGenerator.generateArray(200, 5);
		CalculationsAsymmetric c = new CalculationsAsymmetric(sTest, rTest);
		double[] yTest = c.y(5, 1, 0.05);

		AlgorithmAsymmetric g = new AlgorithmAsymmetric(alpha, beta, sTest,
				rTest, yTest);
		System.err.println("R^2:" + g.rSquared());
	}

	public void testConvertToSymmeric() {
		double[][] matrix = { { 1, 0.5, 0.3 }, { 0.2, 1, 0.7 }, { 0.4, 0.5, 1 } };
		System.out.println("Asymmetric:");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("\nSymmetric:");
		double[][] matrixS = GraphGenerator.converteGraphToUndirected(matrix);
		for (int i = 0; i < matrixS.length; i++) {
			for (int j = 0; j < matrixS.length; j++) {
				System.out.print(matrixS[i][j] + "\t");
			}
			System.out.println();
		}
	}

	// public void testTrainAndTestWithSameGraphBoth(int no, int maxIter,
	// int times, String fileName) {
	// DecimalFormat df = new DecimalFormat("#.######");
	// double[][] s = null;
	// double[] r = null;
	//
	// double alpha = 1;
	// double beta = 1;
	// double lr = 0.0001;
	//
	// double r2A = 0;
	// double r2S = 0;
	//
	// String[] text = new String[times * 2];
	// int textLine = 0;
	//
	// String textS = "";
	// String textA = "";
	// for (int i = 1; i <= times; i++) {
	// s = DataGenerator.generateS(no);
	// r = DataGenerator.generateR(no);
	// r2A = runAsymmetric(alpha, beta, lr, s, r, maxIter);
	// r2S = runSymmetric(alpha, beta, lr, s, r, maxIter);
	// textA = i + ": R^2 Asymmetric = " + df.format(r2A);
	// textS = i + ": R^2 Symmetric = " + df.format(r2S);
	// System.out.println(textA);
	// System.out.println(textS);
	// text[textLine++] = textA;
	// text[textLine++] = textS;
	// }
	// Writer.write(fileName, text);
	// }

	public void testTrainAndTestMore(int no, int maxIter, int times,
			boolean same) {
		DecimalFormat df = new DecimalFormat("#.######");
		double[][] s = null;
		double[][] sT = null;
		double[] r = null;
		double[] rT = null;

		double alpha = 1;
		double beta = 1;
		double lr = 0.0001;

		double r2A = 0;

		String path = "AsymmetricSame";
		if (!same) {
			path = "AsymmetricNotSame";
		}
		Writer.createFolder(path);

		String[] text = new String[times];
		String textA = "";
		for (int i = 1; i <= times; i++) {
			s = GraphGenerator.generateDirectedGraph(no);
			Writer.writeGraph(s, path + "/graph" + i + ".txt");
			r = ArrayGenerator.generateArray(200, 5);
			Writer.writeDoubleArray(r, path + "/r" + i + ".txt");
			if (same) {
				r2A = runAsymmetric(alpha, beta, lr, s, r, maxIter);
			} else {
				sT = GraphGenerator.generateDirectedGraph(no);
				Writer.writeGraph(sT, path + "/graphTest" + i + ".txt");
				rT = ArrayGenerator.generateArray(200, 5);
				Writer.writeDoubleArray(rT, path + "/rTest" + i + ".txt");
				r2A = runAsymmetricDif(alpha, beta, lr, s, r, maxIter, sT, rT);
			}
			textA = i + ": R^2 Asymmetric = " + df.format(r2A);
			System.out.println(textA);
			text[i - 1] = textA;
		}
		Writer.write(text, path + "/results" + path + ".txt");
	}

	public void testTrainAndTestMoreWithProbability(int no, int maxIter,
			int times, boolean same, double probability) {
		DecimalFormat df = new DecimalFormat("#.######");
		double[][] s = null;
		double[][] sT = null;
		double[] r = null;
		double[] rT = null;

		double alpha = 1;
		double beta = 1;
		double lr = 0.0001;

		double r2A = 0;

		String path = "AsymmetricSameWithProb" + probability;
		if (!same) {
			path = "AsymmetricNotSameWithProb" + probability;
		}
		Writer.createFolder(path);

		String[] text = new String[times];
		String textA = "";
		for (int i = 1; i <= times; i++) {
			s = GraphGenerator.generateDirectedGraphWithEdgeProbability(no,
					probability);
			Writer.writeGraph(s, path + "/graph" + i + ".txt");
			r = ArrayGenerator.generateArray(200, 5);
			Writer.writeDoubleArray(r, path + "/r" + i + ".txt");
			if (same) {
				r2A = runAsymmetric(alpha, beta, lr, s, r, maxIter);
			} else {
				sT = GraphGenerator.generateDirectedGraphWithEdgeProbability(
						no, probability);
				Writer.writeGraph(sT, path + "/graphTest" + i + ".txt");
				rT = ArrayGenerator.generateArray(200, 5);
				Writer.writeDoubleArray(rT, path + "/rTest" + i + ".txt");
				r2A = runAsymmetricDif(alpha, beta, lr, s, r, maxIter, sT, rT);
			}
			textA = i + ": R^2 Asymmetric = " + df.format(r2A);
			System.out.println(textA);
			text[i - 1] = textA;
		}
		Writer.write(text, path + "/results" + path + ".txt");
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
			double[][] s, double[] r, int maxIter, double[][] sT, double[] rT) {
		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		double[] y = c.y(5, 1, 0.05);

		GradientDescentAsymmetric g = new GradientDescentAsymmetric(alpha,
				beta, lr, s, r, y);
		double[] params = g.learn(maxIter, false, null);

		double alphaT = params[0];
		double betaT = params[1];

		CalculationsAsymmetric c1 = new CalculationsAsymmetric(sT, rT);
		double[] yTest = c1.y(5, 1, 0.05);

		AlgorithmAsymmetric g1 = new AlgorithmAsymmetric(alphaT, betaT, sT, rT,
				yTest);
		return g1.rSquared();
	}

}
