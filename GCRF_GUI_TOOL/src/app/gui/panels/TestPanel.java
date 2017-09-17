package app.gui.panels;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import app.algorithms.basic.BasicCalcs;
import app.exceptions.ConfigurationParameterseException;
import app.file.io.Reader;
import app.file.io.Writer;
import app.gui.frames.ProgressBar;
import app.gui.style.Style;
import app.gui.threads.GCRFTestMyModelForGUI;
import app.gui.threads.DirGCRFTestMyModelForGUI;
import app.gui.threads.UmGCRFTestMyModelForGUI;
import app.predictors.helper.Helper;
import app.predictors.linearregression.LinearRegression;
import app.predictors.linearregression.MultivariateLinearRegression;
import app.predictors.neuralnetwork.MyNN;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JComboBox;

import org.neuroph.core.data.DataSet;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.SystemColor;

public class TestPanel extends JPanel {

	private static final long serialVersionUID = 356011421979477981L;
	private JButton btnTrain;
	private JPanel panel;
	private JFrame mainFrame;
	private JPanel panelForTable;
	private JLabel label_1;
	private JTextField txtModelName;
	private JFileChooser fc;
	private JLabel lblMethod;
	private JComboBox<String> cmbMethod;

	// params
	private String matlabPath;
	private boolean useMatlab;
	private long proxy;
	private JLabel label;
	private JComboBox<String> cmbDataset;
	

	private String xPath = "";
	private String yPath = "";
	private String sPath = "";
	private JLabel label_2;
	private JLabel lblTestYourMethod;

