package gcrf_tool.gui.threads;

import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.style.Style;
import gcrf_tool.methods.AlgorithmSymmetric;

public class GCRFTestMyModelForGUI extends Thread {
	private JFrame mainFrame;
	private JPanel panel;
	private String modelFolder;
	private double[][] s;
	private double[] r;
	private double[] y;
	public double[] outputs;
	DecimalFormat df = new DecimalFormat("#.######");

	public GCRFTestMyModelForGUI(JFrame mainFrame, JPanel panel,
			String modelFolder, double[][] s, double[] r, double[] y) {
		super();
		this.mainFrame = mainFrame;
		this.panel = panel;
		this.modelFolder = modelFolder;
		this.s = s;
		this.r = r;
		this.y = y;
		if (panel != null) {
			panel.removeAll();
			panel.revalidate();
			panel.repaint();
		}
	}

	public void run() {
		mainFrame.setEnabled(false);
		double[] paramS = read(modelFolder + "/parameters/GCRF.txt");

		double resultS = resultSymmetric(paramS[0], paramS[1]);
		if (panel != null) {
			createTable(resultS);
			exportResults(resultS, "test");
		} else {
			exportResults(resultS, "predict");
		}

		mainFrame.setEnabled(true);
	}

	public double[] read(String file) {
		String[] txt = Reader.read(file);
		if (txt != null) {
			double[] params = new double[txt.length];
			for (int i = 0; i < txt.length; i++) {
				params[i] = Double.parseDouble(txt[i].substring(txt[i]
						.indexOf("=") + 1));
			}
			return params;
		}
		return null;
	}

	public double resultSymmetric(double alpha, double beta) {
		AlgorithmSymmetric alg = new AlgorithmSymmetric(alpha, beta, s, r, y);
		outputs = alg.outputs();
		return alg.rSquared();
	}

	public JTable createTable(double resultS) {
		JTable table = null;
		String[] columnNames = { "R^2 GCRF" };
		Object[][] data = new Object[1][1];
		data[0][0] = df.format(resultS);
		table = new JTable(data, columnNames);

		table.setBackground(new Color(240, 240, 240));
		JScrollPane scrollPane = new JScrollPane(table);
		Style.resultTable(table, -1);
		panel.add(scrollPane);
		scrollPane.setBounds(10, 10, 700, 200);
		return table;
	}

	private void exportResults(double resultS, String folder) {
		Writer.createFolder(modelFolder + "/" + folder);
		String fileName = modelFolder + "/" + folder + "/results.txt";
		String[] text = exportTxt(resultS, folder);
		Writer.write(text, fileName);
		JOptionPane.showMessageDialog(mainFrame,
				"Export successfully completed. \nFile location: "
						+ modelFolder + "/" + folder + ".");
	}

	public String[] exportTxt(double resultS, String type) {
		String[] txt = new String[outputs.length + 1];

		for (int i = 0; i < outputs.length; i++) {
			txt[i] = outputs[i] + "";
		}

		if (type.equalsIgnoreCase("test")) {
			txt[outputs.length] = "R^2 GCRF: " + df.format(resultS);
		} else {
			txt[outputs.length] = "";
		}
		return txt;
	}

}
