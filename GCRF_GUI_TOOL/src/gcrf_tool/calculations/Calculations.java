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

package gcrf_tool.calculations;

public interface Calculations {

	/**
	 * Calculates Laplacian matrix.
	 */
	public double[][] l();

	/**
	 * Multiplies Alpha parameter by I (identity matrix).
	 */
	public double[][] alphaI(double alpha);

	/**
	 * Multiplies Beta parameter by L (Laplacian matrix).
	 */
	public double[][] betaL(double beta);

	/**
	 * Calculates Q (arbitrary matrix) for GNF.
	 */
	public double[][] q(double alpha, double beta);

	/**
	 * Calculates b vector for GNF.
	 */
	public double[] b(double alpha);

	/**
	 * Calculates mu (the optimal prediction).
	 */

	public double[] mu(double alpha, double beta);

	/**
	 * Generates y (the output variable) based on algorithm rules, with some
	 * added random noise.
	 */
	public double[] y(double alpha, double beta, double p);

	/**
	 * Calculates partial derivative with respect to the Alpha parameter.
	 */
	public double dervativeAlpha(double alpha, double beta, double[] y);

	/**
	 * Calculates partial derivative with respect to the Beta parameter.
	 */
	public double dervativeBeta(double alpha, double beta, double[] y);
}
