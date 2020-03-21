
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


package gcrf_tool.gui.logic;

import javax.swing.JFrame;
import org.neuroph.core.data.DataSet;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.threads.DirGCRFTrainMyModelForGUI;
import gcrf_tool.gui.threads.GCRFTrainMyModelForGUI;
import gcrf_tool.gui.threads.UmGCRFTrainMyModelForGUI;
import gcrf_tool.predictors.helper.Helper;
import gcrf_tool.predictors.linearregression.LinearRegression;
import gcrf_tool.predictors.linearregression.MultipleLinearRegression;
import gcrf_tool.predictors.linearregression.MyLR;
import gcrf_tool.predictors.neuralnetwork.MyNN;

public class TrainTestOnNetworks {
	
	
	public static  String callMethodTrain(String method, String path, int noOfNodes,
			double alpha, double beta, double lr, int maxIter, double[] y,
			double[] r, double[][] s, JFrame mainFrame, boolean chckbxStandard, String matlabPath,long proxy,double[][] sTest, double[] rTest,
			double[] yTest) {
		switch (method) {
		case "DirGCRF":
			trainDirGCRF(noOfNodes, path, maxIter, alpha, beta, lr, r, y, s, mainFrame,chckbxStandard,sTest,rTest,yTest);
			break;
		case "GCRF":
			if (BasicCalcs.isSymmetric(s)) {
				trainGCRF(noOfNodes, path, maxIter, alpha, beta, lr, r, y, s,mainFrame,sTest,rTest,yTest);
			} else {
				return "For GCRF method matrix should be symmetric.";
			}
			break;
		case "UmGCRF":
			if (BasicCalcs.isSymmetric(s)) {
				trainUmGCRF(path, r, y, s,mainFrame, matlabPath, proxy,sTest,rTest,yTest);
			} else {
				return "For UmGCRF method matrix should be symmetric.";
			}
			break;
		default:
			return "Unknown method.";
		}
		return null;

	}

	public static void trainDirGCRF(int noOfNodes, String modelFolder, int maxIter,
			double alpha, double beta, double lr, double[] r, double[] y,
			double[][] s, JFrame mainFrame, boolean chckbxStandard,double[][] sTest, double[] rTest,
			double[] yTest) {

		ProgressBar frame = new ProgressBar(maxIter);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		boolean both = false;
		if (chckbxStandard) {
			both = true;
		}
		DirGCRFTrainMyModelForGUI t = new DirGCRFTrainMyModelForGUI(
				modelFolder, frame, mainFrame, s, r, y, alpha, beta, lr,
				maxIter, both,sTest,rTest,yTest);
		// 10, 10
		t.start();

	}

	public static void trainGCRF(int noOfNodes, String modelFolder, int maxIter,
			double alpha, double beta, double lr, double[] r, double[] y,
			double[][] s,JFrame mainFrame,double[][] sTest, double[] rTest,
			double[] yTest) {

		ProgressBar frame = new ProgressBar(maxIter);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		GCRFTrainMyModelForGUI t = new GCRFTrainMyModelForGUI(modelFolder,
				frame, mainFrame, s, r, y, alpha, beta, lr, maxIter,sTest,rTest,yTest);

		t.start();
	}

	public static void trainUmGCRF(String modelFolder, double[] r, double[] y,
			double[][] s,JFrame mainFrame,String matlabPath,long proxy,double[][] sTest, double[] rTest,
			double[] yTest) {
		ProgressBar frame = new ProgressBar("Training");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		UmGCRFTrainMyModelForGUI t = new UmGCRFTrainMyModelForGUI(matlabPath,
				modelFolder, frame, mainFrame, s, r, y, proxy,sTest,rTest,yTest);

		t.start();
	}
	
	public static double callPredictorTrain(String name, String path, String[] x, double[] y, String hidden, String iter) {
		if (name.contains("neural")) {
			int noOfHidden = Integer.parseInt(hidden);
			int noOfIter = Integer.parseInt(iter);
			DataSet trainingSet = Helper.prepareDataForNN(x, y);
			return MyNN.learn(noOfHidden, trainingSet, 0.003, noOfIter, path);
		}
		if (name.contains("linear")) {
			double[][] xNumbers = Helper.prepareDataForLR(x);
			return MyLR.learn(xNumbers, y, path);
		}
		return -7000;

	}
	
	


	public static double callPredictorTest(String path, String[] x, double[] y) {

		if (Writer.checkFolder(path + "/nn")) {

			DataSet testSet = Helper.prepareDataForNN(x, y);
			
			return MyNN.test(path, testSet);

		}
		if (Writer.checkFolder(path + "/mlr")) {
			double[][] xMlr = Helper.prepareDataForLR(x);
			MultipleLinearRegression m = (MultipleLinearRegression) Helper.deserilazie(path + "/mlr/lr.txt");
			return m.test(y, xMlr, path, true);
		}
		if (Writer.checkFolder(path + "/lr")) {
			double[][] xMlr = Helper.prepareDataForLR(x);
			double[] xOne = new double[xMlr.length];
			for (int i = 0; i < xOne.length; i++) {
				xOne[i] = xMlr[i][0];
			}
			LinearRegression lr = (LinearRegression) Helper.deserilazie(path + "/lr/lr.txt");
			return LinearRegression.test(y, xOne, path, lr, true);
		}
		return -7000;

	}

	
}