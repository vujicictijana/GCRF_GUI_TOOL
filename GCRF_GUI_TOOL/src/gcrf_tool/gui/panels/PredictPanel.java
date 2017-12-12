package gcrf_tool.gui.panels;

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

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.exceptions.ConfigurationParameterseException;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.gui.threads.DirGCRFTestMyModelForGUI;
import gcrf_tool.gui.threads.GCRFTestMyModelForGUI;
import gcrf_tool.gui.threads.UmGCRFTestMyModelForGUI;
import gcrf_tool.predictors.helper.Helper;
import gcrf_tool.predictors.linearregression.LinearRegression;
import gcrf_tool.predictors.linearregression.MultivariateLinearRegression;
import gcrf_tool.predictors.neuralnetwork.MyNN;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JComboBox;

public class PredictPanel extends JPanel {

	private static final long serialVersionUID = 356011421979477981L;
	private JButton btnTrain;
	private JPanel panel;
	private JFrame mainFrame;
	private JTextField txtMatrixFile;
	private JLabel lblFileWithEdges;
	private JButton button;
	private JButton btnQuestionS;
	private JLabel label_1;
	private JTextField txtModelName;
	private JLabel lblFileWithAttributes;
	private JTextField txtXFile;
	private JButton button_2;
	private JButton btnQuestionR;
	private JLabel label_4;
	private JTextField txtNoOfNodes;
	private JFileChooser fc;
	private JLabel lblMethod;
	private JComboBox<String> cmbMethod;

	// params
	private String matlabPath;
	private boolean useMatlab;
	private long proxy;

