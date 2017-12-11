package gcrf_tool.tool.predictors.linearregression;

import java.io.Serializable;

import gcrf_tool.algorithms.basic.BasicCalcs;
import gcrf_tool.file.Writer;
import gcrf_tool.predictors.helper.Helper;

public class LinearRegression implements Serializable {

	private static final long serialVersionUID = 281809303636659399L;
	private final int N;
	private final double intercept, slope;
	private final double R2;
	private final double svar, svar0, svar1;

	/**
	 * Performs a linear regression on the data points <tt>(y[i], x[i])</tt>.
	 *
	 * @param x
	 *            the values of the predictor variable
	 * @param y
	 *            the corresponding values of the response variable
	 * @throws IllegalArgumentException
	 *             if the lengths of the two arrays are not equal
	 */
	public LinearRegression(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("array lengths are not equal");
		}
		N = x.length;

		// first pass
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
		for (int i = 0; i < N; i++) {
			sumx += x[i];
			sumx2 += x[i] * x[i];
			sumy += y[i];
		}
		double xbar = sumx / N;
		double ybar = sumy / N;

		// second pass: compute summary statistics
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = 0; i < N; i++) {
			xxbar += (x[i] - xbar) * (x[i] - xbar);
			yybar += (y[i] - ybar) * (y[i] - ybar);
			xybar += (x[i] - xbar) * (y[i] - ybar);
		}
		slope = xybar / xxbar;
		intercept = ybar - slope * xbar;

		// more statistical analysis
		double rss = 0.0; // residual sum of squares
		double ssr = 0.0; // regression sum of squares
		for (int i = 0; i < N; i++) {
			double fit = slope * x[i] + intercept;
			rss += (fit - y[i]) * (fit - y[i]);
			ssr += (fit - ybar) * (fit - ybar);
		}

		int degreesOfFreedom = N - 2;
		R2 = ssr / yybar;
		svar = rss / degreesOfFreedom;
		svar1 = svar / xxbar;
		svar0 = svar / N + xbar * xbar * svar1;
	}

	/**
	 * Returns the <em>y</em>-intercept &alpha; of the best of the best-fit line
	 * <em>y</em> = &alpha; + &beta; <em>x</em>.
	 *
	 * @return the <em>y</em>-intercept &alpha; of the best-fit line
	 *         <em>y = &alpha; + &beta; x</em>
	 */
	public double intercept() {
		return intercept;
	}

	/**
	 * Returns the slope &beta; of the best of the best-fit line <em>y</em> =
	 * &alpha; + &beta; <em>x</em>.
	 *
	 * @return the slope &beta; of the best-fit line <em>y</em> = &alpha; +
	 *         &beta; <em>x</em>
	 */
	public double slope() {
		return slope;
	}

	/**
	 * Returns the coefficient of determination <em>R</em><sup>2</sup>.
	 *
	 * @return the coefficient of determination <em>R</em><sup>2</sup>, which is
	 *         a real number between 0 and 1
	 */
	public double R2() {
		return R2;
	}

	/**
	 * Returns the standard error of the estimate for the intercept.
	 *
	 * @return the standard error of the estimate for the intercept
	 */
	public double interceptStdErr() {
		return Math.sqrt(svar0);
	}

	/**
	 * Returns the standard error of the estimate for the slope.
	 *
	 * @return the standard error of the estimate for the slope
	 */
	public double slopeStdErr() {
		return Math.sqrt(svar1);
	}

	/**
	 * Returns the expected response <tt>y</tt> given the value of the predictor
	 * variable <tt>x</tt>.
	 *
	 * @param x
	 *            the value of the predictor variable
	 * @return the expected response <tt>y</tt> given the value of the predictor
	 *         variable <tt>x</tt>
	 */
	public double predict(double x) {
		return slope * x + intercept;
	}

	/**
	 * Returns a string representation of the simple linear regression model.
	 *
	 * @return a string representation of the simple linear regression model,
	 *         including the best-fit line and the coefficient of determination
	 *         <em>R</em><sup>2</sup>
	 */
	public String toString() {
		String s = "";
		s += String.format("%.2f N + %.2f", slope(), intercept());
		return s + "  (R^2 = " + String.format("%.3f", R2()) + ")";
	}

	public static double test(double[] y1, double[] x1, String folder,
			LinearRegression lr, boolean test) {

		double[] outputs = new double[y1.length];
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = lr.predict(x1[i]);
		}
		if (folder != null) {
			if (test) {
				Writer.writeDoubleArray(outputs, folder + "/data/rTest.txt");
			} else {
				Writer.createFolder(folder + "/lr");
				Helper.serilazie(lr, folder + "/lr/lr.txt");
				Writer.writeDoubleArray(outputs, folder + "/data/r.txt");
			}
		}
		return BasicCalcs.rSquared(outputs,y1);
	}

}
