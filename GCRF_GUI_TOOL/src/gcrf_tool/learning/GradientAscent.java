package gcrf_tool.learning;

import javax.swing.JProgressBar;

import gcrf_tool.calculations.Calculations;

public class GradientAscent implements LearningAlgorithm {
	private double alpha;
	private double beta;
	private double lr;
	Calculations calcs;
	private double[] y;
	int maxIter;
	boolean showProgress;
	JProgressBar progress;

	/**
	 * Class constructor specifying: parameters for learning algorithm (object
	 * of Parameters class), calculation rules (object of class that implements
	 * Calculations interface), array of y (the output variable) values, and
	 * whether progress will be shown in the console (showProgress=true) or in
	 * the specific JProgressBar (pass JProgressBar object or null).
	 */
	public GradientAscent(Parameters parameters, Calculations calcs, double[] y) {
		super();
		this.alpha = parameters.getFirstAlpha();
		this.beta = parameters.getFirstBeta();
		this.lr = parameters.getLearningRate();
		this.maxIter = parameters.getMaxIterations();
		this.calcs = calcs;
		this.y = y;
		this.showProgress = parameters.isShowProgress();
		this.progress = parameters.getProgressBar();
	}

	public double[] learn() {
		int tempIter = 0;
		double little = 1E-10;
		double tempAlpha = 0;
		double tempBeta = 0;
		double difAlpha = 1;
		double difBeta = 1;

		while (difAlpha > little || difBeta > little) {
			// while (tempIter < iterNum) {
			if (tempIter % 50 == 0) {
				if (progress != null) {
					progress.setValue(tempIter);
				}
				if (showProgress) {
					System.out.println("Iter: " + tempIter);
				}
			}
			if (tempIter == maxIter) {
				break;
			}
			tempAlpha = alpha + lr * calcs.dervativeAlpha(alpha, beta, y);
			tempBeta = beta + lr * calcs.dervativeBeta(alpha, beta, y);

			difAlpha = Math.abs(alpha - tempAlpha);
			difBeta = Math.abs(beta - tempBeta);
			alpha = tempAlpha;
			beta = tempBeta;
			tempIter++;
		}
		if (showProgress) {
			System.out.println("Iteration: " + tempIter);
			System.out.println("Alpha: " + alpha);
			System.out.println("Beta: " + beta);
		}
		double[] params = new double[2];
		params[0] = alpha;
		params[1] = beta;
		return params;
	}

}