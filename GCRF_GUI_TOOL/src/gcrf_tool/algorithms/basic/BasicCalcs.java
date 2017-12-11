package gcrf_tool.algorithms.basic;

import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;
import org.ujmp.core.Matrix;

import gcrf_tool.generators.GraphGenerator;

public class BasicCalcs {

	public static double[] vectorMinusVector(double[] first, double[] second) {
		double[] firstMinusSecond = new double[first.length];

		for (int i = 0; i < firstMinusSecond.length; i++) {
			firstMinusSecond[i] = first[i] - second[i];
		}
		return firstMinusSecond;
	}

	public static double[] vectorPlusVector(double[] first, double[] second) {
		double[] firstPlusSecond = new double[first.length];

		for (int i = 0; i < firstPlusSecond.length; i++) {
			firstPlusSecond[i] = first[i] + second[i];
		}
		return firstPlusSecond;
	}

	public static double multiplyTwoVectors(double[] first, double[] second) {
		double sum = 0;
		for (int i = 0; i < first.length; i++) {
			sum += first[i] * second[i];
		}
		return sum;
	}

	public static double trace(double[][] matrix) {
		// sum of the elements on the main diagonal
		Matrix m1 = Matrix.Factory.linkToArray(matrix);
		return m1.trace();
	}

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

	public static double[] multiplyVectorByANumber(double[] vector,
			double number) {
		double[] result = new double[vector.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = number * vector[i];
		}
		return result;
	}

	public static double[][] multiplyMatrixByANumber(double[][] matrix,
			double number) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix);
		Matrix res = m1.times(number);
		return res.toDoubleArray();
	}

	public static double[][] multiplyTwoMatrices(double[][] matrix1,
			double[][] matrix2) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix1);
		Matrix m2 = Matrix.Factory.linkToArray(matrix2);
		Matrix res = m1.times(m2);
		return res.toDoubleArray();
	}

	public static double[][] matrixMinusMatrix(double[][] matrix1,
			double[][] matrix2) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix1);
		Matrix m2 = Matrix.Factory.linkToArray(matrix2);
		Matrix res = m1.minus(m2);
		return res.toDoubleArray();
	}

	public static double[][] matrixPlusMatrix(double[][] matrix1,
			double[][] matrix2) {
		Matrix m1 = Matrix.Factory.linkToArray(matrix1);
		Matrix m2 = Matrix.Factory.linkToArray(matrix2);
		Matrix res = m1.plus(m2);
		return res.toDoubleArray();
	}

	public static double[] multiplyMatrixByAColumnVector(double[][] matrix,
			double[] vector) {
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

	public static double[][] diag(double[] vector) {
		double[][] diag = new double[vector.length][vector.length];
		for (int i = 0; i < diag.length; i++) {
			diag[i][i] = vector[i];
		}
		return diag;
	}

	public static double[][] identityMatrix(int n) {
		double[][] identity = new double[n][n];
		for (int i = 0; i < identity.length; i++) {
			identity[i][i] = 1;
		}
		return identity;
	}

	public static double[][] inverse(double[][] matrix) {
		// Matrix m = Matrix.Factory.linkToArray(matrix);
		// Matrix inverse = m.inv();
		// return inverse.toDoubleArray();
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

	public static double[][] degreeMatrix(double[][] matrix) {
		double[][] degreeMatrix = new double[matrix.length][matrix.length];
		for (int i = 0; i < degreeMatrix.length; i++) {
			degreeMatrix[i][i] = degree(i, matrix);
		}
		return degreeMatrix;
	}

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

	public static double average(double[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum / array.length;
	}

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
