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

package gcrf_tool.predictors.neuralnetwork;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.file.Writer;
import gcrf_tool.predictors.helper.Helper;

public class MyNN {
	public static double learn(int hidden, DataSet trainingSet,
			double maxError, int maxIter, String folder) {
		BackPropagation b = new BackPropagation();
		b.setMaxError(maxError);
		b.setMaxIterations(maxIter);

		if (trainingSet != null) {
			MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(
					TransferFunctionType.TANH, trainingSet.getRowAt(0)
							.getInput().length, hidden, 1);
			neuralNetwork.learn(trainingSet, b);
			double[] outputs = new double[trainingSet.getRows().size()];
			String[] rArray = new String[outputs.length];

			int i = 0;
			for (DataSetRow row : trainingSet.getRows()) {
				neuralNetwork.setInput(row.getInput());
				neuralNetwork.calculate();
				outputs[i] = Helper.round(neuralNetwork.getOutput()[0]);
				rArray[i] = outputs[i] + "";
				i++;
			}
			if (folder != null) {
				Writer.createFolder(folder + "/nn");
				neuralNetwork.save(folder + "/nn/nn.nnet");
				Writer.write(rArray, folder + "/data/r.txt");
			}
			return testWithSame(trainingSet, neuralNetwork);
		} else {
			return -5000;
		}
	}

	public static double learnAndTest(int hidden, DataSet data,
			double maxError, int maxIter, String folder, int total, int traning) {

		BackPropagation b = new BackPropagation();
		b.setMaxError(maxError);
		b.setMaxIterations(maxIter);

		DataSet trainingSet = new DataSet(data.getInputSize(),
				data.getOutputSize());

		DataSet testSet = new DataSet(data.getInputSize(), data.getOutputSize());
		int index = 0;
		for (int i = 0; i < data.getRows().size(); i++) {

			if (index == total - 1) {
				testSet.addRow(data.getRowAt(i));
				index = 0;
			} else {
				if (index < traning) {
					trainingSet.addRow(data.getRowAt(i));
				} else {
					testSet.addRow(data.getRowAt(i));
				}
				index++;
			}
		}
		// System.out.println(trainingSet.getRows().size());
		// System.out.println(trainingSet);
		// System.out.println("\n" +testSet.getRows().size());
		// System.out.println(testSet);
		if (trainingSet.getRows().size() != 0 && testSet.getRows().size() != 0) {
			MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(
					TransferFunctionType.TANH, trainingSet.getRowAt(0)
							.getInput().length, hidden, 1);
			neuralNetwork.learn(trainingSet, b);
			double[] outputs = new double[trainingSet.getRows().size()];
			String[] rArray = new String[outputs.length];

			int i = 0;
			for (DataSetRow row : trainingSet.getRows()) {
				neuralNetwork.setInput(row.getInput());
				neuralNetwork.calculate();
				outputs[i] = Helper.round(neuralNetwork.getOutput()[0]);
				rArray[i] = outputs[i] + "";
				i++;
			}
			if (folder != null) {
				Writer.createFolder(folder + "/nn");
				neuralNetwork.save(folder + "/nn/nn.nnet");
				Writer.write(rArray, folder + "/data/r.txt");
			}

			return test(folder, testSet);
		} else {
			return -5000;
		}
	}

	private static double testWithSame(DataSet trainingSet,
			MultiLayerPerceptron neuralNetwork) {
		double[] outputs = new double[trainingSet.getRows().size()];
		double[] expectedY = new double[trainingSet.getRows().size()];
		String[] rArray = new String[outputs.length];

		int i = 0;
		for (DataSetRow row : trainingSet.getRows()) {
			neuralNetwork.setInput(row.getInput());
			neuralNetwork.calculate();
			outputs[i] = Helper.round(neuralNetwork.getOutput()[0]);
			expectedY[i] = row.getDesiredOutput()[0];
			rArray[i] = outputs[i] + "";
			i++;
		}
		return BasicCalcs.rSquared(outputs, expectedY);
	}

	public static double test(String folder, DataSet testSet) {
		MultiLayerPerceptron neuralNetwork = (MultiLayerPerceptron) NeuralNetwork
				.createFromFile(folder + "/nn/nn.nnet");
		if (testSet == null) {
			return -5000;
		}
		try {
			double[] outputs = new double[testSet.getRows().size()];
			double[] expectedY = new double[testSet.getRows().size()];
			String[] rArray = new String[outputs.length];
			int i = 0;
			for (DataSetRow row : testSet.getRows()) {
				neuralNetwork.setInput(row.getInput());
				neuralNetwork.calculate();
				outputs[i] = neuralNetwork.getOutput()[0];
				expectedY[i] = row.getDesiredOutput()[0];
				rArray[i] = outputs[i] + "";

				i++;
			}
			if (folder != null) {
				Writer.write(rArray, folder + "/data/rTest.txt");
			}
			return BasicCalcs.rSquared(outputs, expectedY);
		} catch (Exception e) {
			return -9000;
		}
	}

	public static double testNoY(String folder, String[] x) {
		MultiLayerPerceptron neuralNetwork = (MultiLayerPerceptron) NeuralNetwork
				.createFromFile(folder + "/nn/nn.nnet");
		double[] y = new double[x.length];
		DataSet testSet = Helper.prepareDataForNN(x, y);
		if (testSet == null) {
			return -5000;
		}
		double[] outputs = new double[testSet.getRows().size()];
		String[] rArray = new String[outputs.length];
		int i = 0;
		for (DataSetRow row : testSet.getRows()) {
			neuralNetwork.setInput(row.getInput());
			neuralNetwork.calculate();
			outputs[i] = neuralNetwork.getOutput()[0];
			rArray[i] = outputs[i] + "";
			i++;
		}
		if (folder != null) {
			Writer.write(rArray, folder + "/data/rPredict.txt");
		}
		return 1;
	}

}
