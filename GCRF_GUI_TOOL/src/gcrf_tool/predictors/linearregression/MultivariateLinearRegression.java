package gcrf_tool.predictors.linearregression;

import java.io.Serializable;

import gcrf_tool.algorithms.basic.BasicCalcs;
import gcrf_tool.file.Writer;
import gcrf_tool.predictors.helper.Helper;
import Jama.Matrix;
import Jama.QRDecomposition;

public class MultivariateLinearRegression implements Serializable {

	private static final long serialVersionUID = 2267604643584597866L;
	private final int N; // number of
	private final int p; // number of dependent variables
	private final Matrix beta; // regression coefficients
	private double SSE; // sum of squared
	private double SST; // sum of squared

	public MultivariateLinearRegression(double[][] x, double[] y) {
		if (x.length != y.length)
			throw new RuntimeException("dimensions don't agree");
		N = y.length;
		p = x[0].length;

		Matrix X = new Matrix(x);

		// create matrix from vector
		Matrix Y = new Matrix(y, N);

		// find least squares solution
		QRDecomposition qr = new QRDecomposition(X);
		beta = qr.solve(Y);

		// mean of y[] values
		double sum = 0.0;
		for (int i = 0; i < N; i++)
			sum += y[i];
		double mean = sum / N;

		// total variation to be accounted for
		for (int i = 0; i < N; i++) {
			double dev = y[i] - mean;
			SST += dev * dev;
		}

		// variation not accounted for
		Matrix residuals = X.times(beta).minus(Y);
		SSE = residuals.norm2() * residuals.norm2();

	}

	public double beta(int j) {
		return beta.get(j, 0);
	}

	public double R2() {
		return 1.0 - SSE / SST;
	}

	public double test(double[] y1, double[][] x1, String folder, boolean test) {

		double[] outputs = new double[y1.length];
		for (int i = 0; i < y1.length; i++) {
			outputs[i] = 0;
			for (int j = 0; j < x1[i].length; j++) {
				// System.out.println(j + " " + beta(j));
				outputs[i] += beta(j) * x1[i][j];
			}
		}
		// for (int j = 0; j < x1[0].length; j++) {
		// System.out.println(j + " " + beta(j));
		// }
		if (folder != null) {
			if (test) {
				Writer.writeDoubleArray(outputs, folder + "/data/rTest.txt");
			} else {
				Writer.createFolder(folder + "/mlr");
				Helper.serilazie(this, folder + "/mlr/lr.txt");
				Writer.writeDoubleArray(outputs, folder + "/data/r.txt");
			}
		}

		return BasicCalcs.rSquared(outputs,y1);
	}

}
