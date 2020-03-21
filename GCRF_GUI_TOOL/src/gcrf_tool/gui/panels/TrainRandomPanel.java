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

package gcrf_tool.gui.panels;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import gcrf_tool.calculations.CalculationsDirGCRF;
import gcrf_tool.data.generators.ArrayGenerator;
import gcrf_tool.data.generators.GraphGenerator;
import gcrf_tool.exceptions.ConfigurationParameterseException;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.gui.threads.TrainWithRandomForGUI;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class TrainRandomPanel extends JPanel {

	private static final long serialVersionUID = 2574347379339766542L;
	private JLabel lblType;
	private JButton btnQuestionType;
	private JTextField txtProb;
	private JLabel lblRArrayFile;
	private JLabel lblAlpha;
	private JLabel lblFirstBeta;
	private JTextField txtAlpha;
	private JTextField txtBeta;
	private JLabel lblLearningRate;
	private JTextField txtLR;
	private JLabel lblYArrayFile;
	private JTextField txtNoOfNodes;
	private JButton btnTrain;
	private JButton btnTest;
	private JTextField txtIter;
	private JLabel label;
	private JComboBox<String> cmbGraphType;
	private JFrame mainFrame;
	private JCheckBox chckbxSymmetric;
	private JLabel lblTrainSymmetric;
	private JPanel panelForTable;
	private JLabel lblTime;
	private JLabel lblTrainRandomModels;
	private TestRandomPanel testPanel;

	private JTabbedPane tabbedPane;

	// params

	private int alphaGen;
	private int betaGen;
	private int alpha;
	private int beta;
	private double lr;
	private int iterations;
	private JLabel lblTrainDirgcrfOn;

	/**
	 * Create the panel.
	 */
	public TrainRandomPanel(JFrame mainFrame) {
		if (Reader.checkFile(Reader.jarFile() + "/cfg.txt")) {
			String result = readParametersFromCfg();
			if (result != null) {
				JOptionPane
						.showMessageDialog(
								mainFrame,
								result
										+ " Please configure parameters values in Settings->Configuration.",
								"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				setBackground(UIManager.getColor("Button.background"));
				

				this.mainFrame = mainFrame;
				
				
				GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.columnWidths = new int[] { 0, 0 };
				gridBagLayout.rowHeights = new int[] { 0,0};
				gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
				gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
				setLayout(gridBagLayout);
				
				GridBagConstraints gbc_lblTrainDirgcrfOn = new GridBagConstraints();
				gbc_lblTrainDirgcrfOn.gridwidth = 15;
				gbc_lblTrainDirgcrfOn.fill = GridBagConstraints.BOTH;
				gbc_lblTrainDirgcrfOn.insets = new Insets(0, 0, 5, 5);
				gbc_lblTrainDirgcrfOn.gridx = 0;
				gbc_lblTrainDirgcrfOn.gridy = 0;
				add(getLblTrainDirgcrfOn(), gbc_lblTrainDirgcrfOn);
				
				GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
				gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
				gbc_tabbedPane.fill = GridBagConstraints.BOTH;
				gbc_tabbedPane.gridx = 0;
				gbc_tabbedPane.gridy = 1;
				add(getTabbedPane(), gbc_tabbedPane);
		
				
			
				createMainFolders();
				setTxtValues();
		
			}
		} else {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							"Please configure parameters values in Settings->Configuration.",
							"Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	
	
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			
			JComponent panel1 = makePanelTrain();
			tabbedPane.addTab("TRAIN", null, panel1,
					"Parameters for random data generation");

			tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

			JComponent panel2 = makePanelTest();
			tabbedPane.addTab("TEST", null, panel2,
					"Parameters for training GCRF and DirGCRF");
			tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
			
			tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
		        public void stateChanged(javax.swing.event.ChangeEvent evt) {
		            if(tabbedPane.getSelectedIndex()==1) {
		            	testPanel.refreshCMB();
		            }
		        }
		});
			
		}
		return tabbedPane;
	}
	
	protected JComponent makePanelTrain() {
		JPanel panel = new JPanel(false);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 145, 91, 58, 166, 357,
				0 };
		gridBagLayout.rowHeights = new int[] { 36, 33, 31, 30, 31, 32,
				30, 0, 0, 0, 30, 130, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panel.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_lblTestRandomModels = new GridBagConstraints();
		gbc_lblTestRandomModels.fill = GridBagConstraints.BOTH;
		gbc_lblTestRandomModels.gridwidth = 15;
		gbc_lblTestRandomModels.insets = new Insets(0, 0, 5, 5);
		gbc_lblTestRandomModels.gridx = 0;
		gbc_lblTestRandomModels.gridy = 0;
		panel.add(getLblTestRandomModels(), gbc_lblTestRandomModels);
		
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.fill = GridBagConstraints.VERTICAL;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 2;
		panel.add(getLblType(), gbc_lblType);

		GridBagConstraints gbc_btnQuestionType = new GridBagConstraints();
		gbc_btnQuestionType.anchor = GridBagConstraints.WEST;
		gbc_btnQuestionType.fill = GridBagConstraints.VERTICAL;
		gbc_btnQuestionType.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuestionType.gridx = 4;
		gbc_btnQuestionType.gridy = 2;
		panel.add(getBtnQuestionS(), gbc_btnQuestionType);
		GridBagConstraints gbc_lblRArrayFile = new GridBagConstraints();
		gbc_lblRArrayFile.anchor = GridBagConstraints.EAST;
		gbc_lblRArrayFile.fill = GridBagConstraints.VERTICAL;
		gbc_lblRArrayFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblRArrayFile.gridx = 0;
		gbc_lblRArrayFile.gridy = 3;
		panel.add(getLblRArrayFile(), gbc_lblRArrayFile);
		GridBagConstraints gbc_txtNoOfNodes = new GridBagConstraints();
		gbc_txtNoOfNodes.fill = GridBagConstraints.BOTH;
		gbc_txtNoOfNodes.insets = new Insets(0, 0, 5, 5);
		gbc_txtNoOfNodes.gridx = 1;
		gbc_txtNoOfNodes.gridy = 3;
		panel.add(getTxtNoOfNodes(), gbc_txtNoOfNodes);
		GridBagConstraints gbc_lblYArrayFile = new GridBagConstraints();
		gbc_lblYArrayFile.anchor = GridBagConstraints.EAST;
		gbc_lblYArrayFile.fill = GridBagConstraints.VERTICAL;
		gbc_lblYArrayFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblYArrayFile.gridx = 0;
		gbc_lblYArrayFile.gridy = 4;
		panel.add(getLblYArrayFile(), gbc_lblYArrayFile);
		GridBagConstraints gbc_txtProb = new GridBagConstraints();
		gbc_txtProb.fill = GridBagConstraints.BOTH;
		gbc_txtProb.insets = new Insets(0, 0, 5, 5);
		gbc_txtProb.gridx = 1;
		gbc_txtProb.gridy = 4;
		panel.add(getTxtProb(), gbc_txtProb);
		GridBagConstraints gbc_lblAlpha = new GridBagConstraints();
		gbc_lblAlpha.anchor = GridBagConstraints.EAST;
		gbc_lblAlpha.fill = GridBagConstraints.VERTICAL;
		gbc_lblAlpha.insets = new Insets(0, 0, 5, 5);
		gbc_lblAlpha.gridx = 0;
		gbc_lblAlpha.gridy = 5;
		panel.add(getLblAlpha(), gbc_lblAlpha);
		GridBagConstraints gbc_txtAlpha = new GridBagConstraints();
		gbc_txtAlpha.fill = GridBagConstraints.BOTH;
		gbc_txtAlpha.insets = new Insets(0, 0, 5, 5);
		gbc_txtAlpha.gridx = 1;
		gbc_txtAlpha.gridy = 5;
		panel.add(getTxtAlpha(), gbc_txtAlpha);
		GridBagConstraints gbc_lblFirstBeta = new GridBagConstraints();
		gbc_lblFirstBeta.anchor = GridBagConstraints.EAST;
		gbc_lblFirstBeta.fill = GridBagConstraints.VERTICAL;
		gbc_lblFirstBeta.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstBeta.gridx = 0;
		gbc_lblFirstBeta.gridy = 6;
		panel.add(getLblFirstBeta(), gbc_lblFirstBeta);
		GridBagConstraints gbc_txtBeta = new GridBagConstraints();
		gbc_txtBeta.fill = GridBagConstraints.BOTH;
		gbc_txtBeta.insets = new Insets(0, 0, 5, 5);
		gbc_txtBeta.gridx = 1;
		gbc_txtBeta.gridy = 6;
		panel.add(getTxtBeta(), gbc_txtBeta);
		GridBagConstraints gbc_lblLearningRate = new GridBagConstraints();
		gbc_lblLearningRate.anchor = GridBagConstraints.EAST;
		gbc_lblLearningRate.fill = GridBagConstraints.VERTICAL;
		gbc_lblLearningRate.insets = new Insets(0, 0, 5, 5);
		gbc_lblLearningRate.gridx = 0;
		gbc_lblLearningRate.gridy = 7;
		panel.add(getLblLearningRate(), gbc_lblLearningRate);
		GridBagConstraints gbc_txtLR = new GridBagConstraints();
		gbc_txtLR.fill = GridBagConstraints.BOTH;
		gbc_txtLR.insets = new Insets(0, 0, 5, 5);
		gbc_txtLR.gridx = 1;
		gbc_txtLR.gridy = 7;
		panel.add(getTxtLr(), gbc_txtLR);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 8;
		panel.add(getLabel(), gbc_label);
		GridBagConstraints gbc_txtIter = new GridBagConstraints();
		gbc_txtIter.fill = GridBagConstraints.BOTH;
		gbc_txtIter.insets = new Insets(0, 0, 5, 5);
		gbc_txtIter.gridx = 1;
		gbc_txtIter.gridy = 8;
		panel.add(getTxtMaxIter(), gbc_txtIter);
		
		GridBagConstraints gbc_lblTrainSymmetric = new GridBagConstraints();
		gbc_lblTrainSymmetric.anchor = GridBagConstraints.NORTH;
		gbc_lblTrainSymmetric.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTrainSymmetric.insets = new Insets(0, 0, 5, 5);
		gbc_lblTrainSymmetric.gridx = 0;
		gbc_lblTrainSymmetric.gridy = 9;
		panel.add(getLblTrainSymmetric(), gbc_lblTrainSymmetric);
		
		GridBagConstraints gbc_chckbxSymmetric = new GridBagConstraints();
		gbc_chckbxSymmetric.anchor = GridBagConstraints.NORTH;
		gbc_chckbxSymmetric.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxSymmetric.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxSymmetric.gridx = 1;
		gbc_chckbxSymmetric.gridy = 9;
		panel.add(getChckbxSymmetric(), gbc_chckbxSymmetric);
		
		GridBagConstraints gbc_btnTrain = new GridBagConstraints();
		gbc_btnTrain.fill = GridBagConstraints.BOTH;
		gbc_btnTrain.insets = new Insets(0, 0, 5, 5);
		gbc_btnTrain.gridx = 3;
		gbc_btnTrain.gridy = 9;
		panel.add(getBtnTrain(), gbc_btnTrain);

		GridBagConstraints gbc_panelForTable = new GridBagConstraints();
		gbc_panelForTable.fill = GridBagConstraints.BOTH;
		gbc_panelForTable.insets = new Insets(0, 0, 5, 0);
		gbc_panelForTable.gridwidth = 5;
		gbc_panelForTable.gridx = 1;
		gbc_panelForTable.gridy = 11;
		panel.add(getPanelForTable(), gbc_panelForTable);
		GridBagConstraints gbc_lblTime = new GridBagConstraints();
		gbc_lblTime.fill = GridBagConstraints.BOTH;
		gbc_lblTime.insets = new Insets(0, 0, 0, 5);
		gbc_lblTime.gridwidth = 4;
		gbc_lblTime.gridx = 1;
		gbc_lblTime.gridy = 12;
		panel.add(getLblTime(), gbc_lblTime);
		GridBagConstraints gbc_cmbGraphType = new GridBagConstraints();
		gbc_cmbGraphType.fill = GridBagConstraints.BOTH;
		gbc_cmbGraphType.insets = new Insets(0, 0, 5, 5);
		gbc_cmbGraphType.gridwidth = 3;
		gbc_cmbGraphType.gridx = 1;
		gbc_cmbGraphType.gridy = 2;
		panel.add(getCmbGraphType(), gbc_cmbGraphType);
		return panel;
	}

	
	protected JComponent makePanelTest() {
		JPanel panel = new JPanel(false);
		
		 testPanel = new TestRandomPanel(mainFrame);
		panel = testPanel;
		
		return panel;
	}
	
	private JLabel getLblType() {
		if (lblType == null) {
			lblType = new JLabel("Graph type:");
			lblType.setHorizontalAlignment(SwingConstants.RIGHT);
			lblType.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblType;
	}

	private JLabel getLblTestRandomModels() {
		if (lblTrainRandomModels == null) {
			lblTrainRandomModels = new JLabel("Train on random networks:");
			lblTrainRandomModels.setOpaque(true);
			lblTrainRandomModels.setHorizontalAlignment(SwingConstants.CENTER);
			lblTrainRandomModels.setForeground(Color.WHITE);
			lblTrainRandomModels.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblTrainRandomModels.setBackground(Color.GRAY);
		}
		return lblTrainRandomModels;
	}
	
	private JButton getBtnQuestionS() {
		if (btnQuestionType == null) {
			btnQuestionType = new JButton("");
			btnQuestionType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Graph types:\n"
											+ "* Directed graph - fully connected directed graph\n"
											+ "* Directed graph with edge probability - edge probability represents the "
											+ "probability that edge between two random nodes exists\n"
											+ "* Directed acyclic graph - directed graph without cycles\n"
											+ "* Directed directed graph without direct feedback- "
											+ "if there is direct connection from A to B, there is no direct connection from B to A\n"
											+ "* Chain -  all nodes are connected in a single sequence, from one node to another\n"
											+ "* Binary tree - graph with a tree structure in which each node could have at most two children\n",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
			Style.questionButtonStyle(btnQuestionType);
		}
		return btnQuestionType;
	}

	private JTextField getTxtProb() {
		if (txtProb == null) {
			txtProb = new JTextField();
			txtProb.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtProb.setColumns(10);
			txtProb.setEnabled(false);
			txtProb.setEditable(false);
		}
		return txtProb;
	}

	private JLabel getLblRArrayFile() {
		if (lblRArrayFile == null) {
			lblRArrayFile = new JLabel("No. of nodes:");
			lblRArrayFile.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRArrayFile.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblRArrayFile;
	}

	private JLabel getLblAlpha() {
		if (lblAlpha == null) {
			lblAlpha = new JLabel("First alpha:");
			lblAlpha.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAlpha.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblAlpha;
	}

	private JLabel getLblFirstBeta() {
		if (lblFirstBeta == null) {
			lblFirstBeta = new JLabel("First beta:");
			lblFirstBeta.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFirstBeta.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblFirstBeta;
	}

	private JTextField getTxtAlpha() {
		if (txtAlpha == null) {
			txtAlpha = new JTextField();
			txtAlpha.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtAlpha.setColumns(10);
		}
		return txtAlpha;
	}

	private JTextField getTxtBeta() {
		if (txtBeta == null) {
			txtBeta = new JTextField();
			txtBeta.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtBeta.setColumns(10);
		}
		return txtBeta;
	}

	private JLabel getLblLearningRate() {
		if (lblLearningRate == null) {
			lblLearningRate = new JLabel("Learning rate:");
			lblLearningRate.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLearningRate.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblLearningRate;
	}

	private JTextField getTxtLr() {
		if (txtLR == null) {
			txtLR = new JTextField();
			txtLR.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLR.setColumns(10);
		}
		return txtLR;
	}

	private JLabel getLblYArrayFile() {
		if (lblYArrayFile == null) {
			lblYArrayFile = new JLabel("Probability:");
			lblYArrayFile.setHorizontalAlignment(SwingConstants.RIGHT);
			lblYArrayFile.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblYArrayFile;
	}

	private JTextField getTxtNoOfNodes() {
		if (txtNoOfNodes == null) {
			txtNoOfNodes = new JTextField();
			txtNoOfNodes.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtNoOfNodes.setColumns(10);
		}
		return txtNoOfNodes;
	}

	private JButton getBtnTrain() {
		if (btnTrain == null) {
			btnTrain = new JButton("TRAIN");
			btnTrain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String message = validateData();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message,
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						int noOfNodes = Integer.parseInt(txtNoOfNodes.getText());
						String model = Writer.folderName(cmbGraphType
								.getSelectedItem().toString());

						String modelFolder = Reader.jarFile()
								+ "/RandomModels/" + model + "/" + noOfNodes
								+ "nodes";
						if (model.contains("Probability")) {
							double probability = Double.parseDouble(txtProb
									.getText());
							modelFolder += probability + "probability";
						}

						if (checkModel(modelFolder)) {

							int selectedOption = JOptionPane
									.showConfirmDialog(
											mainFrame,

											"Model for "
													+ cmbGraphType
															.getSelectedItem()
															.toString()
													+ " with "
													+ noOfNodes
													+ " nodes already exists. Do you want to replace it?",
											"Question",
											JOptionPane.YES_NO_OPTION);
							if (selectedOption == JOptionPane.YES_OPTION) {
								Reader.deleteFiles(modelFolder);
								train(noOfNodes, modelFolder);
							}
						} else {
							train(noOfNodes, modelFolder);

						}
					}
				}
			});

			Style.buttonStyle(btnTrain);
		}
		return btnTrain;
	}
	
	
	private JTextField getTxtMaxIter() {
		if (txtIter == null) {
			txtIter = new JTextField();
			txtIter.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtIter.setColumns(10);
		}
		return txtIter;
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Max. iterations:");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label;
	}

	private JComboBox<String> getCmbGraphType() {
		if (cmbGraphType == null) {
			cmbGraphType = new JComboBox<String>();
			cmbGraphType.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (cmbGraphType.getSelectedItem().toString()
							.contains("probability")) {
						txtProb.setEnabled(true);
						txtProb.setEditable(true);
					} else {
						txtProb.setEnabled(false);
						txtProb.setEditable(false);
					}
				}
			});
			cmbGraphType.addItem("choose graph type");
			cmbGraphType.addItem("directed graph");
			cmbGraphType.addItem("directed graph with edge probability");
			cmbGraphType.addItem("directed acyclic graph");
			cmbGraphType.addItem("directed graph without direct feedback");
			cmbGraphType.addItem("chain");
			cmbGraphType.addItem("binary tree");
		}
		return cmbGraphType;
	}

	public void train(int noOfNodes, String modelFolder) {

		double alpha = Double.parseDouble(txtAlpha.getText());
		double beta = Double.parseDouble(txtBeta.getText());
		double lr = Double.parseDouble(txtLR.getText());
		int maxIter = Integer.parseInt(txtIter.getText());



		try {
			double[][] s = generateGraph(noOfNodes);
			ProgressBar frame = new ProgressBar(maxIter);
			frame.pack();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			double[] r = ArrayGenerator.generateArray(noOfNodes, 5);
			CalculationsDirGCRF c = new CalculationsDirGCRF(s, r);
			double[] y = c.y(alphaGen, betaGen, 0.05);
			boolean both = false;
			if (chckbxSymmetric.isSelected()) {
				both = true;
			}
			TrainWithRandomForGUI t = new TrainWithRandomForGUI(modelFolder,
					frame, mainFrame, s, r, y, alpha, beta, lr, maxIter,
					panelForTable, both, 10, 10, lblTime, alphaGen, betaGen);
			t.start();
		} catch (OutOfMemoryError e) {

			JOptionPane
			.showMessageDialog(
					mainFrame,
					"Java heap space - Too many nodes, JVM is out of memory.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	public String validateData() {
		if (cmbGraphType.getSelectedIndex() == 0) {
			return "Choose graph type.";
		}
		try {
			Integer.parseInt(txtNoOfNodes.getText());
		} catch (NumberFormatException e) {
			return "No. of nodes should be integer.";
		}
		try {
			Double.parseDouble(txtAlpha.getText());
		} catch (NumberFormatException e) {
			return "First alpha should be number.";
		}
		try {
			Double.parseDouble(txtBeta.getText());
		} catch (NumberFormatException e) {
			return "First beta should be number.";
		}
		try {
			Double.parseDouble(txtLR.getText());
		} catch (NumberFormatException e) {
			return "Learning rate should be number.";
		}

		try {
			Integer.parseInt(txtIter.getText());
		} catch (NumberFormatException e) {
			return "Max. iterations should be integer.";
		}

		if (cmbGraphType.getSelectedItem().toString().contains("probability")) {
			try {
				Double.parseDouble(txtProb.getText());
			} catch (NumberFormatException e) {
				return "Probability should be number.";
			}
		}
		return null;
	}

	public double[][] generateGraph(int noOfNodes) {
		String type = Writer.folderName(cmbGraphType.getSelectedItem()
				.toString());
		double probability = 0;
		if (cmbGraphType.getSelectedItem().toString().contains("probability")) {
			probability = Double.parseDouble(txtProb.getText());
		}
		return GraphGenerator.generateGraphByType(noOfNodes, type, probability);
	}

	private JCheckBox getChckbxSymmetric() {
		if (chckbxSymmetric == null) {
			chckbxSymmetric = new JCheckBox("");
			chckbxSymmetric.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		}
		return chckbxSymmetric;
	}

	private JLabel getLblTrainSymmetric() {
		if (lblTrainSymmetric == null) {
			lblTrainSymmetric = new JLabel("Train symmetric:");
			lblTrainSymmetric.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTrainSymmetric.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblTrainSymmetric;
	}

	public void createMainFolders() {

		String path = Reader.jarFile() + "/RandomModels";
		Writer.createFolder(path);
		for (int i = 1; i < cmbGraphType.getItemCount(); i++) {
			String folder = Writer.folderName(cmbGraphType.getItemAt(i)
					.toString());
			Writer.createFolder(Reader.jarFile() + "/RandomModels/" + folder);
		}
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

	private JLabel getLblTime() {
		if (lblTime == null) {
			lblTime = new JLabel("Time:");
			lblTime.setVisible(false);
			lblTime.setHorizontalAlignment(SwingConstants.LEFT);
			lblTime.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblTime;
	}

	public String readParametersFromCfg() {
		try {
			Map<String, String> params = Reader.readCfg();
			try {
				alphaGen = Integer.parseInt(params.get("AlphaGen").toString());
				betaGen = Integer.parseInt(params.get("BetaGen").toString());
				alpha = Integer.parseInt(params.get("Alpha").toString());
				beta = Integer.parseInt(params.get("Beta").toString());
				lr = Double.parseDouble(params.get("LR").toString());
				iterations = Integer.parseInt(params.get("Iterations")
						.toString());
			} catch (NumberFormatException e) {
				return "Configuration file reading failed. File has wrong format.";
			}
		} catch (ConfigurationParameterseException e) {
			return e.getMessage();
		}
		return null;
	}

	public void setTxtValues() {
		txtAlpha.setText(alpha + "");
		txtBeta.setText(beta + "");
		txtLR.setText(lr + "");
		txtIter.setText(iterations + "");
	}

	private JLabel getLblTrainDirgcrfOn() {
		if (lblTrainDirgcrfOn == null) {
			lblTrainDirgcrfOn = new JLabel("TRAIN AND TEST DirGCRF ON RANDOM NETWORKS:");
			lblTrainDirgcrfOn.setOpaque(true);
			lblTrainDirgcrfOn.setHorizontalAlignment(SwingConstants.CENTER);
			lblTrainDirgcrfOn.setForeground(Color.WHITE);
			lblTrainDirgcrfOn.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblTrainDirgcrfOn.setBackground(Color.GRAY);
		}
		return lblTrainDirgcrfOn;
	}
}
