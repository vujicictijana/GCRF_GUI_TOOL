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

public class TemporalData {

	private double[][] xTrain;
	private double[] yTrain;
	private double[][] xTest;
	private double[] yTest;

	public TemporalData(double[][] xTrain, double[] yTrain, double[][] xTest,
			double[] yTest) {
		super();
		this.xTrain = xTrain;
		this.yTrain = yTrain;
		this.xTest = xTest;
		this.yTest = yTest;
	}

	public double[][] getxTrain() {
		return xTrain;
	}

	public void setxTrain(double[][] xTrain) {
		this.xTrain = xTrain;
	}

	public double[] getyTrain() {
		return yTrain;
	}

	public void setyTrain(double[] yTrain) {
		this.yTrain = yTrain;
	}

	public double[][] getxTest() {
		return xTest;
	}

	public void setxTest(double[][] xTest) {
		this.xTest = xTest;
	}

	public double[] getyTest() {
		return yTest;
	}

	public void setyTest(double[] yTest) {
		this.yTest = yTest;
	}

}
