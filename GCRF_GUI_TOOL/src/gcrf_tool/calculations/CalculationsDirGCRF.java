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

public class CalculationsDirGCRF extends CalculationsGCRF implements Calculations {

	/**
	 * Class constructor specifying similarity matrix (S) and the outputs of
	 * unstructured predictor (R).
	 */
	public CalculationsDirGCRF(double[][] s, double[] r) {
		super(s, r);
	}

	/**
	 * Calculates Laplacian matrix according to the equation: L = 1/2 *
	 * diag(rowSum(matrix) + colSum(matrix)) - 2*matrix
	 *
	 * @return the Laplacian matrix as two dimensional array of double values
	 */
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

	/**
	 * Calculates Q (arbitrary matrix) according to the equation: Q = alpha*I +
	 * beta*L
	 *
	 * @param alpha
	 *            value of Alpha parameter
	 * @param beta
	 *            value of Beta parameter
	 * @return the resulting Q matrix as two dimensional array of double values
	 */

	public double[][] q(double alpha, double beta) {
		// Q = alpha*I + beta*L
		double[][] alphaI = alphaI(alpha);
		double[][] betaL = betaL(beta);
		return BasicCalcs.matrixPlusMatrix(alphaI, betaL);
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
		double[] mu = mu(alpha, beta);
		double[][] q = q(alpha, beta);
		double[][] qInverse = BasicCalcs.inverse(q);
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
		double[][] iPlusQInverseQ = BasicCalcs.matrixPlusMatrix(BasicCalcs.identityMatrix(q.length), qInverseQ);

		// muMinusY = (mu-y)
		double[] muMinusY = BasicCalcs.vectorMinusVector(mu, y);

		// second = (R-mu)^T [I+Q^-1Q](mu-y)
		// second = (R-mu)^T * res1
		double[] res1 = BasicCalcs.multiplyMatrixByAColumnVector(iPlusQInverseQ, muMinusY);
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
		// partial derivative of l with respect to the beta
		// -1/2 * [ y^T*L*y - (-Q^-1*L*mu)^T*Q*y - mu^T*L*y +
		// (-Q^-1*L*mu)^T*Q*mu] +1/2 * Tr(L*Q^-1)
		double[] mu = mu(alpha, beta);
		double[][] q = q(alpha, beta);
		double[][] qInverse = BasicCalcs.inverse(q);
		double[][] l = l();

		// first = y^T * L * y
		double first = BasicCalcs.multiplyTwoVectors(y, BasicCalcs.multiplyMatrixByAColumnVector(l, y));

		// second = (-Q^-1*L*mu)^T*Q*y

		// product1 = Q^-1*L*mu
		// product1 = qInverse * L * mu
		double[] product1 = BasicCalcs.multiplyMatrixByAColumnVector(BasicCalcs.multiplyTwoMatrices(qInverse, l), mu);

		// res1 = - Q^-1*L*mu
		// res1 = -1 * product1
		double[] res1 = BasicCalcs.multiplyVectorByANumber(product1, -1);

		// res2= Q*y
		double[] res2 = BasicCalcs.multiplyMatrixByAColumnVector(q, y);

		// second = (-Q^-1*L*mu)^T*Q*y
		// second = res1 * res2
		double second = BasicCalcs.multiplyTwoVectors(res1, res2);

		// third = mu^T*L*y
		double third = BasicCalcs.multiplyTwoVectors(mu, BasicCalcs.multiplyMatrixByAColumnVector(l, y));

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
