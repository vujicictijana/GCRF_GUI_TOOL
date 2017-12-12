package gcrf_tool.calculations;

import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;
import org.ujmp.core.Matrix;

public class BasicCalcs {

	/**
	 * Returns a vector that is result of subtraction of two vectors.
	 *
	 * @param first
	 *            first vector (minuen) as array of double values
	 * @param second
	 *            second vector (subtrahend) as array of double values
	 * @return the resulting vector as array of double values
	 */
	public static double[] vectorMinusVector(double[] first, double[] second) {
		double[] firstMinusSecond = new double[first.length];

		for (int i = 0; i < firstMinusSecond.length; i++) {
			firstMinusSecond[i] = first[i] - second[i];
		}
		return firstMinusSecond;
	}

	/**
	 * Returns a vector that is result of addition of two vectors.
	 *
	 * @param first
	 *            first vector as array of double values
	 * @param second
	 *            second vector as array of double values
	 * @return the resulting vector as array of double values
	 */

	public static double[] vectorPlusVector(double[] first, double[] second) {
		double[] firstPlusSecond = new double[first.length];

		for (int i = 0; i < firstPlusSecond.length; i++) {
			firstPlusSecond[i] = first[i] + second[i];
		}
		return firstPlusSecond;
	}

	/**
	 * Returns product of two vectors.
	 *
	 * @param first
	 *            first vector as array of double values
	 * @param second
	 *            second vector as array of double values
	 * @return the resulting double value
	 */

	public static double multiplyTwoVectors(double[] first, double[] second) {
		double sum = 0;
		for (int i = 0; i < first.length; i++) {
			sum += first[i] * second[i];
		}
		return sum;
	}

	/**
	 * Calculates the trace of square matrix.
	 *
	 * @param matrix
	 *            square matrix passed as two dimensional array of double values
	 * @return sum of the diagonal elements as double value
	 */

	public static double trace(double[][] matrix) {
		// sum of the elements on the main diagonal
		Matrix m1 = Matrix.Factory.linkToArray(matrix);
		return m1.trace();
	}

	/**
	 * Returns true if matrix is symmetric.
	 *
	 * @param matrix
	 *            two dimensional array of double values
	 * @return true if the given matrix is symmetric, otherwise false
	 */
	public static boolean isSymmetric(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] != matrix[j][i]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns vector that is result of multiplication of vector by a number.
	 *
	 * @param vector
	 *            array of double values
	 * @param number
	 *            double value
	 * @return the resulting vector as array of double values
	 */

	public static double[] multiplyVectorByANumber(double[] vector, double number) {
		double[] result = new double[vector.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = number * vector[i];
		}
		return result;
	}

	/**
	 * Returns matrix that is result of multiplication of matrix by a number.
	 *
	 * @param matrix
	 *            two dimensional array of double values
	 * @param number
	 *            double value
	 * @return the resulting matrix as two dimensional array of double values
	 */

