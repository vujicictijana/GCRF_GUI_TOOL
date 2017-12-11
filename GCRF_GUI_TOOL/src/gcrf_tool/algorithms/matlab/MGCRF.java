package gcrf_tool.algorithms.matlab;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import gcrf_tool.algorithms.basic.BasicCalcs;
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

public class MGCRF {

	public static String train(String matlabPath, double[][] s, double[][] y,
			double[][] r, int noTime, int training, int maxIter, int regAlpha,
			int regBeta, ProgressBar frame, String modelFolder, long proxyTime) {

		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
				.setHidden(true).setProxyTimeout(proxyTime)
				.setMatlabLocation(matlabPath).build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		MatlabProxy proxy;
		try {
			proxy = factory.getProxy();

			String path = Reader.jarFile() + "/matlab/mGCRF";
			proxy.eval("addpath('" + path + "')");

			MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
			processor.setNumericArray("similarity", new MatlabNumericArray(s,
					null));
			processor.setNumericArray("R", new MatlabNumericArray(r, null));
			y = Helper.putNaN(y);
			processor.setNumericArray("y", new MatlabNumericArray(y, null));

			proxy.setVariable("numTimeSteps", noTime);

			proxy.setVariable("training", training);
			proxy.setVariable("maxiter", maxIter);
			proxy.setVariable("regAlpha", regAlpha);
			proxy.setVariable("regBeta", regBeta);
			// proxy.setVariable("r", r);
			proxy.eval("RR{1} = R");

			// run train

			String message = null;
			try {
				proxy.eval("[ualpha, ubeta, Data,predictionCRF] = mGCRF(numTimeSteps,training,maxiter,regAlpha,regBeta,RR,y,similarity);");

				proxy.eval("rmpath('" + path + "')");

				double[] outputs = ((double[]) proxy
						.getVariable("predictionCRF"));

				double ualpha = ((double[]) proxy.getVariable("ualpha"))[0];
				double ubeta = ((double[]) proxy.getVariable("ubeta"))[0];

				Writer.createFolder(modelFolder + "/parameters");
				String fileName = modelFolder + "/parameters/mGCRF.mat";
				proxy.setVariable("fileName", fileName);

				proxy.eval("save(fileName,'Data','-v7.3')");
				double[] lastY = new double[s.length];
				for (int i = 0; i < lastY.length; i++) {
					lastY[i] = y[i][noTime - 1];
				}

				String fileName1 = modelFolder + "/parameters/mGCRF.txt";
				String[] text = { "Alpha=" + ualpha, "Beta=" + ubeta };
				Writer.write(text, fileName1);

				double r2 = BasicCalcs.rSquaredWitNaN(outputs, lastY);
				DecimalFormat df = new DecimalFormat("#.####");
				String export = exportResults(r2, outputs, modelFolder);
				message = "m-GCRF results: \n* R^2 value for test data: "
						+ df.format(r2) + export;
				proxy.disconnect();
				// proxy.exit();

			} catch (Exception e) {
				message = "An internal MATLAB exception occurred. Please check your data.";
			}

			// close matlab
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("taskkill /F /IM MATLAB.exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printFStackTrace();
			}
			frame.setVisible(false);
			return message;

		} catch (MatlabConnectionException e) {
			// e.printStackTrace();
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

	private static String exportResults(double r2, double[] outputs,
			String folder) {
		Writer.createFolder(folder + "/test");
		String fileName = folder + "/test/results.txt";
		String[] text = exportTxt(r2, outputs);
		Writer.write(text, fileName);
		return "\n* Results are successfully exported. \n* File location: "
				+ folder + "/test";
	}

	private static String[] exportTxt(double r2, double[] outputs) {
		DecimalFormat df = new DecimalFormat("#.######");
		String[] txt = new String[outputs.length + 1];

		for (int i = 0; i < outputs.length; i++) {
			txt[i] = outputs[i] + "";
		}

		txt[outputs.length] = "R^2 m-GCRF: " + df.format(r2);

		return txt;
	}
}
