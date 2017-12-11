package gcrf_tool.tool.predictors.linearregression;

public class TemporalData {

	private double[][] xTrain;
	private double[] yTrain;
	private double[][] xTest;
	private double[] yTest;

	public TemporalData(double[][] xTrain, double[] yTrain, double[][] xTest,
			double[] yTest) {
		super();
		this.xTrain = xTrain;
		this.yTrain = yTrain;
		this.xTest = xTest;
		this.yTest = yTest;
	}

	public double[][] getxTrain() {
		return xTrain;
	}

	public void setxTrain(double[][] xTrain) {
		this.xTrain = xTrain;
	}

	public double[] getyTrain() {
		return yTrain;
	}

	public void setyTrain(double[] yTrain) {
		this.yTrain = yTrain;
	}

	public double[][] getxTest() {
		return xTest;
	}

	public void setxTest(double[][] xTest) {
		this.xTest = xTest;
	}

	public double[] getyTest() {
		return yTest;
	}

	public void setyTest(double[] yTest) {
		this.yTest = yTest;
	}

}
