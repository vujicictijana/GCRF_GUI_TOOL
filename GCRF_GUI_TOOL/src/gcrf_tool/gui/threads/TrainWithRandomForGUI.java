package gcrf_tool.gui.threads;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gcrf_tool.algorithms.asymmetric.AlgorithmAsymmetric;
import gcrf_tool.algorithms.asymmetric.GradientDescentAsymmetric;
import gcrf_tool.algorithms.symmetric.AlgorithmSymmetric;
import gcrf_tool.algorithms.symmetric.CalculationsSymmetric;
import gcrf_tool.algorithms.symmetric.GradientDescentSymmetric;
import gcrf_tool.data.generators.GraphGenerator;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.frames.ProgressBar;
import gcrf_tool.gui.style.Style;

public class TrainWithRandomForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;

	private double s[][];
	private double r[];
	private double y[];
	private double alpha;
	private double beta;
	private double lr;
	private int maxIter;
	private JPanel panel;
	private boolean both;
	private String modelFolder;
	private int xTable;
	private int yTable;
	private JLabel timeLabel;
	private String time;
	private double alphaGen ;
	private double betaGen ;
	DecimalFormat df = new DecimalFormat("#.##");
	private Thread thisThread;
	
	public TrainWithRandomForGUI(String modelFolder, ProgressBar frame,
			JFrame mainFrame, double[][] s, double[] r, double[] y,
			double alpha, double beta, double lr, int maxIter, JPanel panel,
			boolean both, int xTable, int yTable, JLabel timeLabel, double alphaGen, double betaGen) {
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
		this.panel = panel;
		this.both = both;
		this.modelFolder = modelFolder;
		this.xTable = xTable;
		this.yTable = yTable;
		this.timeLabel = timeLabel;
		this.alphaGen = alphaGen;
		this.betaGen = betaGen;
		time = "Time in seconds: ";
		// System.out.println(Writer.edges(s));
		this.thisThread = this;
	}

	public void run() {
		mainFrame.setEnabled(false);
		frame.setTitle("Progress DirGCRF");
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
		GradientDescentAsymmetric gda = new GradientDescentAsymmetric(alpha,
				beta, lr, s, r, y);
		long start = System.currentTimeMillis();
		double[] res = gda.learn(maxIter, false, frame.getCurrent());
		long elapsedTime = System.currentTimeMillis() - start;
		time += "DirGCRF: " + df.format((double) elapsedTime/1000);

		AlgorithmAsymmetric alg = new AlgorithmAsymmetric(res[0], res[1], s, r,
				y);
		double r2 = alg.rSquared();

		double[] resS = null;
		double r2S = -1;
		if (both) {
			frame.getCurrent().setValue(0);
			frame.setTitle("Progress GCRF");
			start = System.currentTimeMillis();
			double[][] sS = GraphGenerator.converteGraphToUndirected(s);
			CalculationsSymmetric cS = new CalculationsSymmetric(sS, r);
			double[] yS = cS.y(alphaGen, betaGen, 0.05);
			GradientDescentSymmetric gdS = new GradientDescentSymmetric(alpha,
					beta, lr, sS, r, yS);
			start = System.currentTimeMillis();
			resS = gdS.learn(maxIter, false, frame.getCurrent());
			elapsedTime = System.currentTimeMillis() - start;

			AlgorithmSymmetric algS = new AlgorithmSymmetric(resS[0], resS[1],
					sS, r, yS);
			r2S = algS.rSquared();
			elapsedTime = System.currentTimeMillis() - start;
			time += " GCRF: " + df.format((double) elapsedTime/1000);
		}
		createTable(res, r2, resS, r2S);
		createFile("DirGCRF.txt", res);
		if (resS != null) {
			createFile("GCRF.txt", resS);
		}
		timeLabel.setText(time);
		timeLabel.setVisible(true);
		mainFrame.setEnabled(true);
		frame.setVisible(false);
	}

	public JTable createTable(double[] res, double r2, double[] resS, double r2S) {
		String[] columnNames = { "Alg. ", "Alpha", "Beta",
				"R^2 (with same data)" };
		Object[][] data = fillData(res, r2, resS, r2S);

		JTable table = new JTable(data, columnNames);

		table.setBackground(new Color(240, 240, 240));
		panel.removeAll();
		panel.repaint();
		panel.revalidate();
		JScrollPane scrollPane = new JScrollPane(table);
		Style.resultTable(table, -1);
		panel.add(scrollPane);
		if (resS == null) {
			scrollPane.setBounds(xTable, yTable, 700, 50);
		} else {
			scrollPane.setBounds(xTable, yTable, 700, 75);
		}
		return table;
	}

	public void createFile(String symmetric, double[] results) {
		Writer.createFolder(modelFolder);
		String fileName = modelFolder + "/" + symmetric;
		String[] resultsS = { results[0] + "", results[1] + "" };
		Writer.write(resultsS, fileName);
	}

	public Object[][] fillData(double[] res, double r2, double[] resS,
			double r2S) {
		Object[][] data = null;
		if (resS == null) {
			data = new Object[1][4];
			data[0][0] = "DirGCRF";
			data[0][1] = res[0];
			data[0][2] = res[1];
			data[0][3] = r2;
		} else {
			data = new Object[2][4];
			data[0][0] = "DirGCRF";
			data[0][1] = res[0];
			data[0][2] = res[1];
			data[0][3] = r2;
			data[1][0] = "GCRF";
			data[1][1] = resS[0];
			data[1][2] = resS[1];
			data[1][3] = r2S;
		}
		return data;
	}
}
