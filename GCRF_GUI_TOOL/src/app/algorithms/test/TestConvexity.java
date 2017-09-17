package app.algorithms.test;

import java.text.DecimalFormat;

import app.algorithms.asymmetric.AlgorithmAsymmetric;
import app.algorithms.asymmetric.CalculationsAsymmetric;
import app.algorithms.asymmetric.GradientDescentAsymmetric;

public class TestConvexity {

	public void test(int no, int maxIter, double lr, double[][] s, double[] r,
			double[] y, double[][] s1, double[] r1, double[] y1) {
		double alpha = 0;
		double beta = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		for (int i = 0; i < no; i++) {
			alpha = Math.random() * 5;
			alpha = Double.parseDouble(df.format(alpha));
			beta = Math.random() * 5;
			beta = Double.parseDouble(df.format(beta));
			GradientDescentAsymmetric gda = new GradientDescentAsymmetric(
					alpha, beta, lr, s, r, y);
			double[] res = gda.learn(maxIter, false, null);

			AlgorithmAsymmetric alg = new AlgorithmAsymmetric(res[0], res[1],
					s, r, y);
			double r2 = alg.rSquared();

			AlgorithmAsymmetric alg1 = new AlgorithmAsymmetric(res[0], res[1],
					s1, r1, y1);
			double r2Test = alg1.rSquared();
			System.out.println(alpha + "," + beta + "," + r2 + "," + r2Test);
		}

	}

	public void testNew(int maxIter, double lr, double[][] s, double[] r,
			double[] y, double[][] s1, double[] r1, double[] y1) {
		double alpha = 0;
		double beta = 0;
		double theta = 0.01;
		double theta1 = 0;
		double alpha1 = 0;
		double beta1 = 0;
		double r2Test = 0;
		double r2 = 0;
		double[] res = null;

		DecimalFormat df = new DecimalFormat("#.##");
		int i = 1;
		while (theta < 1.57) {
			alpha = Double.parseDouble(df.format(Math.sin(theta)));
			beta = Double.parseDouble(df.format(Math.cos(theta)));
			GradientDescentAsymmetric gda = new GradientDescentAsymmetric(
					alpha, beta, lr, s, r, y);
			res = gda.learn(maxIter, false, null);

			alpha1 = res[0];
			beta1 = res[1];

			AlgorithmAsymmetric alg = new AlgorithmAsymmetric(res[0], res[1],
					s, r, y);
			r2 = alg.rSquared();

			AlgorithmAsymmetric alg1 = new AlgorithmAsymmetric(alpha1, beta1,
					s1, r1, y1);
			r2Test = alg1.rSquared();
			theta1 = Math.atan(alpha1 / beta1);
			System.out.println(i + "," + theta + "," + alpha + "," + beta + ","
					+ alpha1 + "," + beta1 + "," + theta1 + "," + r2 + ","
					+ r2Test);
			// System.out.println(theta1);
			i++;
			theta += 0.1;
		}
	}

	public void testNewLog(double[][] s, double[] r,
			double[] y) {
		double alpha = 0;
		double beta = 0;
		double theta = 0.01;

		DecimalFormat df = new DecimalFormat("#.##");
		int i = 1;
		while (theta < 1.57) {
			alpha = Double.parseDouble(df.format(Math.sin(theta)));
			beta = Double.parseDouble(df.format(Math.cos(theta)));
			CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
//			System.out.println(i + "," + theta + ","
//					+ c.logLikelihood(alpha, beta, y));
			System.out.println(c.logLikelihood(alpha, beta, y));
			i++;
			theta += 0.1;
		}
	}
}
