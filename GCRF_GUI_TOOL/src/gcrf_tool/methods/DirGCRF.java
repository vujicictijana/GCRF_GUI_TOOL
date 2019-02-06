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

import gcrf_tool.calculations.CalculationsDirGCRF;
import gcrf_tool.data.datasets.Dataset;
import gcrf_tool.learning.GradientAscent;
import gcrf_tool.learning.Parameters;


public class DirGCRF extends Basic {

	private GradientAscent learning;

	/**
	 * Class constructor specifying parameters for Gradient descent learning
	 * algorithm and data for DirGCRF.
	 * 
	 */
	public DirGCRF(Parameters parameters, Dataset data) {
		super();
		this.expectedY = data.getY();
		this.calcs = new CalculationsDirGCRF(data.getS(), data.getR());
		this.learning = new GradientAscent(parameters, calcs, expectedY);
		double[] params = learning.learn();
		this.alpha = params[0];
		this.beta = params[1];
	}

	/**
	 * Class constructor specifying alpha and beta parameters
	 *  and data for DirGCRF (if model is already trained).
	 * 
	 */
	public DirGCRF(double alpha, double beta, Dataset data) {
		this.expectedY = data.getY();
		this.calcs = new CalculationsDirGCRF(data.getS(), data.getR());
		double[] params = {alpha, beta};
		this.alpha = params[0];
		this.beta = params[1];
	}
	
	public double[] predictOutputs(double[][] testS, double[] testR) {
		CalculationsDirGCRF calc = new CalculationsDirGCRF(testS,testR);
		return calc.mu(alpha, beta);
	}

}
