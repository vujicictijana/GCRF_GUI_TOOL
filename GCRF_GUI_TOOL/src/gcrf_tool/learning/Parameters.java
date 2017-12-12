package gcrf_tool.learning;

import javax.swing.JProgressBar;

public class Parameters {

	private double firstAlpha;
	private double firstBeta;
	private int maxIterations;
	private double learningRate;
	private boolean showProgress;
	private JProgressBar progressBar;

	/**
	 * Class constructor specifying parameters for learning algorithm: first
	 * value for Alpha and Beta parameters, maximal number of iterations,
	 * learning rate, showProgress (value true or false - if 'true' progress will be shown in console) 
	 * and progressBar (object of JProgressBar class or null) .
	 */
	public Parameters(double firstAlpha, double firstBeta, int maxIterations, double learningRate, boolean showProgress,
			JProgressBar progressBar) {
		super();
		this.firstAlpha = firstAlpha;
		this.firstBeta = firstBeta;
		this.maxIterations = maxIterations;
		this.learningRate = learningRate;
		this.showProgress = showProgress;
		this.progressBar = progressBar;
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

	public boolean isShowProgress() {
		return showProgress;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

}
