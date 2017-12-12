package gcrf_tool.methods;

import gcrf_tool.calculations.CalculationsDirGCRF;
import gcrf_tool.data.datasets.Dataset;
import gcrf_tool.learning.GradientAscent;
import gcrf_tool.learning.Parameters;

//test
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
		this.learning = new GradientAscent(parameters, calcs, expectedY, false, null);
		double[] params = learning.learn();
		this.alpha = params[0];
		this.beta = params[1];
	}

	public double[] predictOutputsForTest(double[][] testS, double[] testR) {
		CalculationsDirGCRF calc = new CalculationsDirGCRF(testS,testR);
		return calc.mu(alpha, beta);
	}
}
