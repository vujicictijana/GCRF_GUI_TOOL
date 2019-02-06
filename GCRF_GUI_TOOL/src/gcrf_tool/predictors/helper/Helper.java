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

package gcrf_tool.predictors.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import gcrf_tool.data.generators.ArrayGenerator;
import gcrf_tool.data.generators.GraphGenerator;
import gcrf_tool.predictors.linearregression.TemporalData;

public class Helper {

	public static double normalize(double x, double max, double min) {
		DecimalFormat df = new DecimalFormat("#.##");
		double res = (x - min) / (max - min);
		return Double.parseDouble(df.format(res));
	}

	public static double round(double d) {
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.parseDouble(df.format(d));
	}

	public static double average(double[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum / array.length;
	}

	public static double rootMeanSquaredError(double[] expectedY,
			double[] outputs) {
		double firstSum = 0;
		for (int i = 0; i < outputs.length; i++) {
			firstSum += Math.pow(outputs[i] - expectedY[i], 2);
		}
		return Math.sqrt(firstSum / (double) expectedY.length);
	}

	public static int[] getInts(String l) {
		String[] line = l.split("-");
		int[] ints = new int[line.length];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = Integer.parseInt(line[i]);
		}
		return ints;
	}

	public static void showMatrix(double[][] s) {
		DecimalFormat df = new DecimalFormat("#.##");
		for (int i = 0; i < s.length; i++) {
			double[] s1 = s[i];
			for (int j = 0; j < s1.length; j++) {
				System.out.print(df.format(s[i][j]) + "\t");
			}
			System.out.println();
		}
	}

	public static void showArray(double[] array) {
		DecimalFormat df = new DecimalFormat("#.##");

		for (int j = 0; j < array.length; j++) {
			System.out.print(df.format(array[j]) + "\t");
		}
	}

	public static double[][] connectMatrices(double[][] m1, double[][] m2) {
		double[][] finalMatrix = new double[m1.length][m1.length];
		double avg = 0;
		for (int j = 0; j < finalMatrix.length; j++) {
			for (int i = 0; i < finalMatrix.length; i++) {
				avg = (m1[i][j] + m2[i][j]) / 2;
				finalMatrix[i][j] = avg;
			}
		}
		return finalMatrix;
	}

	public static DataSet prepareDataForNN(String[] data, double[] y) {
		int no = data[0].split(",").length;
		DataSet d = new DataSet(no, 1);
		double[][] x = getAllDataNormalized(data, no);
		double[] yNormalized = getArrayNormalized(y);
		if (x == null || yNormalized == null) {
			return null;
		}
		for (int i = 0; i < data.length; i++) {
			d.addRow(new DataSetRow(x[i], new double[] { yNormalized[i] }));
		}
		// System.out.println(d);
		return d;
	}

	public static DataSet prepareTemporalDataForNN(String[] data, String[] y,
			int x, int time, boolean normalized) {
		DataSet d = new DataSet(x, 1);
		double[] oneX = null;
		String[] line = null;
		String[] lineY = null;
		for (int i = 0; i < data.length; i++) {
			line = data[i].split(",");
			lineY = y[i].split(",");
			int indexY = 0;
			int indexX = 0;
			oneX = new double[x];
			for (int j = 0; j < line.length; j++) {
				oneX[indexX] = Double.parseDouble(line[j]);
				if (normalized) {
					if (oneX[indexX] > 1 || oneX[indexX] < 0) {
						return null;
					}
				}
				indexX++;
				if ((j + 1) % time == 0) {
					double yValue = Double.parseDouble(lineY[indexY]);
					if (normalized) {
						if (yValue > 1 || yValue < 0) {
							if (yValue != -9999) {
								return null;
							} else {
								yValue = 0;
							}
						}
					} else {
						if (yValue == -9999) {
							yValue = 0;
						}
					}
					d.addRow(new DataSetRow(oneX, new double[] { yValue }));
					oneX = new double[x];
					indexY++;
					indexX = 0;
				}
			}
		}
		// System.out.println(d);
		return d;
	}

	public static double[][] putNaN(double[][] y) {
		for (int i = 0; i < y.length; i++) {
			double[] array = y[i];
			for (int j = 0; j < array.length; j++) {
				if (y[i][j] == -9999) {
					y[i][j] = Double.NaN;
				}
			}
		}
		return y;
	}