	/**
	 * Create the panel.
	 */
	public PredictPanel(JFrame mainFrame) {
		readParametersFromCfg();
		setBackground(UIManager.getColor("Button.background"));
		setLayout(null);
		add(getBtnTrain());
		panel = this;
		this.mainFrame = mainFrame;
		add(getTxtMatrixFile());
		add(getLabel_1());
		add(getButton());
		add(getBtnQuestionS());
		add(getLabel_1_1());
		add(getTxtModelName());
		add(getLblFileWithAttributes());
		add(getTxtXFile());
		add(getButton_2());
		add(getBtnQuestionR());
		add(getLabel_4());
		add(getTxtNoOfNodes());
		panel = this;
		add(getLblMethod());
		add(getCmbMethod());
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"TEXT FILES", "txt", "text");
		fc.setFileFilter(filter);
	}

	private JButton getBtnTrain() {
		if (btnTrain == null) {
			btnTrain = new JButton("Predict");
			btnTrain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String message = validateData();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message,
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						int noOfNodes = Integer.parseInt(txtNoOfNodes.getText());
						String method = cmbMethod.getSelectedItem().toString();
						String mainPath = Reader.jarFile().getAbsolutePath();
						String dataPath = mainPath + "/MyModels" + method + "/"
								+ txtModelName.getText();
						File matrixFile = new File(txtMatrixFile.getText());
						Writer.copyFile(matrixFile, dataPath
								+ "/data/matrixPredict.txt");
						File xFile = new File(txtXFile.getText());
						Writer.copyFile(xFile, dataPath + "/data/xPredict.txt");

						String[] x = Reader.read(txtXFile.getText());
						if (x == null) {
							JOptionPane.showMessageDialog(mainFrame,
									"Reading file error.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						if (x.length != noOfNodes) {
							JOptionPane.showMessageDialog(mainFrame,
									"Number of lines in the file with attributes should be "
											+ noOfNodes + ".", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						double result = callPredictor(dataPath, x);

						if (result == -7000) {
							JOptionPane.showMessageDialog(mainFrame,
									"Unknown predictor.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (result == -3000) {
							JOptionPane
									.showMessageDialog(
											mainFrame,
											"Predictor cannot be applied to your data. Choose different predictor.",
											"Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (result == -5000) {
							JOptionPane
									.showMessageDialog(
											mainFrame,
											"File with attributes is not in correct format.",
											"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							double[] r = Reader.readArray(dataPath
									+ "/data/rPredict.txt", noOfNodes);
							double[][] s = Reader.readGraph(
									txtMatrixFile.getText(), noOfNodes);

							if (s == null) {
								JOptionPane.showMessageDialog(mainFrame,
										"Ordinal number of node can be between 1 and "
												+ noOfNodes + ".", "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}

							callMethod(noOfNodes, method, dataPath, r, s);

						}

					}
				}

			});

			Style.buttonStyle(btnTrain);
			btnTrain.setBounds(330, 235, 112, 45);
		}
		return btnTrain;
	}

	private void testDirGCRF(int noOfNodes, String modelFolder, double[] r,
			double[][] s) {
		double[] y = new double[r.length];
		DirGCRFTestMyModelForGUI test = new DirGCRFTestMyModelForGUI(mainFrame,
				null, modelFolder, s, r, y);
		test.start();
	}

	private void testGCRF(int noOfNodes, String modelFolder, double[] r,
			double[][] s) {
		double[] y = new double[r.length];
		GCRFTestMyModelForGUI test = new GCRFTestMyModelForGUI(mainFrame, null,
				modelFolder, s, r, y);
		test.start();
	}

	private void testUmGCRF(String modelFolder, double[] r, double[][] s) {
		ProgressBar frame = new ProgressBar("Testing");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		double[] y = new double[r.length];
		UmGCRFTestMyModelForGUI test = new UmGCRFTestMyModelForGUI(matlabPath,
				mainFrame, null, modelFolder, s, r, y, frame, proxy);
		test.start();
	}

	public void chooseFile(JTextField txt) {
		int returnVal = fc.showOpenDialog(panel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			txt.setText(fc.getSelectedFile().getPath());
		}
	}

	private void callMethod(int noOfNodes, String method, String dataPath,
			double[] r, double[][] s) {

		switch (method) {
		case "DirGCRF":
			testDirGCRF(noOfNodes, dataPath, r, s);
			break;
		case "GCRF":
			if (BasicCalcs.isSymmetric(s)) {
				testGCRF(noOfNodes, dataPath, r, s);
			} else {
				JOptionPane.showMessageDialog(mainFrame,
						"For GCRF method matrix should be symmetric.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "UmGCRF":
			if (BasicCalcs.isSymmetric(s)) {
				testUmGCRF(dataPath, r, s);
			} else {
				JOptionPane.showMessageDialog(mainFrame,
						"For UmGCRF method matrix should be symmetric.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			break;
		default:
			JOptionPane.showMessageDialog(mainFrame, "Unknown method.",
					"Error", JOptionPane.ERROR_MESSAGE);
			break;
		}
	}

	public String validateData() {
		if (cmbMethod.getSelectedIndex() == 0) {
			return "Choose method.";
		}
		if (txtMatrixFile.getText().equals("")) {
			return "Choose matrix file.";
		}
		if (txtXFile.getText().equals("")) {
			return "Choose file with attributes.";
		}
		try {
			Integer.parseInt(txtNoOfNodes.getText());
		} catch (NumberFormatException e) {
			return "No. of nodes should be integer.";
		}
		if (txtModelName.getText().equals("")) {
			return "Insert model name.";
		}
		
		String method = cmbMethod.getSelectedItem().toString();
		if (!Writer.checkFolder(Reader.jarFile()  + "/MyModels" + method + "/"
				+ txtModelName.getText())) {
			return "Model with name " + txtModelName.getText()
					+ " does not exist.";
		}
		return null;
	}

	public boolean checkModel(String path) {
		return Writer.checkFolder(path);
	}

	private JTextField getTxtMatrixFile() {
		if (txtMatrixFile == null) {
			txtMatrixFile = new JTextField();
			txtMatrixFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtMatrixFile.setColumns(10);
			txtMatrixFile.setBounds(178, 68, 315, 30);
		}
		return txtMatrixFile;
	}

	private JLabel getLabel_1() {
		if (lblFileWithEdges == null) {
			lblFileWithEdges = new JLabel("File with edges:");
			lblFileWithEdges.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFileWithEdges.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblFileWithEdges.setBounds(34, 66, 129, 30);
		}
		return lblFileWithEdges;
	}

	private JButton getButton() {
		if (button == null) {
			button = new JButton("Browse");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chooseFile(txtMatrixFile);
				}
			});
			button.setBounds(510, 68, 100, 30);
		}
		return button;
	}

	private JButton getBtnQuestionS() {
		if (btnQuestionS == null) {
			btnQuestionS = new JButton("");
			Style.questionButtonStyle(btnQuestionS);
			btnQuestionS.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Text file (.txt) that contains data about connections between nodes."
											+ "\nThis file contains data about all edges in following format: "
											+ "from node, to node, weight\n"
											+ "For example an edge from node 1 to node 2 with weight 10 will be presented as: "
											+ "1,2,10"
											+ "\nEach edge should be in a separate line."
											+ "\nNodes are represented by ordinal numbers.",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
			btnQuestionS.setBounds(629, 68, 30, 30);
		}
		return btnQuestionS;
	}

	private JLabel getLabel_1_1() {
		if (label_1 == null) {
			label_1 = new JLabel("Model name:");
			label_1.setHorizontalAlignment(SwingConstants.RIGHT);
			label_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
			label_1.setBounds(62, 148, 100, 30);
		}
		return label_1;
	}

	private JTextField getTxtModelName() {
		if (txtModelName == null) {
			txtModelName = new JTextField();
			txtModelName.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtModelName.setColumns(10);
			txtModelName.setBounds(178, 148, 315, 30);
		}
		return txtModelName;
	}

	private JLabel getLblFileWithAttributes() {
		if (lblFileWithAttributes == null) {
			lblFileWithAttributes = new JLabel("File with attributes:");
			lblFileWithAttributes.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFileWithAttributes.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblFileWithAttributes.setBounds(25, 107, 138, 30);
		}
		return lblFileWithAttributes;
	}

	private JTextField getTxtXFile() {
		if (txtXFile == null) {
			txtXFile = new JTextField();
			txtXFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtXFile.setColumns(10);
			txtXFile.setBounds(178, 109, 315, 30);
		}
		return txtXFile;
	}

	private JButton getButton_2() {
		if (button_2 == null) {
			button_2 = new JButton("Browse");
			button_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chooseFile(txtXFile);
				}
			});
			button_2.setBounds(510, 109, 100, 30);
		}
		return button_2;
	}

	private JButton getBtnQuestionR() {
		if (btnQuestionR == null) {
			btnQuestionR = new JButton("");
			btnQuestionR.setBounds(629, 109, 30, 30);
			Style.questionButtonStyle(btnQuestionR);
			btnQuestionR.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Text file (.txt) that contains value of each atribute for each node."
											+ "\nAtributes for each node should be in a separate line. "
											+ "\nAtributes should be comma separated."
											+ "\nAll atributes should be numbers. "
											+ "\nOrder should be consistent with ordinal numbers of nodes in the file with edges.",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionR;
	}

	private double callPredictor(String path, String[] x) {

		if (Writer.checkFolder(path + "/nn")) {
			return MyNN.testNoY(path, x);
		}
		if (Writer.checkFolder(path + "/mlr")) {
			double[][] xMlr = Helper.prepareDataForLR(x);
			MultivariateLinearRegression m = (MultivariateLinearRegression) Helper
					.deserilazie(path + "/mlr/lr.txt");
			double[] y = new double[x.length];
			return m.test(y, xMlr, path, true);
		}
		if (Writer.checkFolder(path + "/lr")) {
			double[][] xMlr = Helper.prepareDataForLR(x);
			double[] xOne = new double[xMlr.length];
			for (int i = 0; i < xOne.length; i++) {
				xOne[i] = xMlr[i][0];
			}
			LinearRegression lr = (LinearRegression) Helper.deserilazie(path
					+ "/lr/lr.txt");
			double[] y = new double[x.length];
			return LinearRegression.test(y, xOne, path, lr, true);
		}
		return -7000;

	}

	private JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel("No. of nodes:");
			label_4.setHorizontalAlignment(SwingConstants.RIGHT);
			label_4.setFont(new Font("Segoe UI", Font.BOLD, 15));
			label_4.setBounds(62, 190, 100, 30);
		}
		return label_4;
	}

	private JTextField getTxtNoOfNodes() {
		if (txtNoOfNodes == null) {
			txtNoOfNodes = new JTextField();
			txtNoOfNodes.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtNoOfNodes.setColumns(10);
			txtNoOfNodes.setBounds(178, 189, 91, 30);
		}
		return txtNoOfNodes;
	}

	private JLabel getLblMethod() {
		if (lblMethod == null) {
			lblMethod = new JLabel("Method:");
			lblMethod.setHorizontalAlignment(SwingConstants.RIGHT);
			lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblMethod.setBounds(62, 27, 100, 30);
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
			cmbMethod.setBounds(178, 27, 227, 30);
			cmbMethod.addItem("choose method");
			cmbMethod.addItem("GCRF");
			cmbMethod.addItem("DirGCRF");
			if (useMatlab) {
				cmbMethod.addItem("UmGCRF");
			}
		}
		return cmbMethod;
	}
}
