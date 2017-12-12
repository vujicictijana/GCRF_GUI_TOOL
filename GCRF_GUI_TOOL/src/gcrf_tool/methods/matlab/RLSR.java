package gcrf_tool.methods.matlab;

import java.io.IOException;
import java.text.DecimalFormat;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class RLSR {

	public static String train(String matlabPath, double[][] y, double[][] x, int noTime, int training, int maxIter,
			int noOfNodes, int validation, int noX, int lfSize, int test, int iterNN, int hidden, int iterSSE,
			int iterLs, String lambda, ProgressBar frame, String modelFolder, long proxyTime) {
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder().setHidden(true)
				.setProxyTimeout(proxyTime).setMatlabLocation(matlabPath).build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		MatlabProxy proxy;
		try {
			proxy = factory.getProxy();
			String path =  Reader.jarFile() + "/matlab/RLSR";
			proxy.eval("addpath('" + path + "')");

			proxy.eval("addpath(genpath('" + path + "/codes'))");

			// neural network parameters
			proxy.eval("parameters;");
			proxy.eval("nn_params = params;");
			proxy.eval("nn_params.Nh = " + hidden + ";");
			proxy.eval("nn_params.nIter = " + iterNN + ";");
			proxy.eval("nn_params.save = 0;");
			proxy.eval("nn_params.func = {'sigm', 'none'};");
			proxy.eval("nn_params.early.use = 1;");
			proxy.eval("nn_params.nFig = 3;");

			// Structure learning parameters
			proxy.eval("sse_params = struct;");
			proxy.eval("sse_params.quiet = 1;");
			proxy.eval("sse_params.max_iters = " + iterSSE + ";");
			proxy.eval("sse_params.ls_max_iters = " + iterLs + ";");
			proxy.eval("sse_params.tol = 1e-6;");
			proxy.eval("sse_params.col_tol = 1e-6;");
			proxy.eval("sse_params.alpha = 1;");

			// Training parameters
			proxy.setVariable("maxIter", maxIter);
			proxy.setVariable("trainSize", training);
			proxy.setVariable("validSize", validation);
			proxy.setVariable("testSize", test);
			proxy.setVariable("LFSize", lfSize);
			proxy.eval("lambda_set = [" + lambda + "];");

			// csv data
			// proxy.eval("x = csvread('" + path + "/data/X.csv');");
			// proxy.eval("y = csvread('" + path + "/data/Y.csv');");

			// data

			MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
			processor.setNumericArray("x", new MatlabNumericArray(x, null));
			processor.setNumericArray("y", new MatlabNumericArray(y, null));

			proxy.eval("[trainValidX, trainValidY, testX, testY] = splitData(x, y, trainSize, validSize, testSize);");

			// run train

			String message = null;
			try {
				proxy.eval("[best_layer, best_Lambda, best_Theta, best_lambda, time_spent] = "
						+ "RLSR_train_valid(trainValidX, trainValidY, trainSize, validSize, lambda_set, maxIter, LFSize, nn_params, sse_params);");

				proxy.eval("[test_mse,pred_test] = RLSR_test(testX, testY, best_layer, best_Lambda, best_Theta);");

				proxy.eval("rmpath('" + path + "')");

				proxy.eval("pred_test = transpose(pred_test);");
				MatlabNumericArray array = processor.getNumericArray("pred_test");
				double[][] outputs = array.getRealArray2D();
				double mse = ((double[]) proxy.getVariable("test_mse"))[0];

				Writer.createFolder(modelFolder + "/parameters");
				String fileName = modelFolder + "/parameters/RLSR.mat";

				proxy.setVariable("fileName", fileName);
				proxy.eval("Data = struct;");
				proxy.eval("Data.best_layer = best_layer;");
				proxy.eval("Data.best_Lambda = best_Lambda;");
				proxy.eval("Data.best_Theta = best_Theta;");
				proxy.eval("save(fileName,'Data','-v7.3')");

				DecimalFormat df = new DecimalFormat("#.####");
				String export = exportResults(y, outputs, modelFolder);
				message = "RLSR results: \n* MSE: " + df.format(mse) + export;
				proxy.disconnect();

			} catch (Exception e) {
				e.printStackTrace();
				message = "An internal MATLAB exception occurred. Please check your data.";
			}

			// close matlab
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("taskkill /F /IM MATLAB.exe");
			} catch (IOException e) {
				// e.printStackTrace();
			}
			frame.setVisible(false);
			return message;

		} catch (MatlabConnectionException e) {
			if (e.getMessage().contains("milliseconds")) {
				return e.getMessage() + ". Increase proxy timeout in Settings->Configuration.";
			}
		} catch (MatlabInvocationException e) {
			// e.printStackTrace();
		}
		frame.setVisible(false);
		return "Connection with MATLAB cannot be established.";
	}

	private static String exportResults(double[][] y, double[][] outputs, String folder) {
		Writer.createFolder(folder + "/test");
		String fileName = folder + "/test/results";
		int colsOutput = outputs[0].length;
		int colsY = y[0].length;
		double[] array = null;
		double[] arrayY = null;
		double sum = 0;
		double r2 = 0;
		DecimalFormat df = new DecimalFormat("#.####");
		int firstY = colsY - colsOutput - 1;
		for (int i = 0; i < colsOutput; i++) {
			array = new double[outputs.length];
			arrayY = new double[outputs.length];
			for (int j = 0; j < array.length; j++) {
				array[j] = outputs[j][i];
				arrayY[j] = y[j][firstY];
			}

			r2 = BasicCalcs.rSquaredWitNaN(array, arrayY);
			sum += r2;
			String[] text = exportTxt(r2, array);
			Writer.write(text, fileName + "T" + (firstY + 1) + ".txt");
			firstY++;
		}
		double avg = sum / colsOutput;
		return "\n* Average R^2 value: " + df.format(avg) + "\n* Results are successfully exported. \n* File location: "
				+ folder + "/test";
	}

	private static String[] exportTxt(double r2, double[] outputs) {
		DecimalFormat df = new DecimalFormat("#.######");
		String[] txt = new String[outputs.length + 1];

		for (int i = 0; i < outputs.length; i++) {
			txt[i] = outputs[i] + "";
		}

		txt[outputs.length] = "R^2 RLSR: " + df.format(r2);

		return txt;
	}
}
