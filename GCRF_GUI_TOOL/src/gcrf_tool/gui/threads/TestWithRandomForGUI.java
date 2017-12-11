package gcrf_tool.gui.threads;

import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gcrf_tool.algorithms.asymmetric.AlgorithmAsymmetric;
import gcrf_tool.algorithms.asymmetric.CalculationsAsymmetric;
import gcrf_tool.algorithms.basic.BasicCalcs;
import gcrf_tool.algorithms.symmetric.AlgorithmSymmetric;
import gcrf_tool.algorithms.symmetric.CalculationsSymmetric;
import gcrf_tool.data.generators.ArrayGenerator;
import gcrf_tool.data.generators.GraphGenerator;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;

public class TestWithRandomForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;
	private JPanel panel;
	private String modelFolder;
	private int noOfNodes;
	private int times;
	private double probability;
	private double alphaGen;
	private double betaGen;

	public TestWithRandomForGUI(ProgressBar frame, JFrame mainFrame,
			JPanel panel, String modelFolder, int noOfNodes, int times,
			double probability, double alphaGen, double betaGen) {
		super();
		this.frame = frame;
		this.mainFrame = mainFrame;
		this.panel = panel;
		this.modelFolder = modelFolder;
		this.noOfNodes = noOfNodes;
		this.times = times;
		this.probability = probability;
		this.alphaGen = alphaGen;
		this.betaGen = betaGen;
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}

	public void run() {
		mainFrame.setEnabled(false);
		frame.setTitle("Progress");
		double[] param = read(modelFolder + "/DirGCRF.txt");
		double[] results = resultsAsymmetric(param[0], param[1]);
		double[] paramS = read(modelFolder + "/GCRF.txt");
		double[] resultsS = null;
		if (paramS != null) {
			resultsS = resultsSymmetric(paramS[0], paramS[1]);
			createTable(results, resultsS);
		} else {
			createTable(results, null);
		}
		exportResults(results, resultsS);
		mainFrame.setEnabled(true);
		frame.setVisible(false);
	}

	private void exportResults(double[] results, double[] resultsS) {
		Writer.createFolder(modelFolder + "/Test");
		String fileName = modelFolder + "/Test/TestWith" + noOfNodes + "Nodes"
				+ times + "times.txt";
		String[] a = exportTxt(results, resultsS);
		Writer.write(a, fileName);
		JOptionPane.showMessageDialog(mainFrame,
				"Export successfully completed. \nFile location: "
						+ modelFolder + "/Test.");
	}

	public double[] read(String file) {
		String[] txt = Reader.read(file);
		if (txt != null) {
			double[] params = new double[txt.length];
			for (int i = 0; i < txt.length; i++) {
				params[i] = Double.parseDouble(txt[i]);
			}
			return params;
		}
		return null;
	}

	public double[][] graph(int noOfNodes) {
		String[] folders =  modelFolder.split("/");
		String type = folders[folders.length-2];
		return GraphGenerator.generateGraphByType(noOfNodes, type, probability);
	}

	public double[] resultsAsymmetric(double alpha, double beta) {
		double[] results = new double[times];
		frame.setTitle("Progress DirGCRF");
		for (int i = 0; i < results.length; i++) {
			frame.getCurrent().setValue((i + 1));
			double[][] s = graph(noOfNodes);
			double[] r = ArrayGenerator.generateArray(noOfNodes, 5);
			CalculationsAsymmetric c = new CalculationsAsymmetric(s, r);

			double[] y = c.y(alphaGen, betaGen, 0.05);

			AlgorithmAsymmetric alg = new AlgorithmAsymmetric(alpha, beta, s,
					r, y);
			results[i] = alg.rSquared();
		}
		return results;
	}

	public double[] resultsSymmetric(double alpha, double beta) {
		double[] results = new double[times];
		frame.setTitle("Progress GCRF");
		frame.getCurrent().setValue(0);
		for (int i = 0; i < results.length; i++) {
			frame.getCurrent().setValue((i + 1));
			double[][] s = GraphGenerator
					.converteGraphToUndirected(graph(noOfNodes));
			double[] r = ArrayGenerator.generateArray(noOfNodes, 5);
			CalculationsSymmetric c = new CalculationsSymmetric(s, r);

			double[] y = c.y(alphaGen, betaGen, 0.05);

			AlgorithmSymmetric alg = new AlgorithmSymmetric(alpha, beta, s, r,
					y);
			results[i] = alg.rSquared();
		}
		return results;
	}

	public JTable createTable(double[] results, double[] resultsS) {
		JTable table = null;
		if (resultsS != null) {
			String[] columnNames = { "No.", "R^2 DirGCRF", "R^2 GCRF" };
			Object[][] data = fillData(results, resultsS);

			table = new JTable(data, columnNames);
		} else {
			String[] columnNames = { "No.", "R^2 DirGCRF" };
			Object[][] data = fillData(results, resultsS);

			table = new JTable(data, columnNames);
		}
		table.setBackground(new Color(240, 240, 240));
		JScrollPane scrollPane = new JScrollPane(table);
		Style.resultTable(table, times + 1);
		panel.add(scrollPane);
		scrollPane.setBounds(10, 10, 700, 200);
		return table;
	}

	public String[] exportTxt(double[] results, double[] resultsS) {
		String[] txt = null;
		if (resultsS != null) {
			txt = new String[(times * 2) + 4];
		} else {
			txt = new String[times + 2];
		}
		double sum = 0;
		double sumS = 0;
		int no = 0;
		for (int i = 0; i < results.length; i++) {
			txt[no] = (i + 1) + " R^2 DirGCRF: " + results[i];
			sum += results[i];
			no++;
			if (resultsS != null) {
				txt[no] = (i + 1) + " R^2 GCRF: " + resultsS[i];
				sumS += resultsS[i];
				no++;
			}
		}
		DecimalFormat df = new DecimalFormat("#.############");
		txt[txt.length - 1] = "Average DirGCRF: " + (sum / times);
		if (resultsS != null) {
			txt[txt.length - 4] = "Average DirGCRF: " + (sum / times);
			txt[txt.length - 3] = "Average GCRF: " + (sumS / times);
		}
		txt[txt.length - 2] = "Standard deviation DirGCRF: "
				+ df.format(BasicCalcs.standardDeviation(results));
		if (resultsS != null) {
			txt[txt.length - 2] = "Standard deviation DirGCRF: "
					+ df.format(BasicCalcs.standardDeviation(results));
			txt[txt.length - 1] = "Standard deviation GCRF: "
					+ df.format(BasicCalcs.standardDeviation(resultsS));
		}
		return txt;
	}

	public Object[][] fillData(double[] results, double[] resultsS) {
		Object[][] data;
		if (resultsS != null) {
			data = new Object[times + 2][3];
		} else {
			data = new Object[times + 2][2];
		}
		double sum = 0;
		double sumS = 0;
		for (int i = 0; i < times; i++) {
			data[i][0] = (i + 1);
			data[i][1] = results[i];
			sum += results[i];
			if (resultsS != null) {
				data[i][2] = resultsS[i];
				sumS += resultsS[i];
			}
		}
		data[times][0] = "Average";
		data[times][1] = sum / times;
		DecimalFormat df = new DecimalFormat("#.############");
		data[times + 1][0] = "Standard deviation";
		data[times + 1][1] = df.format(BasicCalcs.standardDeviation(results));
		if (resultsS != null) {
			data[times][2] = sumS / times;
			data[times + 1][2] = df.format(BasicCalcs
					.standardDeviation(resultsS));
		}
		return data;
	}
}
