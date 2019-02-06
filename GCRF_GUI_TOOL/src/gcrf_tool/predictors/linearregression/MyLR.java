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

package gcrf_tool.predictors.linearregression;
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
				MultipleLinearRegression m = new MultipleLinearRegression(x, y);
				return m.test(y, x, folder, false);
			} catch (Exception e) {
				return -3000;
			}
		}
	}
}
