package gcrf_tool.gui.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gcrf_tool.algorithms.matlab.RLSR;
import gcrf_tool.file.Reader;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;

public class RLSRTrainMyModelForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;

	private double x[][];
	private double y[][];
	private String modelFolder;
	private String time;
	private String matlabPath;
	private int noTime;
	private int training;
	private int maxIter;
	private int noOfNodes;
	private int validation;
	private int noX;
	private int lfSize;
	private String lambda;
	private int test;
	private int iterNN;
	private int hidden;
	private int iterSSE;
	private int iterLs;
	private long proxyTime;
	private boolean cancelTrain;
	DecimalFormat df = new DecimalFormat("#.##");

	public RLSRTrainMyModelForGUI(String matlabPath, String modelFolder,
			ProgressBar frame, JFrame mainFrame, double[][] r, double[][] y,
			int noTime, int training, int maxIter, int noOfNodes,
			int validation, int noX, int lfSize, String lambda, int test,
			int iterNN, int hidden, int iterSSE, int iterLs, long proxyTime) {
		super();
		this.frame = frame;
		this.mainFrame = mainFrame;
		this.x = r;
		this.y = y;
		this.modelFolder = modelFolder;
		this.matlabPath = matlabPath;
		this.noTime = noTime;
		this.training = training;
		this.maxIter = maxIter;
		this.noOfNodes = noOfNodes;
		this.validation = validation;
		this.noX = noX;
		this.lambda = lambda;
		this.lfSize = lfSize;
		this.test = test;
		this.iterNN = iterNN;
		this.hidden = hidden;
		this.iterSSE = iterSSE;
		this.iterLs = iterLs;
		this.proxyTime = proxyTime;
		time = "* Time in seconds: ";
	}

	public void run() {
		if (Reader.checkFile(matlabPath) == false) {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							"Path to MATLAB.exe is not good. Please change path in Settings->Configuration",
							"Results", JOptionPane.ERROR_MESSAGE);
			frame.setVisible(false);
			return;
		}
		cancelTrain = false;
		mainFrame.setEnabled(false);
		frame.setTitle("Please wait: RLSR is in progress ");
		JButton cancel = new JButton();
		frame.add(cancel);
		frame.setEnabled(true);
		cancel.setBounds(0, 0, 80, 30);
		cancel.setText("Cancel");
		Style.buttonStyle(cancel);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(true);
				frame.setVisible(false);
				Reader.deleteDir(new File(modelFolder));
				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec("taskkill /F /IM MATLAB.exe");
				} catch (IOException e1) {
				}
				cancelTrain = true;
				JOptionPane.showMessageDialog(frame,
						"Training process is canceled.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		long start = System.currentTimeMillis();

		String message = RLSR.train(matlabPath, y, x, noTime, training,
				maxIter, noOfNodes, validation, noX, lfSize, test, iterNN,
				hidden, iterSSE, iterLs, lambda, frame, modelFolder, proxyTime);
		if (!cancelTrain) {
			long elapsedTime = System.currentTimeMillis() - start;
			time += df.format((double) elapsedTime / 1000);
			if (message.contains("R^2")) {
				message += "\n" + time;
				JOptionPane.showMessageDialog(mainFrame, message, "Results",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(mainFrame, message, "Results",
						JOptionPane.ERROR_MESSAGE);
			}
			mainFrame.setEnabled(true);
			frame.setVisible(false);
		}
	}
}
