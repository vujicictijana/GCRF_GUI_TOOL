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
