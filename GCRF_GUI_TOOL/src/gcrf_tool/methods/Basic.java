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

	public double[] predictOutputsForTrain() {
		return calcs.mu(alpha, beta);
	}

	public double rSquaredForTrain() {
		return BasicCalcs.rSquared(predictOutputsForTrain(), expectedY);
	}

	public double rSquaredForTest(double[] predictedY, double[] expectedY) {
		return BasicCalcs.rSquared(predictedY, expectedY);
	}
}
