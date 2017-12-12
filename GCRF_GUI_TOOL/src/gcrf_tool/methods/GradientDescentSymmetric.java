package gcrf_tool.methods;

import javax.swing.JProgressBar;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.calculations.Calculations;
import gcrf_tool.calculations.CalculationsGCRF;

public class GradientDescentSymmetric {
	private double alpha;
	private double beta;
	// learning rate
	private double lr;
	Calculations calcs;
	private double[] r;
	private double[] y;

	public GradientDescentSymmetric(double alpha, double beta, double lr,
			double[][] s, double[] r, double[] y) {
		super();
		this.alpha = alpha;
		this.beta = beta;
		this.lr = lr;
		this.r = r;
		this.y = y;
		calcs = new CalculationsGCRF(s, r);
	}

	public double[] learn(int maxIter, boolean showProgress,
			JProgressBar progress) {
		int tempIter = 0;
		double little = 1E-10;
		double tempAlpha = 0;
		double tempBeta = 0;
		double difAlpha = 1;
		double difBeta = 1;
		// DecimalFormat df = new DecimalFormat("#.##########");
		// System.out.println(df.format(little));

		while (difAlpha > little || difBeta > little) {
			// while (tempIter < iterNum) {
			if (tempIter % 50 == 0) {
				if (progress != null) {
					progress.setValue(tempIter);
				}
				if (showProgress) {
					System.out.println("Iter: " + tempIter);
				}
			}
			if (tempIter == maxIter) {
				break;
			}
			tempAlpha = alpha + lr * dervativeAlpha(alpha, beta);
			tempBeta = beta + lr * dervativeBeta(alpha, beta);

			// difAlpha = Math.abs(alpha) - Math.abs(tempAlpha);
			// difBeta = Math.abs(beta) - Math.abs(tempBeta);

			difAlpha = Math.abs(alpha - tempAlpha);
			difBeta = Math.abs(beta - tempBeta);
			// System.out.println(df.format(difAlpha) + " " +
			// df.format(difBeta));
			alpha = tempAlpha;
			beta = tempBeta;

			tempIter++;
		}
		if (showProgress) {
			System.out.println("Iteration: " + tempIter);
			System.out.println("Alpha: " + alpha);
			System.out.println("Beta: " + beta);
		}
		double[] params = new double[2];
		params[0] = alpha;
		params[1] = beta;
		return params;
	}

	public double dervativeAlpha(double alpha, double beta) {
		// partial derivative with respect to the beta
		// -1/2 * [ y^Ty + 2R^T(mu-y)-mu^Tmu] +1/2 * Tr(Q^-1)
		double[] mu = calcs.mu(alpha, beta);
		double[][] q = calcs.q(alpha, beta);

		// yTy = y^Ty
		double yTy = BasicCalcs.multiplyTwoVectors(y, y);

		// muMinusY = (mu-y)
		double[] muMinusY = BasicCalcs.vectorMinusVector(mu, y);

		// res = 2R^T(mu-y)
		double res = BasicCalcs.multiplyTwoVectors(
				BasicCalcs.multiplyVectorByANumber(r, 2), muMinusY);
		// muTmu = mu^Tmu
		double muTmu = BasicCalcs.multiplyTwoVectors(mu, mu);

		// result1 = -1/2 * [ y^Ty + 2R^T(mu-y)-mu^Tmu]
		// result1 = - [yTy+res-muTmu]/2
		double result1 = -(yTy + res - muTmu) / 2;

		double trace = BasicCalcs.trace(BasicCalcs.inverse(q));
		// result2 = 1/2 * Tr(Q^-1)
		// result2 = trace/2
		double result2 = trace / 2;

		return result1 + result2;
	}

	public double dervativeBeta(double alpha, double beta) {
		// partial derivative respect to the beta
		// -1/2 * (y^T*L*y - mu^T*L*mu) + 1/2 * Tr(L*Q^-1)
		double[] mu = calcs.mu(alpha, beta);
		double[][] l = calcs.l();
		double[][] q = calcs.q(alpha, beta);

		// yTLy = y^T*L*y
		double yTLy = BasicCalcs.multiplyTwoVectors(
				BasicCalcs.multiplyMatrixByAColumnVector(l, y), y);

		// muTLmu = mu^T*L*mu
		double muTLmu = BasicCalcs.multiplyTwoVectors(
				BasicCalcs.multiplyMatrixByAColumnVector(l, mu), mu);

		// result1 = -1/2 * (y^T*L*y - mu^T*L*mu)
		// result1 = - [yTLy - muTLmu]/2
		double result1 = -(yTLy - muTLmu) / 2;

		// trace = Tr(L*Q^-1)
		double trace = BasicCalcs.trace(BasicCalcs.multiplyTwoMatrices(l,
				BasicCalcs.inverse(q)));

		// result2 = 1/2 * Tr(L*Q^-1)
		// result2 = trace/2
		double result2 = trace / 2;

		return result1 + result2;
	}

}