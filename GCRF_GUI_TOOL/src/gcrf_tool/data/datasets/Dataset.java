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
package gcrf_tool.data.datasets;

public class Dataset {

	private double[][] s;
	private double[] r;
	private double[] y;

	/**
	 * Class constructor specifying required data: similarity matrix (S),
	 * outputs of unstructured predictor (R), and expected output (Y).
	 * 
	 */
	public Dataset(double[][] s, double[] r, double[] y) {
		super();
		this.s = s;
		this.r = r;
		this.y = y;
	}

	public double[][] getS() {
		return s;
	}

	public void setS(double[][] s) {
		this.s = s;
	}

	public double[] getR() {
		return r;
	}

	public void setR(double[] r) {
		this.r = r;
	}

	public double[] getY() {
		return y;
	}

	public void setY(double[] y) {
		this.y = y;
	}

}
