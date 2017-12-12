package gcrf_tool.learning;

public class Parameters {

	private double firstAlpha;
	private double firstBeta;
	private int maxIterations;
	private double learningRate;

	/**
	 * Class constructor specifying parameters for learning algorithm: first
	 * value for Alpha and Beta parameters, maximal number of iterations, and
	 * learning rate.
	 */
	public Parameters(double firstAlpha, double firstBeta, int maxIterations, double learningRate) {
		super();
		this.firstAlpha = firstAlpha;
		this.firstBeta = firstBeta;
		this.maxIterations = maxIterations;
		this.learningRate = learningRate;
	}

	public double getFirstAlpha() {
		return firstAlpha;
	}

	public void setFirstAlpha(double firstAlpha) {
		this.firstAlpha = firstAlpha;
	}

	public double getFirstBeta() {
		return firstBeta;
	}

	public void setFirstBeta(double firstBeta) {
		this.firstBeta = firstBeta;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

}
