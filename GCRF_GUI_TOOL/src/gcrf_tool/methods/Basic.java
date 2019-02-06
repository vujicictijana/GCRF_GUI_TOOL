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


import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.calculations.Calculations;
import gcrf_tool.data.datasets.Dataset;
import gcrf_tool.learning.LearningAlgorithm;

public class Basic implements Algorithm {
	protected double alpha;
	protected double beta;
	protected double[] expectedY;
	protected Calculations calcs;

	public Basic() {
		super();
	}

	/**
	 * Class constructor specifying learning algorithm (object of class that
	 * implements LearningAlgorithm interface), calculation rules (object of
	 * class that implements Calculations interface) and data (object of Dataset
	 * class).
	 * 
	 */
	public Basic(LearningAlgorithm l, Calculations calcs, Dataset data) {
		super();
		double[] array = l.learn();
		this.alpha = array[0];
		this.beta = array[1];
		this.expectedY = data.getY();
		this.calcs = calcs;
	}

	public double[] predictOutputs() {
		return calcs.mu(alpha, beta);
	}

	public double rSquared() {
		return BasicCalcs.rSquared(predictOutputs(), expectedY);
	}

	public double rSquaredForTest(double[] predictedY, double[] expectedY) {
		return BasicCalcs.rSquared(predictedY, expectedY);
	}
	
	public double[] getParameters(){
		return new double[] {alpha, beta};
	}
}
