package gcrf_tool.data.datasets;

public class Dataset {

	private double[][] s;
	private double[] r;
	private double[] y;

	/**
	 * Class constructor specifying required data: similarity matrix (S),
	 * outputs of unstructured predictor (R), and expected output (Y).
	 * 
	 */
	public Dataset(double[][] s, double[] r, double[] y) {
		super();
		this.s = s;
		this.r = r;
		this.y = y;
	}

	public double[][] getS() {
		return s;
	}

	public void setS(double[][] s) {
		this.s = s;
	}

	public double[] getR() {
		return r;
	}

	public void setR(double[] r) {
		this.r = r;
	}

	public double[] getY() {
		return y;
	}

	public void setY(double[] y) {
		this.y = y;
	}

}
