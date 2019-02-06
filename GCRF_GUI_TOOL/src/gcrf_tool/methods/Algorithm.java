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

package gcrf_tool.methods;

public interface Algorithm {
	/**
	 * Returns vector of predicted outputs.
	 */
	public double[] predictOutputs();

	/**
	 * Returns the coefficient of determination (R^2).
	 */
	public double rSquared();

	/**
	 * Returns the coefficient of determination (R^2) for test data.
	 */
	public double rSquaredForTest(double[] predictedY, double[] expectedY);
	
	
	/**
	 * Returns alpha and beta parameters.
	 */
	double[] getParameters();

}
