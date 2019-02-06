/** This file is part of GCRF GUI TOOL.

    GCRF GUI TOOL is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GCRF GUI TOOL is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GCRF GUI TOOL.  If not, see <https://www.gnu.org/licenses/>.*/

package gcrf_tool.calculations;

public class CalculationsGCRF implements Calculations {

	protected double[][] s;
	protected double[] r;

	/**
	 * Class constructor specifying similarity matrix (S) and the outputs of
	 * unstructured predictor (R).
	 */
	public CalculationsGCRF(double[][] s, double[] r) {
		super();
		this.s = s;
		this.r = r;
	}

	/**
	 * Calculates Q (arbitrary matrix) according to the equation: Q = 2*Alpha*I
	 * + 2*Beta*L
	 *
	 * @param alpha
	 *            value of Alpha parameter
	 * @param beta
	 *            value of Beta parameter
	 * @return the resulting Q matrix as two dimensional array of double values
	 */
	public double[][] q(double alpha, double beta) {
		// Q = 2*Alpha*I + 2*Beta*L
		double[][] alphaI = alphaI(alpha);
		double[][] betaL = betaL(beta);
		return BasicCalcs.matrixPlusMatrix(BasicCalcs.multiplyMatrixByANumber(alphaI, 2),
				BasicCalcs.multiplyMatrixByANumber(betaL, 2));
	}

	/**
	 * Multiplies Alpha parameter by I (identity matrix).
	 *
	 * @param alpha
	 *            value of Alpha parameter
	 * @return the resulting matrix as two dimensional array of double values
	 */
	public double[][] alphaI(double alpha) {
		// Alpha * I (I - identity matrix)
		double[][] identity = BasicCalcs.identityMatrix(s.length);
		return BasicCalcs.multiplyMatrixByANumber(identity, alpha);
	}

	/**
	 * Calculates Laplacian matrix according to the equation: L = degreeMatrix -
	 * adjacencyMatrix
	 *
	 * @return the Laplacian matrix as two dimensional array of double values
	 */
	public double[][] l() {
		// Laplacian matrix
		// L = degreeMatrix - adjacencyMatrix
		return BasicCalcs.matrixMinusMatrix(BasicCalcs.degreeMatrix(s), s);
	}

	/**
	 * Multiplies Beta parameter by L (Laplacian matrix).
	 *
	 * @param alpha
	 *            value of Beta parameter
	 * @return the resulting matrix as two dimensional array of double values
	 */

	public double[][] betaL(double beta) {
		// Beta * L (L- Laplacian matrix)
		// L = degreeMatrix - adjacencyMatrix
		return BasicCalcs.multiplyMatrixByANumber(l(), beta);
	}

	/**
	 * Calculates mu (the optimal prediction) according to the equation: mu =
	 * Q^-1*b
	 *
	 * @param alpha
	 *            value of Alpha parameter
	 * @param beta
	 *            value of Beta parameter
	 * @return the resulting mu vector as array of double values
	 */
	public double[] mu(double alpha, double beta) {
		// mu = Q^-1*b
		return BasicCalcs.multiplyMatrixByAColumnVector(BasicCalcs.inverse(q(alpha, beta)), b(alpha));
	}

	/**
	 * Calculates b vector ccording to the equation: b=R*alpha
	 *
	 * @param alpha
	 *            value of Alpha parameter
	 * @return the resulting b vector as array of double values
	 */
	public double[] b(double alpha) {
		// b=R*alpha
		return BasicCalcs.multiplyVectorByANumber(r, alpha);
	}

	/**
	 * Generates y (the output variable) according to the equation: y[i] = mu[i]
	 * + (random noise).
	 * 
	 * @param alpha
	 *            value of Alpha parameter
	 * @param beta
	 *            value of Beta parameter
	 * @param p
	 *            coefficient for random noise (random noise = Math.random() * p
	 *            )
	 * @return the resulting b vector as array of double values
	 */

	public double[] y(double alpha, double beta, double p) {

		double[] y = mu(alpha, beta);
		double[] finalY = new double[y.length];
		for (int i = 0; i < y.length; i++) {
			finalY[i] = y[i] + Math.random() * p;
		}
		return finalY;
	}

	/**
	 * Calculates partial derivative with respect to the Alpha parameter.
	 * 
	 * @param alpha
	 *            value of Alpha parameter
	 * @param beta
	 *            value of Beta parameter
	 * @param y
	 *            vector of actual y (the output variable) values
	 * @return the partial derivative as double value
	 */
	public double dervativeAlpha(double alpha, double beta, double[] y) {
		// partial derivative with respect to the beta
		// -1/2 * [ y^Ty + 2R^T(mu-y)-mu^Tmu] +1/2 * Tr(Q^-1)
		double[] mu = mu(alpha, beta);
		double[][] q = q(alpha, beta);

		// yTy = y^Ty
		double yTy = BasicCalcs.multiplyTwoVectors(y, y);

		// muMinusY = (mu-y)
		double[] muMinusY = BasicCalcs.vectorMinusVector(mu, y);

		// res = 2R^T(mu-y)
		double res = BasicCalcs.multiplyTwoVectors(BasicCalcs.multiplyVectorByANumber(r, 2), muMinusY);
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

	/**
	 * Calculates partial derivative with respect to the Beta parameter.
	 * 
	 * @param alpha
	 *            value of Alpha parameter
	 * @param beta
	 *            value of Beta parameter
	 * @param y
	 *            vector of actual y (the output variable) values
	 * @return the partial derivative as double value
	 */
	public double dervativeBeta(double alpha, double beta, double[] y) {
		// partial derivative respect to the beta
		// -1/2 * (y^T*L*y - mu^T*L*mu) + 1/2 * Tr(L*Q^-1)
		double[] mu = mu(alpha, beta);
		double[][] l = l();
		double[][] q = q(alpha, beta);

		// yTLy = y^T*L*y
		double yTLy = BasicCalcs.multiplyTwoVectors(BasicCalcs.multiplyMatrixByAColumnVector(l, y), y);

		// muTLmu = mu^T*L*mu
		double muTLmu = BasicCalcs.multiplyTwoVectors(BasicCalcs.multiplyMatrixByAColumnVector(l, mu), mu);

		// result1 = -1/2 * (y^T*L*y - mu^T*L*mu)
		// result1 = - [yTLy - muTLmu]/2
		double result1 = -(yTLy - muTLmu) / 2;

		// trace = Tr(L*Q^-1)
		double trace = BasicCalcs.trace(BasicCalcs.multiplyTwoMatrices(l, BasicCalcs.inverse(q)));

		// result2 = 1/2 * Tr(L*Q^-1)
		// result2 = trace/2
		double result2 = trace / 2;

		return result1 + result2;
	}

	
	
}
