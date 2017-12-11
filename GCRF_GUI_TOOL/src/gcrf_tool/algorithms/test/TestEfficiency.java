package gcrf_tool.algorithms.test;

import org.ojalgo.array.ArrayUtils;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;

import gcrf_tool.algorithms.asymmetric.CalculationsAsymmetric;
import gcrf_tool.algorithms.asymmetric.GradientDescentAsymmetric;
import gcrf_tool.data.generators.ArrayGenerator;
import gcrf_tool.data.generators.GraphGenerator;

public class TestEfficiency {

	// public void jama(int no) {
	// long start = System.currentTimeMillis();
	// double[][] matrix = GraphGenerator.generateDirectedGraph(no);
	// // double[][] matrix = GraphGenerator
	// // .generateDirectedGraphWithEdgeProbability(no, 0.05);
	// Matrix m = new Matrix(matrix);
	// double[][] m1 = m.inverse().getArray();
	//
	// long elapsedTime = System.currentTimeMillis() - start;
	// System.out.println("Inverse: " + elapsedTime);
	// start = System.currentTimeMillis();
	// m.times(m);
	// elapsedTime = System.currentTimeMillis() - start;
	// System.out.println("M^2: " + elapsedTime);
	// }

	public void universal(int no) {
		long start = System.currentTimeMillis();
		// double[][] matrix = GraphGenerator.generateDirectedGraph(no);
		double[][] matrix = GraphGenerator
				.generateDirectedGraphWithEdgeProbability(no, 0.05);
		org.ujmp.core.Matrix m = org.ujmp.core.Matrix.Factory
				.linkToArray(matrix);

		double[][] m1 = m.inv().toDoubleArray();

		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("Inverse: " + elapsedTime);

		start = System.currentTimeMillis();
		m.times(m);
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println("M^2: " + elapsedTime);
	}

	// public void la4j(int no) {
	//
	// long start = System.currentTimeMillis();
	// // double[][] matrix = GraphGenerator.generateDirectedGraph(no);
	// double[][] matrix = GraphGenerator
	// .generateDirectedGraphWithEdgeProbability(no, 0.05);
	// org.la4j.matrix.Matrix m = new Basic2DMatrix(matrix);
	//
	// // org.la4j.matrix.Matrix m1 = (Basic2DMatrix) m
	// // .inverse(Matrices.DEFAULT_INVERTOR);
	//
	// //
	// long elapsedTime = System.currentTimeMillis() - start;
	// System.out.println("Inverse: " + elapsedTime);
	//
	// start = System.currentTimeMillis();
	// m.multiply(m);
	// elapsedTime = System.currentTimeMillis() - start;
	// System.out.println("M^2: " + elapsedTime);
	// }

	public void ojAlgo(int no) {
		long start = System.currentTimeMillis();
		 double[][] matrix = GraphGenerator.generateDirectedGraph(no);
//		double[][] matrix = GraphGenerator
//				.generateDirectedGraphWithEdgeProbability(no, 0.05);
		BasicMatrix.Factory<PrimitiveMatrix> mtrxFactory = PrimitiveMatrix.FACTORY;
		PrimitiveMatrix mtrxA = mtrxFactory.rows(matrix);
		PrimitiveMatrix mtrxI = mtrxA.invert();
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("Inverse: " + elapsedTime);
		start = System.currentTimeMillis();
		mtrxA.multiply(mtrxA);
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println("M^2: " + elapsedTime);
	}

	public void learn(int no, int iter) {

		// double[][] s = GraphGenerator.generateDirectedGraph(no);
		double[][] s = GraphGenerator.generateDirectedGraphWithEdgeProbability(
				no, 0.05);
		org.ujmp.core.Matrix m = org.ujmp.core.Matrix.Factory.linkToArray(s);

		double[] r = ArrayGenerator.generateArray(no, 5);
		CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);
		double[] y = c.y(5, 1, 0.05);

		GradientDescentAsymmetric gda = new GradientDescentAsymmetric(1, 1,
				0.01, s, r, y);
		long start = System.currentTimeMillis();
		gda.learn(iter, false, null);
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("Universal + Ojalgo: " + elapsedTime);
	}

}
