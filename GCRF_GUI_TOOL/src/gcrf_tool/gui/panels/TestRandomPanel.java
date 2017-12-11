package gcrf_tool.gui.panels;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import gcrf_tool.data.generators.GraphGenerator;
import gcrf_tool.exceptions.ConfigurationParameterseException;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.frames.MainFrame;
import gcrf_tool.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.gui.threads.TestWithRandomForGUI;

import javax.swing.JComboBox;

import java.awt.event.ItemListener;
import java.net.URL;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class TestRandomPanel extends JPanel {

	private static final long serialVersionUID = 4390862740626681899L;
	private JLabel lblType;
	private JLabel lblRArrayFile;
	private JTextField txtNoOfNodes;
	private JButton btnTrain;
	private JComboBox<String> cmbModel;
	private JFrame mainFrame;
	private JLabel label;
	private JTextField txtProb;
	private JLabel lblTimes;
	private JTextField txtTimes;
	private JPanel panelForTable;
	private int alphaGen;
	private int betaGen;
	private JLabel lblTestRandomModels;

	/**
	 * Create the panel.
	 */
	public TestRandomPanel(JFrame mainFrame) {
		if (Reader.checkFile( Reader.jarFile() + "/cfg.txt")) {
			String result = readParametersFromCfg();
			if (result != null) {
				JOptionPane.showMessageDialog(mainFrame,
						result + " Please configure parameters values in Settings->Configuration.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {

				this.mainFrame = mainFrame;
				setBackground(UIManager.getColor("Button.background"));
				GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.columnWidths = new int[]{155, 91, 33, 129, 0, 0, 0, 0};
				gridBagLayout.rowHeights = new int[]{36, 33, 31, 30, 32, 0, 45, 0, 285, 0};
				gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				setLayout(gridBagLayout);
				GridBagConstraints gbc_lblTestRandomModels = new GridBagConstraints();
				gbc_lblTestRandomModels.fill = GridBagConstraints.BOTH;
				gbc_lblTestRandomModels.gridwidth = 15;
				gbc_lblTestRandomModels.insets = new Insets(0, 0, 5, 5);
				gbc_lblTestRandomModels.gridx = 0;
				gbc_lblTestRandomModels.gridy = 0;
				add(getLblTestRandomModels(), gbc_lblTestRandomModels);
				GridBagConstraints gbc_lblType = new GridBagConstraints();
				gbc_lblType.anchor = GridBagConstraints.EAST;
				gbc_lblType.fill = GridBagConstraints.VERTICAL;
				gbc_lblType.insets = new Insets(0, 0, 5, 5);
				gbc_lblType.gridx = 0;
				gbc_lblType.gridy = 1;
				add(getLblType(), gbc_lblType);
				GridBagConstraints gbc_cmbModel = new GridBagConstraints();
				gbc_cmbModel.fill = GridBagConstraints.BOTH;
				gbc_cmbModel.insets = new Insets(0, 0, 5, 5);
				gbc_cmbModel.gridwidth = 3;
				gbc_cmbModel.gridx = 1;
				gbc_cmbModel.gridy = 1;
				add(getCmbModel(), gbc_cmbModel);
				GridBagConstraints gbc_lblRArrayFile = new GridBagConstraints();
				gbc_lblRArrayFile.anchor = GridBagConstraints.EAST;
				gbc_lblRArrayFile.fill = GridBagConstraints.VERTICAL;
				gbc_lblRArrayFile.insets = new Insets(0, 0, 5, 5);
				gbc_lblRArrayFile.gridx = 0;
				gbc_lblRArrayFile.gridy = 2;
				add(getLblRArrayFile(), gbc_lblRArrayFile);
				GridBagConstraints gbc_txtNoOfNodes = new GridBagConstraints();
				gbc_txtNoOfNodes.fill = GridBagConstraints.BOTH;
				gbc_txtNoOfNodes.insets = new Insets(0, 0, 5, 5);
				gbc_txtNoOfNodes.gridx = 1;
				gbc_txtNoOfNodes.gridy = 2;
				add(getTxtNoOfNodes(), gbc_txtNoOfNodes);
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.anchor = GridBagConstraints.EAST;
				gbc_label.fill = GridBagConstraints.VERTICAL;
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 3;
				add(getLabel(), gbc_label);
				GridBagConstraints gbc_txtProb = new GridBagConstraints();
				gbc_txtProb.fill = GridBagConstraints.BOTH;
				gbc_txtProb.insets = new Insets(0, 0, 5, 5);
				gbc_txtProb.gridx = 1;
				gbc_txtProb.gridy = 3;
				add(getTxtProb(), gbc_txtProb);
				GridBagConstraints gbc_lblTimes = new GridBagConstraints();
				gbc_lblTimes.fill = GridBagConstraints.BOTH;
				gbc_lblTimes.insets = new Insets(0, 0, 5, 5);
				gbc_lblTimes.gridx = 0;
				gbc_lblTimes.gridy = 4;
				add(getLblTimes(), gbc_lblTimes);
				GridBagConstraints gbc_txtTimes = new GridBagConstraints();
				gbc_txtTimes.fill = GridBagConstraints.BOTH;
				gbc_txtTimes.insets = new Insets(0, 0, 5, 5);
				gbc_txtTimes.gridx = 1;
				gbc_txtTimes.gridy = 4;
				add(getTxtTimes(), gbc_txtTimes);
				GridBagConstraints gbc_btnTrain = new GridBagConstraints();
				gbc_btnTrain.anchor = GridBagConstraints.WEST;
				gbc_btnTrain.fill = GridBagConstraints.BOTH;
				gbc_btnTrain.insets = new Insets(0, 0, 5, 5);
				gbc_btnTrain.gridx = 3;
				gbc_btnTrain.gridy = 6;
				add(getBtnTrain(), gbc_btnTrain);
				GridBagConstraints gbc_panelForTable = new GridBagConstraints();
				gbc_panelForTable.fill = GridBagConstraints.BOTH;
				gbc_panelForTable.gridwidth = 7;
				gbc_panelForTable.gridx = 1;
				gbc_panelForTable.gridy = 8;
				add(getPanelForTable(), gbc_panelForTable);
			}
		}
	}

	private JLabel getLblType() {
		if (lblType == null) {
			lblType = new JLabel("Model:");
			lblType.setHorizontalAlignment(SwingConstants.RIGHT);
			lblType.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblType;
	}

	private JLabel getLblRArrayFile() {
		if (lblRArrayFile == null) {
			lblRArrayFile = new JLabel("No. of nodes:");
			lblRArrayFile.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRArrayFile.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblRArrayFile;
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
			btnTrain = new JButton("TEST");
			btnTrain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String message = validateData();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						String model = Reader.jarFile()  + "/RandomModels/"
								+ cmbModel.getSelectedItem().toString().replaceAll(" - ", "/");
						int noOfNodes = Integer.parseInt(txtNoOfNodes.getText());
						int times = Integer.parseInt(txtTimes.getText());
						double probability = 0;
						if (cmbModel.getSelectedItem().toString().contains("probability")) {
							probability = Double.parseDouble(txtProb.getText());
						}
						ProgressBar frame = new ProgressBar(times);
						frame.pack();
						frame.setVisible(true);
						frame.setLocationRelativeTo(null);
						TestWithRandomForGUI test = new TestWithRandomForGUI(frame, mainFrame, panelForTable, model,
								noOfNodes, times, probability, alphaGen, betaGen);
						test.start();
					}
				}
			});

			Style.buttonStyle(btnTrain);
		}
		return btnTrain;
	}

	private JComboBox<String> getCmbModel() {
		if (cmbModel == null) {
			cmbModel = new JComboBox<String>();
			cmbModel.addItem("choose model");

			String[] files = Reader.getAllFiles(Reader.jarFile()  + "/RandomModels");
			for (int i = 0; i < files.length; i++) {
				cmbModel.addItem(files[i]);
			}			
			cmbModel.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (cmbModel.getSelectedItem().toString().contains("probability")) {

						String model = Reader.jarFile()  + "/RandomModels/"
								+ cmbModel.getSelectedItem().toString().replaceAll(" - ", "/");
						String probModel = model.split("/")[model.split("/").length - 1];
						probModel = probModel.substring(probModel.indexOf("s") + 1, probModel.indexOf("p"));
						txtProb.setEnabled(true);
						txtProb.setText(probModel);
					}
				}
			});
		}
		return cmbModel;
	}

	public String validateData() {
		if (cmbModel.getSelectedIndex() == 0) {
			return "Choose graph model.";
		}
		try {
			Integer.parseInt(txtNoOfNodes.getText());
		} catch (NumberFormatException e) {
			return "No. of nodes should be integer.";
		}

		try {
			Integer.parseInt(txtTimes.getText());
		} catch (NumberFormatException e) {
			return "No. of graphs should be integer.";
		}

		return null;
	}

	public double[][] generateGraph(int noOfNodes) {
		if (cmbModel.getSelectedIndex() == 1) {
			return GraphGenerator.generateDirectedGraph(noOfNodes);
		} else if (cmbModel.getSelectedIndex() == 2) {
			double prob = Double.parseDouble(txtProb.getText());
			return GraphGenerator.generateDirectedGraphWithEdgeProbability(noOfNodes, prob);
		} else if (cmbModel.getSelectedIndex() == 3) {
			return GraphGenerator.generateDirectedAcyclicGraph(noOfNodes);
		}
		return null;
	}


	public boolean checkModel(String path) {
		return Writer.checkFolder(path);
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Probability:");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label;
	}

	private JTextField getTxtProb() {
		if (txtProb == null) {
			txtProb = new JTextField();
			txtProb.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtProb.setEnabled(false);
			txtProb.setEditable(false);
			txtProb.setColumns(10);
		}
		return txtProb;
	}

	private JLabel getLblTimes() {
		if (lblTimes == null) {
			lblTimes = new JLabel("No. of graphs:");
			lblTimes.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTimes.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblTimes;
	}

	private JTextField getTxtTimes() {
		if (txtTimes == null) {
			txtTimes = new JTextField();
			txtTimes.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtTimes.setColumns(10);
		}
		return txtTimes;
	}

	private JPanel getPanelForTable() {
		if (panelForTable == null) {
			panelForTable = new JPanel();
			panelForTable.setLayout(null);
		}
		return panelForTable;
	}

	public String readParametersFromCfg() {
		try {
			Map<String, String> params = Reader.readCfg();
			try {
				alphaGen = Integer.parseInt(params.get("AlphaGen").toString());
				betaGen = Integer.parseInt(params.get("BetaGen").toString());
			} catch (NumberFormatException e) {
				return "Configuration file reading failed. File has wrong format.";
			}
		} catch (ConfigurationParameterseException e) {
			return e.getMessage();
		}
		return null;
	}
	private JLabel getLblTestRandomModels() {
		if (lblTestRandomModels == null) {
			lblTestRandomModels = new JLabel("TEST RANDOM MODELS:");
			lblTestRandomModels.setOpaque(true);
			lblTestRandomModels.setHorizontalAlignment(SwingConstants.CENTER);
			lblTestRandomModels.setForeground(Color.WHITE);
			lblTestRandomModels.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblTestRandomModels.setBackground(Color.GRAY);
		}
		return lblTestRandomModels;
	}
}