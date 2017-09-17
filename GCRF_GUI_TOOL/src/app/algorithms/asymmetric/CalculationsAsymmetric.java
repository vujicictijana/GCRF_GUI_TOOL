package app.algorithms.asymmetric;

import org.ujmp.core.Matrix;

import app.algorithms.basic.BasicCalcs;

public class CalculationsAsymmetric {

	private int n;
	private double[][] s;
	private double[] r;

	public CalculationsAsymmetric(double[][] s, double[] r) {
		super();
		this.n = s.length;
		this.s = s;
		this.r = r;
	}

	public double[][] l() {

		// rowSum(S)
		double[] rowSum = BasicCalcs.rowSum(s);

		// colSum(S)
		double[] colSum = BasicCalcs.colSum(s);

		// rowSum(S) + colSum(S)
		double[] sumRowCol = BasicCalcs.vectorPlusVector(rowSum, colSum);

		// diag(rowSum(S) + colSum(S))
		double[][] diag = BasicCalcs.diag(sumRowCol);

		// 2S
		double[][] twoS = BasicCalcs.multiplyMatrixByANumber(s, 2);

		// diag(rowSum(S) + colSum(S)) - 2S
		double[][] diagMinusTwoS = BasicCalcs.matrixMinusMatrix(diag, twoS);

		// L = 1/2 * diag(rowSum(S) + colSum(S)) - 2S
		double[][] l = BasicCalcs.multiplyMatrixByANumber(diagMinusTwoS, 0.5);

		return l;
	}

	public double[][] alphaI(double alpha) {
		// Alpha * I (I - identity matrix)
		double[][] identity = BasicCalcs.identityMatrix(s.length);
		return BasicCalcs.multiplyMatrixByANumber(identity, alpha);
	}

	public double[][] betaL(double beta) {
		// Beta * L
		double[][] l = l();
		return BasicCalcs.multiplyMatrixByANumber(l, beta);
	}

	public double[][] q(double alpha, double beta) {
		// Q = alpha*I + beta*L
		double[][] alphaI = alphaI(alpha);
		double[][] betaL = betaL(beta);
		return BasicCalcs.matrixPlusMatrix(alphaI, betaL);
	}

	public double[] b(double alpha) {
		return BasicCalcs.multiplyVectorByANumber(r, alpha);
	}

	public double[] mu(double alpha, double beta) {
		// mu = Q^-1*b
		double[] mu = new double[n];
		double[][] qInverse = BasicCalcs.inverse(q(alpha, beta));
		double[] b = b(alpha);
		mu = BasicCalcs.multiplyMatrixByAColumnVector(qInverse, b);
		return mu;
	}

	public double[] y(double alpha, double beta, double p) {
		double[][] qInverse = BasicCalcs.inverse(q(alpha, beta));
		double[] b = b(alpha);
		double[] y = BasicCalcs.multiplyMatrixByAColumnVector(qInverse, b);
		double[] finalY = new double[y.length];
		for (int i = 0; i < y.length; i++) {
			finalY[i] = y[i] + Math.random() * p;
		}
		return finalY;
	}

	public double logLikelihood(double alpha, double beta, double[] y) {
		double[][] q = q(alpha, beta);
		double[] mu = mu(alpha, beta);

		// y*Q*y
		double yQy = BasicCalcs.multiplyTwoVectors(y,
				BasicCalcs.multiplyMatrixByAColumnVector(q, y));

		// y*Q*mu
		double yQmu = BasicCalcs.multiplyTwoVectors(y,
				BasicCalcs.multiplyMatrixByAColumnVector(q, mu));

		// mu*Q*y
		double muQy = BasicCalcs.multiplyTwoVectors(mu,
				BasicCalcs.multiplyMatrixByAColumnVector(q, y));

		// mu*Q*mu
		double muQmu = BasicCalcs.multiplyTwoVectors(mu,
				BasicCalcs.multiplyMatrixByAColumnVector(q, mu));

		// y*Q*y - y*Q*mu - mu*Q*y + mu*Q*mu
		double result1 = yQy - yQmu - muQy + muQmu;

		Matrix m = Matrix.Factory.linkToArray(q);

		double det = m.inv().det();
		//log(det(Q-1)^1/2)
		
		double result2 = Math.log(Math.sqrt(det));

		return (-(result1 / 2)) - result2;
	}
}