	public static double[][] multiplyMatrixByANumber(double[][] matrix, double number) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix);
		Matrix res = m1.times(number);
		return res.toDoubleArray();
	}

	/**
	 * Returns matrix that is result of multiplication of two matrices.
	 *
	 * @param matrix1
	 *            first matrix passed as two dimensional array of double values
	 * @param matrix2
	 *            second matrix passed as two dimensional array of double values
	 * @return the resulting matrix as two dimensional array of double values
	 */
	public static double[][] multiplyTwoMatrices(double[][] matrix1, double[][] matrix2) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix1);
		Matrix m2 = Matrix.Factory.linkToArray(matrix2);
		Matrix res = m1.times(m2);
		return res.toDoubleArray();
	}

	/**
	 * Returns matrix that is result of subtraction of two matrices.
	 *
	 * @param matrix1
	 *            first matrix passed as two dimensional array of double values
	 * @param matrix2
	 *            second matrix passed as two dimensional array of double values
	 * @return the resulting matrix as two dimensional array of double values
	 */
	public static double[][] matrixMinusMatrix(double[][] matrix1, double[][] matrix2) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix1);
		Matrix m2 = Matrix.Factory.linkToArray(matrix2);
		Matrix res = m1.minus(m2);
		return res.toDoubleArray();
	}

	/**
	 * Returns matrix that is result of addition of two matrices.
	 *
	 * @param matrix1
	 *            first matrix passed as two dimensional array of double values
	 * @param matrix2
	 *            second matrix passed as two dimensional array of double values
	 * @return the resulting matrix as two dimensional array of double values
	 */
	public static double[][] matrixPlusMatrix(double[][] matrix1, double[][] matrix2) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix1);
		Matrix m2 = Matrix.Factory.linkToArray(matrix2);
		Matrix res = m1.plus(m2);
		return res.toDoubleArray();
	}

	/**
	 * Returns vector that is result of multiplication of matrix by vector.
	 *
	 * @param matrix
	 *            matrix passed as two dimensional array of double values
	 * @param vector
	 *            vector passed as array of double values
	 * @return the resulting vector as array of double values
	 */
	public static double[] multiplyMatrixByAColumnVector(double[][] matrix, double[] vector) {
		double[] result = new double[matrix.length];
		double temp = 0;
		for (int i = 0; i < matrix.length; i++) {
			temp = 0;
			for (int j = 0; j < matrix.length; j++) {
				temp += matrix[i][j] * vector[j];
			}
			result[i] = temp;
		}
		return result;
	}

	/**
	 * Returns vector containing the sum of each row of the given matrix.
	 *
	 * @param matrix
	 *            matrix passed as two dimensional array of double values
	 * @return the resulting vector as array of double values
	 */
	public static double[] rowSum(double[][] matrix) {
		double[] rowSum = new double[matrix.length];
		double sum = 0;
		for (int i = 0; i < matrix.length; i++) {
			sum = 0;
			for (int j = 0; j < matrix.length; j++) {
				sum += matrix[i][j];
			}
			rowSum[i] = sum;
		}
		return rowSum;
	}

	/**
	 * Returns vector containing the sum of each column of the given matrix.
	 *
	 * @param matrix
	 *            matrix passed as two dimensional array of double values
	 * @return the resulting vector as array of double values
	 */
	public static double[] colSum(double[][] matrix) {
		double[] colSum = new double[matrix.length];
		double sum = 0;
		for (int i = 0; i < matrix.length; i++) {
			sum = 0;
			for (int j = 0; j < matrix.length; j++) {
				sum += matrix[j][i];
			}
			colSum[i] = sum;
		}
		return colSum;
	}

	/**
	 * Returns the diagonal matrix from the given vector.
	 *
	 * @param vector
	 *            vector passed as array of double values
	 * @return the resulting diagonal matrix as two dimensional array of double
	 *         values
	 */
	public static double[][] diag(double[] vector) {
		double[][] diag = new double[vector.length][vector.length];
		for (int i = 0; i < diag.length; i++) {
			diag[i][i] = vector[i];
		}
		return diag;
	}

	/**
	 * Returns the square identity matrix of size n, with ones on the diagonal
	 * and zeros elsewhere.
	 *
	 * @param n
	 *            matrix order (number of rows and columns)
	 * @return the resulting square identity matrix as two dimensional array of
	 *         double values
	 */
	public static double[][] identityMatrix(int n) {
		double[][] identity = new double[n][n];
		for (int i = 0; i < identity.length; i++) {
			identity[i][i] = 1;
		}
		return identity;
	}

	/**
	 * Returns inverse of the given matrix.
	 *
	 * @param matrix
	 *            matrix passed as two dimensional array of double values
	 * @return the resulting inverse matrix as two dimensional array of double
	 *         values
	 */
	public static double[][] inverse(double[][] matrix) {
		BasicMatrix.Factory<PrimitiveMatrix> mtrxFactory = PrimitiveMatrix.FACTORY;
		PrimitiveMatrix mtrxA = mtrxFactory.rows(matrix);
		PrimitiveMatrix mtrxI = mtrxA.invert();
		return mtrxI.toRawCopy2D();
	}

	private static int degree(int row, double[][] matrix) {
		int degree = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[row][i] != 0) {
				degree++;
			}
		}
		return degree;
	}

	/**
	 * Returns degree matrix for the given matrix. The diagonal matrix contains
	 * information about the degree of each vertex (the number of edges attached
	 * to each vertex).
	 * 
	 * @param matrix
	 *            matrix passed as two dimensional array of double values
	 * @return the resulting degree matrix as two dimensional array of double
	 *         values
	 */
	public static double[][] degreeMatrix(double[][] matrix) {
		double[][] degreeMatrix = new double[matrix.length][matrix.length];
		for (int i = 0; i < degreeMatrix.length; i++) {
			degreeMatrix[i][i] = degree(i, matrix);
		}
		return degreeMatrix;
	}

	/**
	 * Returns standard deviation for the given array.
	 * 
	 * @param array
	 *            array of double values
	 * @return the standard deviation as double value
	 */
	public static double standardDeviation(double[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		double avg = sum / array.length;
		double sum2 = 0;
		for (int i = 0; i < array.length; i++) {
			sum2 += Math.pow(array[i] - avg, 2);
		}
		return Math.sqrt(sum2 / array.length);
	}

	/**
	 * Returns the average value of elements in the given array.
	 * 
	 * @param array
	 *            array of double values
	 * @return the average value
	 */
	public static double average(double[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum / array.length;
	}

	/**
	 * Returns the coefficient of determination (R^2) - statistical measure of
	 * how close the data are to the fitted regression line.
	 * 
	 * @param output
	 *            the predicted values passed as array of double values
	 * @param expectedY
	 *            the actual values passed as array of double values
	 * @return R^2 coefficient as double value
	 */
	public static double rSquared(double[] output, double[] expectedY) {
		double avg = average(output);
		double firstSum = 0;
		double secondSum = 0;
		for (int i = 0; i < output.length; i++) {
			firstSum += Math.pow(expectedY[i] - output[i], 2);
			secondSum += Math.pow(expectedY[i] - avg, 2);
		}
		return 1 - (firstSum / secondSum);
	}
	
	public static double rSquaredWitNaN(double[] output, double[] expectedY) {
		double avg = averageWitNaN(expectedY);
		double firstSum = 0;
		double secondSum = 0;
		for (int i = 0; i < output.length; i++) {
			if (!Double.isNaN(expectedY[i])) {
				firstSum += Math.pow(expectedY[i] - output[i], 2);
				secondSum += Math.pow(expectedY[i] - avg, 2);
			}
		}
		return 1 - (firstSum / secondSum);
	}
	
	public static double averageWitNaN(double[] array) {
		double sum = 0;
		int no = 0;
		for (int i = 0; i < array.length; i++) {
			if (!Double.isNaN(array[i])) {
				sum += array[i];
				no++;
			}
		}
		// System.out.println(no);
		return sum / no;
	}

}