	/**
	 * Create the panel.
	 */
	public TestPanel(JFrame mainFrame) {
		readParametersFromCfg();
		this.mainFrame = mainFrame;
		setBackground(UIManager.getColor("Button.background"));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{134, 268, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 30, 33, 30, 36, 0, 45, 127, 63, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblTestYourMethod = new GridBagConstraints();
		gbc_lblTestYourMethod.fill = GridBagConstraints.BOTH;
		gbc_lblTestYourMethod.gridwidth = 15;
		gbc_lblTestYourMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblTestYourMethod.gridx = 0;
		gbc_lblTestYourMethod.gridy = 0;
		add(getLblTestYourMethod(), gbc_lblTestYourMethod);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.gridwidth = 10;
		gbc_label_2.fill = GridBagConstraints.BOTH;
		gbc_label_2.insets = new Insets(0, 0, 5, 0);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 1;
		add(getLabel_2(), gbc_label_2);
		GridBagConstraints gbc_lblMethod = new GridBagConstraints();
		gbc_lblMethod.fill = GridBagConstraints.BOTH;
		gbc_lblMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblMethod.gridx = 0;
		gbc_lblMethod.gridy = 2;
		add(getLblMethod(), gbc_lblMethod);
		GridBagConstraints gbc_cmbMethod = new GridBagConstraints();
		gbc_cmbMethod.anchor = GridBagConstraints.WEST;
		gbc_cmbMethod.fill = GridBagConstraints.BOTH;
		gbc_cmbMethod.insets = new Insets(0, 0, 5, 5);
		gbc_cmbMethod.gridx = 1;
		gbc_cmbMethod.gridy = 2;
		add(getCmbMethod(), gbc_cmbMethod);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 3;
		add(getLabel(), gbc_label);
		GridBagConstraints gbc_cmbDataset = new GridBagConstraints();
		gbc_cmbDataset.anchor = GridBagConstraints.WEST;
		gbc_cmbDataset.fill = GridBagConstraints.BOTH;
		gbc_cmbDataset.insets = new Insets(0, 0, 5, 5);
		gbc_cmbDataset.gridx = 1;
		gbc_cmbDataset.gridy = 3;
		add(getCmbDataset(), gbc_cmbDataset);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.fill = GridBagConstraints.VERTICAL;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 4;
		add(getLabel_1_1(), gbc_label_1);
		GridBagConstraints gbc_txtModelName = new GridBagConstraints();
		gbc_txtModelName.anchor = GridBagConstraints.WEST;
		gbc_txtModelName.fill = GridBagConstraints.BOTH;
		gbc_txtModelName.insets = new Insets(0, 0, 5, 5);
		gbc_txtModelName.gridx = 1;
		gbc_txtModelName.gridy = 4;
		add(getTxtModelName(), gbc_txtModelName);
		panel = this;
		GridBagConstraints gbc_btnTrain = new GridBagConstraints();
		gbc_btnTrain.fill = GridBagConstraints.BOTH;
		gbc_btnTrain.insets = new Insets(0, 0, 5, 5);
		gbc_btnTrain.gridx = 1;
		gbc_btnTrain.gridy = 6;
		add(getBtnTrain(), gbc_btnTrain);
		GridBagConstraints gbc_panelForTable = new GridBagConstraints();
		gbc_panelForTable.insets = new Insets(0, 0, 5, 5);
		gbc_panelForTable.fill = GridBagConstraints.BOTH;
		gbc_panelForTable.gridwidth = 8;
		gbc_panelForTable.gridx = 1;
		gbc_panelForTable.gridy = 8;
		add(getPanelForTable(), gbc_panelForTable);
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fc.setFileFilter(filter);
	}

	private JButton getBtnTrain() {
		if (btnTrain == null) {
			btnTrain = new JButton("TEST");
			btnTrain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String message = validateData();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						String mainPathDatasets = Reader.jarFile() 
								+ "/Datasets/Networks/";

						xPath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/xTest.txt";
						yPath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/yTest.txt";
						sPath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/sTest.txt";

						String readme = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/readme.txt";
						int noOfNodes = 0;
						try {
							String nodesTrain = Reader.read(readme)[1];
							noOfNodes = Integer.parseInt(nodesTrain
									.substring(nodesTrain.indexOf(":") + 2));
							if (noOfNodes <= 0) {
								JOptionPane
										.showMessageDialog(
												mainFrame,
												"No. of nodes should be greater than 0.",
												"Error",
												JOptionPane.ERROR_MESSAGE);
								return;
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(mainFrame,
									"Reading dataset error.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						String method = cmbMethod.getSelectedItem().toString();

						Writer.createFolder(Reader.jarFile() + "/MyModels" + method);
						String dataPath = Reader.jarFile()  + "/MyModels" + method + "/" + txtModelName.getText();
						File matrixFile = new File(sPath);
						Writer.copyFile(matrixFile, dataPath + "/data/matrixTest.txt");
						File xFile = new File(xPath);
						Writer.copyFile(xFile, dataPath + "/data/xTest.txt");
						File yFile = new File(yPath);
						Writer.copyFile(yFile, dataPath + "/data/yTest.txt");

						String[] x = Reader.read(xPath);
						if (x == null) {
							JOptionPane.showMessageDialog(mainFrame, "Reading file error.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						if (x.length != noOfNodes) {
							JOptionPane.showMessageDialog(mainFrame,
									"Number of lines in the file with attributes should be " + noOfNodes + ".", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						double[] y = Reader.readArray(yPath, noOfNodes);
						if (y == null) {
							JOptionPane.showMessageDialog(mainFrame,
									"Number of lines in in the file with outputs should be " + noOfNodes + ".", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						double result = callPredictor(dataPath, x, y);
						if (result == -9000) {
							JOptionPane.showMessageDialog(mainFrame, "Selected dataset is not suitable for this model.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (result == -7000) {
							JOptionPane.showMessageDialog(mainFrame, "Unknown predictor.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (result == -3000) {
							JOptionPane.showMessageDialog(mainFrame,
									"Predictor cannot be applied to your data. Choose different predictor.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (result == -5000) {
							JOptionPane.showMessageDialog(mainFrame, "File with attributes is not in correct format.",
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							double[] r = Reader.readArray(dataPath + "/data/rTest.txt", noOfNodes);
							double[][] s = Reader.readGraph(sPath, noOfNodes);

							if (s == null) {
								JOptionPane.showMessageDialog(mainFrame,
										"Ordinal number of node can be between 1 and " + noOfNodes + ".", "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}

							callMethod(noOfNodes, method, dataPath, y, r, s);

						}

					}
				}

			});

			Style.buttonStyle(btnTrain);
		}
		return btnTrain;
	}

	private void testDirGCRF(int noOfNodes, String modelFolder, double[] r, double[] y, double[][] s) {
		DirGCRFTestMyModelForGUI test = new DirGCRFTestMyModelForGUI(mainFrame, panelForTable, modelFolder, s, r, y);
		test.start();
	}

	private void testGCRF(int noOfNodes, String modelFolder, double[] r, double[] y, double[][] s) {
		GCRFTestMyModelForGUI test = new GCRFTestMyModelForGUI(mainFrame, panelForTable, modelFolder, s, r, y);
		test.start();
	}

	private void testUmGCRF(String modelFolder, double[] r, double[] y, double[][] s) {
		ProgressBar frame = new ProgressBar("Testing");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		UmGCRFTestMyModelForGUI test = new UmGCRFTestMyModelForGUI(matlabPath, mainFrame, panelForTable, modelFolder, s,
				r, y, frame, proxy);
		test.start();
	}

	private void callMethod(int noOfNodes, String method, String dataPath, double[] y, double[] r, double[][] s) {

		switch (method) {
		case "DirGCRF":
			testDirGCRF(noOfNodes, dataPath, r, y, s);
			break;
		case "GCRF":
			if (BasicCalcs.isSymmetric(s)) {
				testGCRF(noOfNodes, dataPath, r, y, s);
			} else {
				JOptionPane.showMessageDialog(mainFrame, "For GCRF method matrix should be symmetric.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "UmGCRF":
			if (BasicCalcs.isSymmetric(s)) {
				testUmGCRF(dataPath, r, y, s);
			} else {
				JOptionPane.showMessageDialog(mainFrame, "For UmGCRF method matrix should be symmetric.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		default:
			JOptionPane.showMessageDialog(mainFrame, "Unknown method.", "Error", JOptionPane.ERROR_MESSAGE);
			break;
		}
	}

	public String validateData() {
		if (cmbDataset.getSelectedIndex() == 0) {
			return "Choose dataset.";
		}
		if (txtModelName.getText().equals("")) {
			return "Insert model name.";
		}
		String method = cmbMethod.getSelectedItem().toString();

		if (!Writer.checkFolder(Reader.jarFile()  + "/MyModels"  + method + "/" + txtModelName.getText())) {
			return "Model with name " + txtModelName.getText() + " does not exist.";
		}
		return null;
	}

	public boolean checkModel(String path) {
		return Writer.checkFolder(path);
	}

	private JPanel getPanelForTable() {
		if (panelForTable == null) {
			panelForTable = new JPanel();
			panelForTable.setLayout(null);
		}
		return panelForTable;
	}

	private JLabel getLabel_1_1() {
		if (label_1 == null) {
			label_1 = new JLabel("Model name:");
			label_1.setHorizontalAlignment(SwingConstants.RIGHT);
			label_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_1;
	}

	private JTextField getTxtModelName() {
		if (txtModelName == null) {
			txtModelName = new JTextField();
			txtModelName.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtModelName.setColumns(10);
		}
		return txtModelName;
	}

	private double callPredictor(String path, String[] x, double[] y) {

		if (Writer.checkFolder(path + "/nn")) {

			DataSet testSet = Helper.prepareDataForNN(x, y);
			
			return MyNN.test(path, testSet);

		}
		if (Writer.checkFolder(path + "/mlr")) {
			double[][] xMlr = Helper.prepareDataForLR(x);
			MultivariateLinearRegression m = (MultivariateLinearRegression) Helper.deserilazie(path + "/mlr/lr.txt");
			return m.test(y, xMlr, path, true);
		}
		if (Writer.checkFolder(path + "/lr")) {
			double[][] xMlr = Helper.prepareDataForLR(x);
			double[] xOne = new double[xMlr.length];
			for (int i = 0; i < xOne.length; i++) {
				xOne[i] = xMlr[i][0];
			}
			LinearRegression lr = (LinearRegression) Helper.deserilazie(path + "/lr/lr.txt");
			return LinearRegression.test(y, xOne, path, lr, true);
		}
		return -7000;

	}

	private JLabel getLblMethod() {
		if (lblMethod == null) {
			lblMethod = new JLabel("Method:");
			lblMethod.setHorizontalAlignment(SwingConstants.RIGHT);
			lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblMethod;
	}

	public String readParametersFromCfg() {
		try {
			Map<String, String> params = Reader.readCfg();

			if (params.get("Use MATLAB").toString().contains("true")) {
				useMatlab = true;
			} else {
				useMatlab = false;
			}
			matlabPath = params.get("Path").toString();
			proxy = Integer.parseInt(params.get("Proxy").toString());
		} catch (ConfigurationParameterseException e) {
			return e.getMessage();
		}
		return null;
	}

	private JComboBox<String> getCmbMethod() {
		if (cmbMethod == null) {
			cmbMethod = new JComboBox<String>();
			cmbMethod.addItem("choose method");
			cmbMethod.addItem("GCRF");
			cmbMethod.addItem("DirGCRF");
			if (useMatlab) {
				cmbMethod.addItem("UmGCRF");
			}
		}
		return cmbMethod;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Dataset:");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label;
	}
	private JComboBox<String> getCmbDataset() {
		if (cmbDataset == null) {
			cmbDataset = new JComboBox();
			cmbDataset.addItem("choose dataset");
			String[] files = Reader.getAllFolders(Reader.jarFile()  + "/Datasets/Networks");
			for (int i = 0; i < files.length; i++) {
				cmbDataset.addItem(files[i]);
			}
		}
		return cmbDataset;
	}
	private JLabel getLabel_2() {
		if (label_2 == null) {
			label_2 = new JLabel("DATA:");
			label_2.setOpaque(true);
			label_2.setHorizontalAlignment(SwingConstants.CENTER);
			label_2.setForeground(SystemColor.control);
			label_2.setFont(new Font("Segoe UI", Font.BOLD, 15));
			label_2.setBackground(SystemColor.control);
		}
		return label_2;
	}
	private JLabel getLblTestYourMethod() {
		if (lblTestYourMethod == null) {
			lblTestYourMethod = new JLabel("TEST YOUR METHOD:");
			lblTestYourMethod.setOpaque(true);
			lblTestYourMethod.setHorizontalAlignment(SwingConstants.CENTER);
			lblTestYourMethod.setForeground(Color.WHITE);
			lblTestYourMethod.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblTestYourMethod.setBackground(Color.GRAY);
		}
		return lblTestYourMethod;
	}
}
