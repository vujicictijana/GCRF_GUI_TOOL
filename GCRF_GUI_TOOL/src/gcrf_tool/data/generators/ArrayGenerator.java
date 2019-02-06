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

package gcrf_tool.data.generators;

import java.util.Random;

public class ArrayGenerator {

	public static double[] generateArray(int noOfNodes,int maximum) {
		double[] r = new double[noOfNodes];
		Random rand = new Random();
		for (int i = 0; i < r.length; i++) {
			r[i] = rand.nextInt(maximum) + Math.random();
		}
		return r;
	}
}
