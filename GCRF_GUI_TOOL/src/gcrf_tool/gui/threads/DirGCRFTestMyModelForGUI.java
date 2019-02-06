/** This file is part of GCRF GUI TOOL.

    GCRF GUI TOOL is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GCRF GUI TOOL is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GCRF GUI TOOL.  If not, see <https://www.gnu.org/licenses/>.*/

package gcrf_tool.gui.threads;

import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gcrf_tool.data.datasets.Dataset;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.style.Style;
import gcrf_tool.methods.DirGCRF;
import gcrf_tool.methods.GCRF;

public class DirGCRFTestMyModelForGUI extends Thread {
	private JFrame mainFrame;
	private JPanel panel;
	private String modelFolder;
	private double[][] s;
	private double[] r;
	private double[] y;
	public double[] outputs;
	public double[] outputsS;
	DecimalFormat df = new DecimalFormat("#.######");

	public DirGCRFTestMyModelForGUI(JFrame mainFrame, JPanel panel,
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
		double[] param = read(modelFolder + "/parameters/DirGCRF.txt");
		double result = resultAsymmetric(param[0], param[1]);
		// System.out.println("A " + param[0] + " " + param[1]);
		double[] paramS = read(modelFolder + "/parameters/GCRF.txt");
		double resultS = -1;

		if (paramS != null) {
			resultS = resultSymmetric(paramS[0], paramS[1]);
			// System.out.println("S " + paramS[0] + " " + paramS[1]);
			if (panel != null) {
				createTable(result, resultS);
			}
		} else {
			if (panel != null) {
				createTable(result, -1);
			}
		}

		if (panel == null) {
			exportResults(result, resultS, "predict");
		} else {
			exportResults(result, resultS, "test");
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

	public double resultAsymmetric(double alpha, double beta) {
		Dataset d = new Dataset(s, r, y);
		DirGCRF alg = new DirGCRF(alpha, beta, d);
		// System.out.println(alg.rSquared());
		outputs = alg.predictOutputs();
		return alg.rSquared();
	}

	public double resultSymmetric(double alpha, double beta) {
		Dataset d = new Dataset(s, r, y);
		GCRF alg = new GCRF(alpha, beta, d);
		outputsS = alg.predictOutputs();
		return alg.rSquared();
	}

	public JTable createTable(double result, double resultS) {
		JTable table = null;
		if (resultS != -1) {
			String[] columnNames = { "R^2 DirGCRF", "R^2 GCRF" };
			Object[][] data = fillData(result, resultS);
			table = new JTable(data, columnNames);
		} else {
			String[] columnNames = { "R^2 DirGCRF" };
			Object[][] data = fillData(result, resultS);
			table = new JTable(data, columnNames);
		}
		table.setBackground(new Color(240, 240, 240));
		JScrollPane scrollPane = new JScrollPane(table);
		Style.resultTable(table, -1);
		panel.add(scrollPane);
		scrollPane.setBounds(10, 10, 700, 200);
		return table;
	}

	public String[] exportTxt(double[] array, double result, String method,
			String type) {
		String[] txt = new String[array.length + 1];

		for (int i = 0; i < array.length; i++) {
			txt[i] = array[i] + "";
		}
		if (type.equalsIgnoreCase("test")) {
			txt[outputs.length] = "R^2 " + method + ": " + df.format(result);
		} else {
			txt[outputs.length] = "";
		}
		return txt;
	}

	private void exportResults(double result, double resultS, String folder) {
		Writer.createFolder(modelFolder + "/" + folder);
		String fileName = modelFolder + "/" + folder + "/resultsDirGCRF.txt";
		String[] text = exportTxt(outputs, result, "DirGCRF", folder);
		Writer.write(text, fileName);
		if (resultS != -1) {
			String fileName1 = modelFolder + "/" + folder + "/resultsGCRF.txt";
			String[] text1 = exportTxt(outputsS, resultS, "GCRF", folder);
			Writer.write(text1, fileName1);
		}
		JOptionPane.showMessageDialog(mainFrame,
				"Export successfully completed.\nFile location: " + modelFolder
						+ "/" + folder + ".");
	}

	public Object[][] fillData(double result, double resultS) {
		Object[][] data;
		if (resultS != -1) {
			data = new Object[1][2];
			data[0][0] = df.format(result);
			data[0][1] = df.format(resultS);

		} else {
			data = new Object[1][1];
			data[0][0] = df.format(result);
		}
		return data;
	}
}
