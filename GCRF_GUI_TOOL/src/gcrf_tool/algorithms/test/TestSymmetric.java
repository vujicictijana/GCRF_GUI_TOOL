package gcrf_tool.algorithms.test;

import java.text.DecimalFormat;

import gcrf_tool.algorithms.symmetric.*;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.generators.ArrayGenerator;
import gcrf_tool.generators.GraphGenerator;

public class TestSymmetric {

	public void testTrainAndTestMore(int no, int maxIter, int times,
			String folder, boolean same) {
		DecimalFormat df = new DecimalFormat("#.######");
		double[][] s = null;
		double[][] sT = null;
		double[] r = null;
		double[] rT = null;

		double alpha = 1;
		double beta = 1;
		double lr = 0.0001;

		double r2 = 0;

		String[] text = new String[times * 2];
		String[] resultsAsymmetric = Reader.read(folder + "/results" + folder
				+ ".txt");
		String textA = "";
		int t = 0;
		for (int i = 1; i <= times; i++) {
			s = Reader.readGraph(folder + "/graph" + i + ".txt", no);
			r = Reader.readArray(folder + "/r" + i + ".txt", no);
			if (same) {
				r2 = runSymmetric(alpha, beta, lr, s, r, maxIter);
			} else {
				sT = Reader.readGraph(folder + "/graphTest" + i + ".txt", no);
				rT = Reader.readArray(folder + "/rTest" + i + ".txt", no);
				r2 = runSymmetricDif(alpha, beta, lr, s, r, maxIter, sT, rT);
			}
			textA = i + ": R^2 Symmetric = " + df.format(r2);
			System.out.println(textA);
			text[t] = resultsAsymmetric[i - 1];
			t++;
			text[t] = textA;
			t++;
		}
		Writer.write(text, folder + "/resultComparison" + folder + ".txt");
	}

	private double runSymmetricDif(double alpha, double beta, double lr,
			double[][] s, double[] r, int maxIter, double[][] sT, double[] rT) {

		double[][] sS = GraphGenerator.converteGraphToUndirected(s);
		CalculationsSymmetric cS = new CalculationsSymmetric(sS, r);
		double[] yS = cS.y(5, 1, 0.05);
		GradientDescentSymmetric gS = new GradientDescentSymmetric(alpha, beta,
				lr, sS, r, yS);
		double[] paramsS = gS.learn(maxIter, false, null);
		double alphaT = paramsS[0];
		double betaT = paramsS[1];

		double[][] sST = GraphGenerator.converteGraphToUndirected(sT);
		CalculationsSymmetric cS1 = new CalculationsSymmetric(sST, rT);
		double[] yTest = cS1.y(5, 1, 0.05);

		AlgorithmSymmetric g1 = new AlgorithmSymmetric(alphaT, betaT, sST, rT,
				yTest);
		return g1.rSquared();
	}

	private double runSymmetric(double alpha, double beta, double lr,
			double[][] s, double[] r, int maxIter) {
		double[][] sS = GraphGenerator.converteGraphToUndirected(s);

		CalculationsSymmetric cS = new CalculationsSymmetric(sS, r);
		double[] yS = cS.y(5, 1, 0.05);
		GradientDescentSymmetric gS = new GradientDescentSymmetric(alpha, beta,
				lr, sS, r, yS);
		double[] paramsS = gS.learn(maxIter, false, null);
		double alphaTS = paramsS[0];
		double betaTS = paramsS[1];
		// System.out.println(alphaTS + " " + betaTS);
		AlgorithmSymmetric g1S = new AlgorithmSymmetric(alphaTS, betaTS, sS, r,
				yS);
		return g1S.rSquared();
	}

	public void testSymmetric(int no, int maxIter) {
		double[][] s = GraphGenerator.generateDirectedGraph(no);
		double[] r = ArrayGenerator.generateArray(200, 5);
		double alpha = 1;
		double beta = 1;
		double lr = 0.0001;
		System.out.println();

		double r2 = runSymmetric(alpha, beta, lr, s, r, maxIter);

		System.out.println(r2);

	}
}
