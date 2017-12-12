package gcrf_tool.methods;

import java.text.DecimalFormat;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.calculations.Calculations;
import gcrf_tool.calculations.CalculationsGCRF;

public class AlgorithmSymmetric {

	private double alpha;
	private double beta;
	private Calculations calcs;
	private double[] expectedY;
	DecimalFormat df = new DecimalFormat("#.##");

	public AlgorithmSymmetric(double alpha, double beta, double[][] s, double[] r,
			double[] expectedY) {
		super();
		this.alpha = alpha;
		this.beta = beta;
		this.expectedY = expectedY;
		this.calcs = new CalculationsGCRF(s, r);
	}

	public double[] outputs() {
		// double[] mu = new double[testGraph.getGames().size()];
		return calcs.mu(alpha, beta);
	}

	public double rSquared() {
		return BasicCalcs.rSquared(outputs(), expectedY);
	}
}
