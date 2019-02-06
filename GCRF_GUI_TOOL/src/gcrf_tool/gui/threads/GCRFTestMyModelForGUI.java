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

import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gcrf_tool.data.datasets.Dataset;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.methods.GCRF;

public class GCRFTestMyModelForGUI extends Thread {
	private JFrame mainFrame;
	private String modelFolder;
	private double[][] s;
	private double[] r;
	private double[] y;
	public double[] outputs;
	DecimalFormat df = new DecimalFormat("#.######");

	public GCRFTestMyModelForGUI(JFrame mainFrame, String modelFolder, double[][] s, double[] r, double[] y) {
		super();
		this.mainFrame = mainFrame;
		this.modelFolder = modelFolder;
		this.s = s;
		this.r = r;
		this.y = y;
	}

	public void run() {
		mainFrame.setEnabled(false);
		double[] paramS = read(modelFolder + "/parameters/GCRF.txt");

		double resultS = resultSymmetric(paramS[0], paramS[1]);

		exportResults(resultS, "test");

		mainFrame.setEnabled(true);
	}

	public double[] read(String file) {
		String[] txt = Reader.read(file);
		if (txt != null) {
			double[] params = new double[txt.length];
			for (int i = 0; i < txt.length; i++) {
				params[i] = Double.parseDouble(txt[i].substring(txt[i].indexOf("=") + 1));
			}
			return params;
		}
		return null;
	}

	public double resultSymmetric(double alpha, double beta) {
		Dataset d = new Dataset(s, r, y);
		GCRF alg = new GCRF(alpha, beta, d);
		outputs = alg.predictOutputs();
		return alg.rSquared();
	}

	// public JTable createTable(double resultS) {
	// JTable table = null;
	// String[] columnNames = { "R^2 GCRF" };
	// Object[][] data = new Object[1][1];
	// data[0][0] = df.format(resultS);
	// table = new JTable(data, columnNames);
	//
	// table.setBackground(new Color(240, 240, 240));
	// JScrollPane scrollPane = new JScrollPane(table);
	// Style.resultTable(table, -1);
	// panel.add(scrollPane);
	// scrollPane.setBounds(10, 10, 700, 200);
	// return table;
	// }

	private void exportResults(double resultS, String folder) {
		Writer.createFolder(modelFolder + "/" + folder);
		String fileName = modelFolder + "/" + folder + "/results.txt";
		String[] text = exportTxt(resultS, folder);
		Writer.write(text, fileName);
		JOptionPane.showMessageDialog(mainFrame,
				"R^2 GCRF: " + df.format(resultS) + "\nExport successfully completed. \nFile location: " + modelFolder + "/" + folder + ".");
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
