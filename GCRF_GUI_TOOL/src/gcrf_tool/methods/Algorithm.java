package gcrf_tool.methods;

public interface Algorithm {
	/**
	 * Returns vector of predicted outputs.
	 */
	public double[] predictOutputsForTrain();

	/**
	 * Returns the coefficient of determination (R^2).
	 */
	public double rSquaredForTrain();

	/**
	 * Returns the coefficient of determination (R^2) for test data.
	 */
	public double rSquaredForTest(double[] predictedY, double[] expectedY);

}
