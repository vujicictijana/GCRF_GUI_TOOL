
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gcrf_tool.methods.GCRF;
import gcrf_tool.data.datasets.Dataset;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.learning.Parameters;

public class GCRFTrainMyModelForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;

	private double s[][];
	private double r[];
	private double y[];
	private double alpha;
	private double beta;
	private double lr;
	private int maxIter;
	private String modelFolder;
	private String time;
	private Thread thisThread;
	DecimalFormat df = new DecimalFormat("#.##");

	public GCRFTrainMyModelForGUI(String modelFolder, ProgressBar frame,
			JFrame mainFrame, double[][] s, double[] r, double[] y,
			double alpha, double beta, double lr, int maxIter) {
		super();
		this.frame = frame;
		this.mainFrame = mainFrame;
		this.s = s;
		this.r = r;
		this.y = y;
		this.alpha = alpha;
		this.beta = beta;
		this.lr = lr;
		this.maxIter = maxIter;
		this.modelFolder = modelFolder;
		time = "Time in seconds: ";
		this.thisThread = this;
	}

	public void run() {
		mainFrame.setEnabled(false);
		frame.getCurrent().setValue(0);
		frame.setTitle("Progress standard GCRF");
		JButton cancel = new JButton();
		frame.add(cancel);
		cancel.setBounds(0, 0, 80, 30);
		cancel.setText("Cancel");
		Style.buttonStyle(cancel);
		cancel.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(true);
				frame.setVisible(false);
				Reader.deleteDir(new File(modelFolder));
				thisThread.stop();
				JOptionPane.showMessageDialog(frame,
						"Training process is canceled.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		try {
			long start = System.currentTimeMillis();
			Parameters p = new Parameters(alpha, beta, maxIter, lr, false, frame.getCurrent());
			Dataset d = new Dataset(s, r, y);	
			GCRF algS = new GCRF(p, d);
			double r2S = algS.rSquared();
			double[] resS = algS.getParameters();
			long elapsedTime = System.currentTimeMillis() - start;
			time += df.format((double) elapsedTime / 1000);

			createFile("GCRF.txt", resS);

			DecimalFormat df1 = new DecimalFormat("#.####");
			String message = "Testing with same data:\n* R^2 value for standard GCRF is: "
					+ df1.format(r2S);
			message += "\n" + time;
			JOptionPane.showMessageDialog(mainFrame, message, "Results",
					JOptionPane.INFORMATION_MESSAGE);
			mainFrame.setEnabled(true);
			frame.setVisible(false);
		} catch (Exception e) {

		}

	}

	public void createFile(String symmetric, double[] results) {
		Writer.createFolder(modelFolder + "/parameters");
		String fileName = modelFolder + "/parameters/" + symmetric;
		String[] resultsS = { "Alpha=" + results[0], "Beta=" + results[1] };
		Writer.write(resultsS, fileName);
	}

}
