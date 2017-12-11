package gcrf_tool.data.connector;

import org.ujmp.core.Matrix;

public class GraphConnector {

	public static double[][] connectMatrices(Matrix[] matrices) {
		int rows = 0;
		int cols = 0;
		for (int i = 0; i < matrices.length; i++) {
			// rows += matrices[i].getRowDimension();
			rows += matrices[i].getRowCount();
			// cols += matrices[i].getColumnDimension();
			cols += matrices[i].getColumnCount();
		}
		double[][] finalMatrix = new double[rows][cols];
		int rowIndex = 0;
		int colIndex = 0;
		int begin = 0;
		for (int i = 0; i < matrices.length; i++) {
			Matrix m = matrices[i];
			begin = colIndex;
			// for (int j = 0; j < m.getRowDimension(); j++) {
			// for (int j2 = 0; j2 < m.getColumnDimension(); j2++) {
			// finalMatrix[rowIndex][colIndex] = m.get(j, j2);
			for (int j = 0; j < m.getRowCount(); j++) {
				for (int j2 = 0; j2 < m.getColumnCount(); j2++) {
					finalMatrix[rowIndex][colIndex] = m.getAsDouble(j,j2);
					colIndex++;
				}
				rowIndex++;
				colIndex = begin;
			}
//			colIndex = colIndex + m.getColumnDimension();
			colIndex = colIndex + (int) m.getColumnCount();
		}
		return finalMatrix;

	}

	public static double[] connectArrays(double[][] arrays) {
		int no = 0;
		for (int i = 0; i < arrays.length; i++) {
			no += arrays[i].length;
		}

		double[] finalArray = new double[no];
		int i = 0;
		for (int j = 0; j < arrays.length; j++) {
			for (int j2 = 0; j2 < arrays[j].length; j2++) {
				finalArray[i] = arrays[j][j2];
				i++;
			}
		}

		return finalArray;
	}
}
