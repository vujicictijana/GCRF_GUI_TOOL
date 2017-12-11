package gcrf_tool.algorithms.matlab;

import java.io.IOException;
import java.net.URL;

import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.frames.MainFrame;
import gcrf_tool.frames.ProgressBar;
import gcrf_tool.predictors.helper.Helper;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class UpGCRF {

	public static String train(String matlabPath, double[][] s, double[][] y,
			double[][] x, int noTime, int training, int test, int maxIter,
			int noOfNodes, int lag, int noX, boolean useX, ProgressBar frame,
			String modelFolder, long proxyTime) {
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
				.setHidden(true).setProxyTimeout(proxyTime)
				.setMatlabLocation(matlabPath).build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		MatlabProxy proxy;
		try {
			proxy = factory.getProxy();

			String path =  Reader.jarFile() + "/matlab/upGCRF";
			proxy.eval("addpath('" + path + "')");

			MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
			processor.setNumericArray("S", new MatlabNumericArray(s, null));
			double[][][] x3d = null;
			if (useX) {
				x3d = Helper.get3DArray(x, noTime, noOfNodes, noX);
			} else {
				x3d = new double[noTime][noOfNodes][noX];
				;
			}
			processor.setNumericArray("X", new MatlabNumericArray(x3d, null));
			y = Helper.putNaN(y);
			processor.setNumericArray("y", new MatlabNumericArray(y, null));

			proxy.setVariable("lag", lag);
			proxy.setVariable("trainTs", training);
			proxy.setVariable("predictTs", test);
			proxy.setVariable("maxiter", maxIter);
			if (!useX) {
				proxy.eval("select_features = [];");
			} else {
				String features = "[";
				for (int i = 1; i <= noX; i++) {
					features += i + ",";
				}
				features += "];";
				proxy.eval("select_features = " + features);
			}
			proxy.setVariable("N", noOfNodes);
			proxy.eval("similarities{1} = S");

			// rain data test
			// proxy.eval("load rain_data_northwest.mat");
			// proxy.setVariable("lag", 12);
			// proxy.setVariable("trainTs", 20);
			// proxy.setVariable("predictTs", 20);
			// proxy.setVariable("maxiter", 20);
			// proxy.eval("select_features = [];");

			// run train

			String message = null;
			try {
				proxy.eval("[Data,muNoisyGCRF] = upGCRF(lag,trainTs,predictTs,maxiter,select_features,N, X, y, similarities);");

				proxy.eval("rmpath('" + path + "')");

				MatlabNumericArray array = processor
						.getNumericArray("muNoisyGCRF");
				double[][] outputs = array.getRealArray2D();

				MatlabNumericArray array1 = processor.getNumericArray("y");
				double[][] y1 = array1.getRealArray2D();

				// GraphGenerator.showMatrix(outputs);

				Writer.createFolder(modelFolder + "/parameters");
				String fileName =  modelFolder
						+ "/parameters/upGCRF.mat";
				proxy.setVariable("fileName", fileName);

				proxy.eval("save(fileName,'Data','-v7.3')");
				String export = exportResults(y1, outputs, modelFolder);
				message = "up-GCRF results: " + export;
				proxy.disconnect();

			} catch (Exception e) {
				// e.printStackTrace();
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
				return e.getMessage()
						+ ". Increase proxy timeout in Settings->Configuration.";
			}
		} catch (MatlabInvocationException e) {
			// e.printStackTrace();
		}
		frame.setVisible(false);
		return "Connection with MATLAB cannot be established.";
	}

	private static String exportResults(double[][] y, double[][] outputs,
			String folder) {
		Writer.createFolder(folder + "/test");
		String fileName = folder + "/test/results";
		int cols = outputs[0].length;
		double[] array = null;
		double[] arrayY = null;
		// double sum = 0;
		// double r2 = 0;
		// DecimalFormat df = new DecimalFormat("#.####");
		for (int i = 0; i < cols; i++) {
			array = new double[outputs.length];
			arrayY = new double[outputs.length];
			for (int j = 0; j < array.length; j++) {
				array[j] = outputs[j][i];
				arrayY[j] = y[j][i];
			}

			// r2 = BasicCalcs.rSquaredWitNaN(array, arrayY);
			// sum += r2;
			String[] text = exportTxt(0, array);
			Writer.write(text, fileName + "T" + (i + 1) + ".txt");
		}
		// double avg = sum / cols;
		// "\n* Average R^2 value: " + df.format(avg)
		return "\n* Results are successfully exported. \n* File location: "
				+ folder + "/test";
	}

	private static String[] exportTxt(double r2, double[] outputs) {
		// DecimalFormat df = new DecimalFormat("#.######");
		String[] txt = new String[outputs.length + 1];

		for (int i = 0; i < outputs.length; i++) {
			txt[i] = outputs[i] + "";
		}
		txt[outputs.length] = "";
		// txt[outputs.length] = "R^2 up-GCRF: " + df.format(r2);

		return txt;
	}
}
