package gcrf_tool.methods;

import java.text.DecimalFormat;

import gcrf_tool.calculations.BasicCalcs;


public class AlgorithmAsymmetric {

	private double alpha;
	private double beta;
	private double[] expectedY;
	private CalculationsAsymmetric calcs;
	DecimalFormat df = new DecimalFormat("#.##");

	public AlgorithmAsymmetric(double alpha, double beta, double[][] s,
			double[] r, double[] expectedY) {
		super();
		this.alpha = alpha;
		this.beta = beta;
		this.expectedY = expectedY;
		this.calcs = new CalculationsAsymmetric(s, r);
	}

	public double[] outputs() {
		return calcs.mu(alpha, beta);
	}

	public double rSquared() {
		return BasicCalcs.rSquared(outputs(), expectedY);
	}

}
