package gcrf_tool.tool.predictors.linearregression;

import gcrf_tool.file.Writer;
import gcrf_tool.predictors.helper.Helper;

public class MyLR {

	public static double learn(double[][] x, double[] y, String folder) {

		if (x[0].length == 1) {
			double[] xOne = new double[x.length];
			for (int i = 0; i < xOne.length; i++) {
				xOne[i] = x[i][0];
			}
			LinearRegression lr = new LinearRegression(xOne, y);
			return LinearRegression.test(y, xOne, folder, lr,false);
		} else {
			try {
				MultivariateLinearRegression m = new MultivariateLinearRegression(x, y);
				return m.test(y, x, folder, false);
			} catch (Exception e) {

				return -3000;
			}
		}
	}
}
