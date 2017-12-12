package gcrf_tool.gui.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gcrf_tool.file.Reader;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.methods.matlab.UpGCRF;

public class UpGCRFTrainMyModelForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;

	private double s[][];
	private double x[][];
	private double y[][];
	private String modelFolder;
	private String time;
	private String matlabPath;
	private int noTime;
	private int noTest;
	private int training;
	private int maxIter;
	private int noOfNodes;
	private int lag;
	private int noX;
	private boolean useX;
	private long proxyTime;
	private boolean cancelTrain;
	DecimalFormat df = new DecimalFormat("#.##");

	public UpGCRFTrainMyModelForGUI(String matlabPath, String modelFolder,
			ProgressBar frame, JFrame mainFrame, double[][] s, double[][] r,
			double[][] y, int noTime, int training, int maxIter, int noOfNodes,
			int lag, int noX, boolean useX, int noTest, long proxyTime) {
		super();
		this.frame = frame;
		this.mainFrame = mainFrame;
		this.s = s;
		this.x = r;
		this.y = y;
		this.modelFolder = modelFolder;
		this.matlabPath = matlabPath;
		this.noTime = noTime;
		this.training = training;
		this.maxIter = maxIter;
		this.noOfNodes = noOfNodes;
		this.lag = lag;
		this.noX = noX;
		this.useX = useX;
		this.noTest = noTest;
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
		frame.setTitle("Please wait: up-GCRF is in progress ");
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

		String message = UpGCRF.train(matlabPath, s, y, x, noTime, training,
				noTest, maxIter, noOfNodes, lag, noX, useX, frame, modelFolder,
				proxyTime);
		if (!cancelTrain) {
			long elapsedTime = System.currentTimeMillis() - start;
			time += df.format((double) elapsedTime / 1000);
			;
			if (message.contains("successfully")) {
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
