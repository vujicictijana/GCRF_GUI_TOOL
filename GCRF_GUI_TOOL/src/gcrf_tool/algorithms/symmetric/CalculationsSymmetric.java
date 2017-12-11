package gcrf_tool.algorithms.symmetric;

import gcrf_tool.algorithms.basic.BasicCalcs;



public class CalculationsSymmetric {

	private double[][] s;
	private double[] r;

	public CalculationsSymmetric(double[][] s, double[] r) {
		super();
		this.s = s;
		this.r = r;
	}

	public double[][] q(double alpha, double beta) {
		// Q = 2*Alpha*I + 2*Beta*L
		double[][] alphaI = alphaI(alpha);
		double[][] betaL = betaL(beta);
		return BasicCalcs.matrixPlusMatrix(
				BasicCalcs.multiplyMatrixByANumber(alphaI, 2),
				BasicCalcs.multiplyMatrixByANumber(betaL, 2));
	}

	public double[][] alphaI(double alpha) {
		// Alpha * I (I - identity matrix)
		double[][] identity = BasicCalcs.identityMatrix(s.length);
		return BasicCalcs.multiplyMatrixByANumber(identity, alpha);
	}

	public double[][] l() {
		// Laplacian matrix
		// L = degreeMatrix - adjacencyMatrix
		return BasicCalcs.matrixMinusMatrix(BasicCalcs.degreeMatrix(s), s);
	}

	public double[][] betaL(double beta) {
		// Beta * L (L- Laplacian matrix)
		// L = degreeMatrix - adjacencyMatrix
		return BasicCalcs.multiplyMatrixByANumber(l(), beta);
	}

	public double[] mu(double alpha, double beta) {
		// mu = Q^-1*b
		return BasicCalcs.multiplyMatrixByAColumnVector(
				BasicCalcs.inverse(q(alpha, beta)), b(alpha));
	}

	public double[] b(double alpha) {
		// b=R*alpha
		return BasicCalcs.multiplyVectorByANumber(r, alpha);
	}

	public double[] y(double alpha, double beta, double p) {
		double[] y = mu(alpha, beta);
		double[] finalY = new double[y.length];
		for (int i = 0; i < y.length; i++) {
			finalY[i] = y[i] + Math.random() * p;
		}
		return finalY;
	}

}