	public static double[][] getAllDataNormalized(String[] data, int no) {
		double[][] xValues = new double[data.length][no];
		String[] values = null;
		for (int i = 0; i < no; i++) {
			double[] array = new double[data.length];
			for (int j = 0; j < data.length; j++) {
				values = data[j].split(",");
				if (values.length != no) {
					return null;
				} else {
					array[j] = Double.parseDouble(values[i]);
				}
			}
			double[] normalized = getArrayNormalized(array);
			for (int j = 0; j < normalized.length; j++) {
				xValues[j][i] = normalized[j];
			}
		}

		return xValues;

	}

	public static double[] getArrayNormalized(double[] array) {

		boolean normalize = false;
		double min = array[0];
		double max = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] > 1 || array[i] < 0) {
				normalize = true;
			}
			if (array[i] > max) {
				max = array[i];
			}
			if (array[i] < min) {
				min = array[i];
			}
		}
		// System.out.println(min + "-" + max);
		if (normalize) {
			double[] normalized = new double[array.length];
			for (int i = 0; i < normalized.length; i++) {
				normalized[i] = normalize(array[i], max, min);
			}
			return normalized;
		} else {
			// System.out.println("NE TREBA");
			return array;
		}

	}

	public static double[][] prepareDataForLR(String[] data) {
		int no = data[0].split(",").length;
		double[][] x = getAllDataNormalized(data, no);
		String[] values = null;
		for (int i = 0; i < data.length; i++) {
			values = data[i].split(",");
			if (values.length != no) {
				return null;
			} else {
				for (int j = 0; j < values.length; j++) {
					x[i][j] = Double.parseDouble(values[j]);
				}
			}
		}
		return x;
	}

	public static TemporalData prepareTemporalDataForLR(String[] data,
			String[] y, int x, int time, int nodes, int traning) {
		DataSet d = prepareTemporalDataForNN(data, y, x, time, false);
		DataSet trainingSet = new DataSet(d.getInputSize(), d.getOutputSize());
		DataSet testSet = new DataSet(d.getInputSize(), d.getOutputSize());
		int index = 0;
		for (int i = 0; i < d.getRows().size(); i++) {

			if (index == time - 1) {
				testSet.addRow(d.getRowAt(i));
				index = 0;
			} else {
				if (index < traning) {
					trainingSet.addRow(d.getRowAt(i));
				} else {
					testSet.addRow(d.getRowAt(i));
				}
				index++;
			}
		}
		double[][] xTrain = new double[nodes * traning][x];
		double[] yTrain = new double[nodes * traning];

		int trainInd = 0;
		for (DataSetRow r : trainingSet.getRows()) {
			xTrain[trainInd] = r.getInput();
			yTrain[trainInd] = r.getDesiredOutput()[0];
			trainInd++;
		}
		double[][] xTest = new double[nodes * (time - traning)][x];
		double[] yTest = new double[nodes * (time - traning)];

		int testInd = 0;
		for (DataSetRow r : testSet.getRows()) {
			xTest[testInd] = r.getInput();
			yTest[testInd] = r.getDesiredOutput()[0];
			testInd++;
		}
		return new TemporalData(xTrain, yTrain, xTest, yTest);
	}

	public static String serilazie(Object o, String filePath) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(o);
			out.close();
			fileOut.close();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Saving predictor failed.";

	}

	public static Object deserilazie(String filePath) {
		try {
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object o = in.readObject();
			in.close();
			fileIn.close();
			return o;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Reading predictor failed.";

	}

	public static double[][][] get3DArray(double[][] matrix, int time,
			int nodes, int x) {
		double[][][] result = new double[time][nodes][x];
		int firstCol = 0;
		for (int i = 0; i < time; i++) {
			double[][] oneTime = new double[nodes][x];
			for (int j = 0; j < nodes; j++) {
				int col = firstCol;
				for (int j2 = 0; j2 < x; j2++) {
					oneTime[j][j2] = matrix[j][col];
					col++;
				}
			}
			firstCol = firstCol + x;
			// GraphGenerator.showMatrix(oneTime);
			result[i] = oneTime;
		}

		return result;
	}
}
