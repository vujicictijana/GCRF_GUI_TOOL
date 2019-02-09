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

package gcrf_tool.methods.matlab;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.MainFrame;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class UmGCRF {

	public static String train(String matlabPath, double[][] s, double[] y, double[] r,
			String modelFolder, long proxyTime,double[][] sTest, double[] rTest,
			double[] yTest) {
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder().setHidden(true)
				.setProxyTimeout(proxyTime).setMatlabLocation(matlabPath).build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		MatlabProxy proxy;
		try {
			proxy = factory.getProxy();
			String path = Reader.jarFile() + "/matlab/UMGCRF";
			proxy.eval("addpath('" + path + "')");

			MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
			processor.setNumericArray("S", new MatlabNumericArray(s, null));

			proxy.setVariable("Rtrain", r);
			proxy.eval("Rtrain = transpose(Rtrain)");

			proxy.setVariable("Ytrain", y);
			proxy.eval("Ytrain = transpose(Ytrain)");

			// run train

			String message = null;
			double theta  = 0;
			try {
				proxy.eval("[theta, MSE_train_UMGCRF, mu] = UMtrain(Ytrain,S,Rtrain);");

				proxy.eval("rmpath('" + path + "')");

				 theta = ((double[]) proxy.getVariable("theta"))[0];
				double[] output = ((double[]) proxy.getVariable("mu"));

				Writer.createFolder(modelFolder + "/parameters");
				String fileName = modelFolder + "/parameters/UmGCRF.txt";
				String[] text = { "Theta=" + theta };
				Writer.write(text, fileName);

	
				double r2 = BasicCalcs.rSquared(output, y);
				DecimalFormat df = new DecimalFormat("#.####");
				message = "Testing with same data:\n* R^2 value for standard UmGCRF is: " + df.format(r2);
				//test
		
				
				message += "OUTPUT:" + test(proxy, sTest, yTest, rTest, theta);
				// close matlab

				proxy.disconnect();

			} catch (Exception e) {
				message = "An internal MATLAB exception occurred. Please check your data.";
			}

		

			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("taskkill /F /IM MATLAB.exe");
			} catch (IOException e) {
				// e.printStackTrace();
			}
			return message;
		} catch (MatlabConnectionException e) {
			if (e.getMessage().contains("milliseconds")) {
				return e.getMessage() + ". Increase proxy timeout in Settings->Configuration.";
			}
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
		return "Connection with MATLAB cannot be established.";
	}

	public static String test(MatlabProxy proxy, double[][] s, double[] y, double[] r, double theta) {

		URL location = MainFrame.class.getProtectionDomain().getCodeSource().getLocation();
		String path = location.getFile();
		path = path.substring(1, path.lastIndexOf("/"));
		path = path.substring(0, path.lastIndexOf("/")) + "/matlab/UMGCRF";
		try {
			proxy.eval("addpath('" + path + "')");
			MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
			processor.setNumericArray("S", new MatlabNumericArray(s, null));

			proxy.setVariable("Rtest", r);
			proxy.eval("Rtest = transpose(Rtest)");

			proxy.setVariable("Ytest", y);
			proxy.eval("Ytest = transpose(Ytest)");

			proxy.setVariable("theta", theta);
			// run train
			try {

				proxy.eval("[mu] = UMtest(Ytest,S,Rtest,theta);");

				proxy.eval("rmpath('" + path + "')");

				double[] output = ((double[]) proxy.getVariable("mu"));
				String res = "";
				for (int i = 0; i < output.length; i++) {
					res += output[i] + "/";
				}
				proxy.disconnect();

				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec("taskkill /F /IM MATLAB.exe");
				} catch (IOException e) {
					// e.printStackTrace();
				}
				return res;
			} catch (Exception e) {
				// e.printStackTrace();
			}

			// close matlab
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("taskkill /F /IM MATLAB.exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		} catch (MatlabInvocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

}
