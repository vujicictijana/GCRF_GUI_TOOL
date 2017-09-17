package app.algorithms.asymmetric;

import javax.swing.JProgressBar;

import app.algorithms.basic.BasicCalcs;

public class GradientDescentAsymmetric {
	private double alpha;
	private double beta;
	private double lr;
	CalculationsAsymmetric calcs;
	private double[] r;
	private double[] y;

	public GradientDescentAsymmetric(double alpha, double beta, double lr,
			double[][] s, double[] r, double[] y) {
		super();
		this.alpha = alpha;
		this.beta = beta;
		this.lr = lr;
		this.r = r;
		this.y = y;
		this.calcs = new CalculationsAsymmetric(s, r);
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

		double[] mu = null;
		double[][] q = null;
		double[][] qInverse = null;

		while (difAlpha > little || difBeta > little) {
			// System.out.print(Math.atan(alpha / beta) + ",");
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
			// System.out.println("Iter: " + tempIter);

			mu = calcs.mu(alpha, beta);
			q = calcs.q(alpha, beta);
			qInverse = BasicCalcs.inverse(q);
			tempAlpha = alpha + lr
					* dervativeAlpha(alpha, beta, mu, q, qInverse);
			tempBeta = beta + lr * dervativeBeta(alpha, beta, mu, q, qInverse);

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

	public double dervativeAlpha(double alpha, double beta, double[] mu,
			double[][] q, double[][] qInverse) {
		// partial derivative of l with respect to the beta
		// -1/2 * [ (y-mu)^T * (y-mu) + (R-mu)^T [I+Q^-1Q](mu-y)]
		// +1/2 * Tr(Q^-1)

		// yMinusMu = (y-mu)
		double[] yMinusMu = BasicCalcs.vectorMinusVector(y, mu);

		// first = (y-mu)^T * (y-mu)
		double first = BasicCalcs.multiplyTwoVectors(yMinusMu, yMinusMu);

		// res1 = (R-mu)^T
		double[] rMinusMu = BasicCalcs.vectorMinusVector(r, mu);

		// qInverseQ = Q^-1Q

		double[][] qInverseQ = BasicCalcs.multiplyTwoMatrices(qInverse, q);

		// res2 = I+Q^-1Q
		// res2 = I+qInverseQ
		double[][] iPlusQInverseQ = BasicCalcs.matrixPlusMatrix(
				BasicCalcs.identityMatrix(q.length), qInverseQ);

		// muMinusY = (mu-y)
		double[] muMinusY = BasicCalcs.vectorMinusVector(mu, y);

		// second = (R-mu)^T [I+Q^-1Q](mu-y)
		// second = (R-mu)^T * res1
		double[] res1 = BasicCalcs.multiplyMatrixByAColumnVector(
				iPlusQInverseQ, muMinusY);
		double second = BasicCalcs.multiplyTwoVectors(rMinusMu, res1);

		// result1 = -1/2 * [ (y-mu)^T * (y-mu) + (R-mu)^T [I+Q^-1Q](mu-y)]
		// result1 = -1/2 * (first + second)
		double result1 = -(first + second) / 2;

		// trace = Tr(Q^-1)
		// trace = Tr(qInverse)
		double trace = BasicCalcs.trace(qInverse);

		// result2 = 1/2 * Tr(Q^-1)

		// result2 = 1/2 * trace

		double result2 = trace / 2;
		// finalResult = -1/2 * [ (y-mu)^T * (y-mu) + (R-mu)^T [I+Q^-1Q](mu-y)]
		// +1/2 * Tr(Q^-1)
		double finalResult = result1 + result2;

		return finalResult;
	}

	public double dervativeBeta(double alpha, double beta, double[] mu,
			double[][] q, double[][] qInverse) {
		// partial derivative of l with respect to the beta
		// -1/2 * [ y^T*L*y - (-Q^-1*L*mu)^T*Q*y - mu^T*L*y +
		// (-Q^-1*L*mu)^T*Q*mu] +1/2 * Tr(L*Q^-1)

		double[][] l = calcs.l();

		// first = y^T * L * y
		double first = BasicCalcs.multiplyTwoVectors(y,
				BasicCalcs.multiplyMatrixByAColumnVector(l, y));

		// second = (-Q^-1*L*mu)^T*Q*y

		// product1 = Q^-1*L*mu
		// product1 = qInverse * L * mu
		double[] product1 = BasicCalcs.multiplyMatrixByAColumnVector(
				BasicCalcs.multiplyTwoMatrices(qInverse, l), mu);

		// res1 = - Q^-1*L*mu
		// res1 = -1 * product1
		double[] res1 = BasicCalcs.multiplyVectorByANumber(product1, -1);

		// res2= Q*y
		double[] res2 = BasicCalcs.multiplyMatrixByAColumnVector(q, y);

		// second = (-Q^-1*L*mu)^T*Q*y
		// second = res1 * res2
		double second = BasicCalcs.multiplyTwoVectors(res1, res2);

		// third = mu^T*L*y
		double third = BasicCalcs.multiplyTwoVectors(mu,
				BasicCalcs.multiplyMatrixByAColumnVector(l, y));

		// fourth = (-Q^-1*L*mu)^T*Q*mu
		// fourth = res1 * Q*mu
		// res22= Q*mu
		double[] res22 = BasicCalcs.multiplyMatrixByAColumnVector(q, mu);

		double fourth = BasicCalcs.multiplyTwoVectors(res1, res22);

		// result1 = -1/2 * [ y^T*L*y - (-Q^-1*L*mu)^T*Q*y - mu^T*L*y +
		// (-Q^-1*L*mu)^T*Q*mu]
		// result1 = -1/2 * (first + second - third + fourth)
		double result1 = -(first - second - third + fourth) / 2;

		// trace = Tr(L*Q^-1)
		// trace = Tr(L * qInverse)
		
		double[][] matrix = BasicCalcs.multiplyTwoMatrices(l, qInverse);
		double trace = BasicCalcs.trace(matrix);
	

		// result2 = 1/2 * Tr(L*Q^-1)
		// result2 = 1/2 * trace
		double result2 = trace / 2;

		// finalResult = -1/2 * [ y^T*L*y - (-Q^-1*L*mu)^T*Q*y - mu^T*L*y +
		// (-Q^-1*L*mu)^T*Q*mu] +1/2 * Tr(L*Q^-1)
		double finalResult = result1 + result2;

		return finalResult;
	}

}