package gcrf_tool.methods.matlab;

import java.io.IOException;
import java.net.URL;

import gcrf_tool.gui.frames.MainFrame;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

public class Test {

	public static void main(String[] args) {
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
				.setHidden(true)
				.setProxyTimeout(30000L)
				.setMatlabLocation(
						"C:/Program Files/MATLAB/R2016a/bin/matlab.exe")
				.build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		MatlabProxy proxy;
		try {
			proxy = factory.getProxy();
			// proxy.eval(new String("x=25;"));
			// proxy.eval(new String("sqrt(x)"));
			// proxy.setVariable("a", 5);
			// proxy.eval("a = a + 6");
			// = ((double[]) proxy.getVariable("a"))[0];
			// System.out.println("Result: " + result);

			URL location = MainFrame.class.getProtectionDomain()
					.getCodeSource().getLocation();
			String path = location.getFile();
			path = path.substring(1, path.lastIndexOf("/"));
			path = path.substring(0, path.lastIndexOf("/")) + "/matlab";

			proxy.eval("addpath('" + path + "')");

			Object[] o = new Object[2];
			o[0] = 1;
			o[1] = 2;
			Object[] returnArguments = proxy.returningFeval("test", 1, o);

			double result = ((double[]) returnArguments[0])[0];
			System.out.println(result);
			proxy.eval("rmpath('" + path + "')");
			proxy.disconnect();
			
			// close matlab
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("taskkill /F /IM MATLAB.exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MatlabConnectionException e) {
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
	}
}
