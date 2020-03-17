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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingConstants;

import gcrf_tool.exceptions.ConfigurationParameterseException;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.logic.TrainTestOnNetworks;
import gcrf_tool.gui.style.Style;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComboBox;

import org.jdesktop.swingx.prompt.PromptSupport;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TrainPanel extends JPanel {

	private static final long serialVersionUID = 7542338256284881226L;
	private JLabel lblDataset;
	private JButton btnQuestionDataset;
	private JLabel lblModelName;
	private JTextField txtModelName;
	private JLabel lblAlpha;
	private JLabel lblBeta;
	private JTextField txtAlpha;
	private JTextField txtBeta;
	private JLabel lblLearningRate;
	private JTextField txtLR;
	private JButton btnTrain;
	private JLabel lblMaxIterations;
	private JTextField txtIter;
	private JFileChooser fc;
	private JFrame mainFrame;
	private JCheckBox chckbxStandard;
	private JLabel lblStandard;
	// private JPanel panelForTable;
	// private JLabel lblTime;

	// params

	private int alpha;
	private int beta;
	private double lr;
	private int iterations;
	private int hidden;
	private int iterNN;
	private String matlabPath;
	private boolean useMatlab;
	private long proxy;
	private JLabel lblData;
	private JLabel lblPredictor;
	private JLabel lblModel;
	private JLabel lblPredictor_1;
	private JComboBox<String> cmbPredictor;
	private JButton btnTestPredictr;
	private JLabel lblNoOfHidden;
	private JTextField txtHidden;
	private JLabel lblNoOfIterations;
	private JTextField txtIterNN;
	private JComboBox<String> cmbMethod;
	private JLabel lblMethod;
	private JComboBox<String> cmbDataset;

	private String xPath = "";
	private String yPath = "";
	private String sPath = "";
	private JButton btnQuestionMethods;
	private JButton btnQuestionPredictors;
	private JButton btnQuestionApplyStandard;

	public TrainPanel(JFrame mainFrame) {
		setBounds(new Rectangle(0, 0, 900, 650));
		setMinimumSize(new Dimension(500, 500));
		if (Reader.checkFile(Reader.jarFile() + "/cfg.txt")) {

			String result = readParametersFromCfg();
			if (result != null) {
				JOptionPane.showMessageDialog(mainFrame,
						result + " Please configure parameters values in Settings->Configuration.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {

				setBackground(UIManager.getColor("Button.background"));
				GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.columnWidths = new int[] { 169, 3, 227, 2, 43, 112, 266, 0 };
				gridBagLayout.rowHeights = new int[] { 30, 33, 33, 30, 50, 30, 30, 31, 38, 30, 30, 0, 0, 0, 0, 0, 0, 0,
						0, 30, 0, 0 };
				gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
				gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
						0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
				setLayout(gridBagLayout);
				GridBagConstraints gbc_lblData = new GridBagConstraints();
				gbc_lblData.fill = GridBagConstraints.BOTH;
				gbc_lblData.insets = new Insets(0, 0, 5, 0);
				gbc_lblData.gridwidth = 10;
				gbc_lblData.gridx = 0;
				gbc_lblData.gridy = 0;
				add(getLblData(), gbc_lblData);
				GridBagConstraints gbc_lblDataset = new GridBagConstraints();
				gbc_lblDataset.fill = GridBagConstraints.BOTH;
				gbc_lblDataset.insets = new Insets(0, 0, 5, 5);
				gbc_lblDataset.gridx = 0;
				gbc_lblDataset.gridy = 2;
				add(getLblDataset(), gbc_lblDataset);

				GridBagConstraints gbc_cmbDataset = new GridBagConstraints();
				gbc_cmbDataset.fill = GridBagConstraints.BOTH;
				gbc_cmbDataset.insets = new Insets(0, 0, 5, 5);
				gbc_cmbDataset.gridwidth = 4;
				gbc_cmbDataset.gridx = 1;
				gbc_cmbDataset.gridy = 2;
				add(getCmbDataset(), gbc_cmbDataset);
				GridBagConstraints gbc_btnQuestionDataset = new GridBagConstraints();
				gbc_btnQuestionDataset.anchor = GridBagConstraints.WEST;
				gbc_btnQuestionDataset.insets = new Insets(0, 0, 5, 5);
				gbc_btnQuestionDataset.gridx = 5;
				gbc_btnQuestionDataset.gridy = 2;
				GridBagConstraints gbc_btnQuestionPredictors = new GridBagConstraints();
				gbc_btnQuestionPredictors.anchor = GridBagConstraints.WEST;
				gbc_btnQuestionPredictors.insets = new Insets(0, 0, 5, 5);
				gbc_btnQuestionPredictors.gridx = 5;
				gbc_btnQuestionPredictors.gridy = 6;
				add(getBtnQuestionPredictors(), gbc_btnQuestionPredictors);
				GridBagConstraints gbc_btnTestPredictr = new GridBagConstraints();
				gbc_btnTestPredictr.anchor = GridBagConstraints.NORTHWEST;
				gbc_btnTestPredictr.insets = new Insets(0, 0, 5, 5);
				gbc_btnTestPredictr.gridx = 6;
				gbc_btnTestPredictr.gridy = 6;
				add(getBtnTestPredictr(), gbc_btnTestPredictr);
				GridBagConstraints gbc_lblNoOfHidden = new GridBagConstraints();
				gbc_lblNoOfHidden.anchor = GridBagConstraints.EAST;
				gbc_lblNoOfHidden.fill = GridBagConstraints.VERTICAL;
				gbc_lblNoOfHidden.insets = new Insets(0, 0, 5, 5);
				gbc_lblNoOfHidden.gridwidth = 2;
				gbc_lblNoOfHidden.gridx = 0;
				gbc_lblNoOfHidden.gridy = 7;
				add(getLblNoOfHidden(), gbc_lblNoOfHidden);
				add(getBtnQuestionDataset(), gbc_btnQuestionDataset);
				GridBagConstraints gbc_lblModelName = new GridBagConstraints();
				gbc_lblModelName.anchor = GridBagConstraints.EAST;
				gbc_lblModelName.fill = GridBagConstraints.VERTICAL;
				gbc_lblModelName.insets = new Insets(0, 0, 5, 5);
				gbc_lblModelName.gridx = 0;
				gbc_lblModelName.gridy = 3;
				add(getLblModelName(), gbc_lblModelName);
				GridBagConstraints gbc_txtModelName = new GridBagConstraints();
				gbc_txtModelName.fill = GridBagConstraints.BOTH;
				gbc_txtModelName.insets = new Insets(0, 0, 5, 5);
				gbc_txtModelName.gridwidth = 4;
				gbc_txtModelName.gridx = 1;
				gbc_txtModelName.gridy = 3;
				add(getTxtModelName(), gbc_txtModelName);
				GridBagConstraints gbc_lblPredictor = new GridBagConstraints();
				gbc_lblPredictor.fill = GridBagConstraints.BOTH;
				gbc_lblPredictor.insets = new Insets(0, 0, 5, 0);
				gbc_lblPredictor.gridwidth = 10;
				gbc_lblPredictor.gridx = 0;
				gbc_lblPredictor.gridy = 5;
				add(getLblPredictor(), gbc_lblPredictor);
				GridBagConstraints gbc_lblPredictor_1 = new GridBagConstraints();
				gbc_lblPredictor_1.fill = GridBagConstraints.BOTH;
				gbc_lblPredictor_1.insets = new Insets(0, 0, 5, 5);
				gbc_lblPredictor_1.gridwidth = 2;
				gbc_lblPredictor_1.gridx = 0;
				gbc_lblPredictor_1.gridy = 6;
				add(getLblPredictor_1(), gbc_lblPredictor_1);

				GridBagConstraints gbc_txtHidden = new GridBagConstraints();
				gbc_txtHidden.anchor = GridBagConstraints.WEST;
				gbc_txtHidden.fill = GridBagConstraints.BOTH;
				gbc_txtHidden.insets = new Insets(0, 0, 5, 5);
				gbc_txtHidden.gridx = 2;
				gbc_txtHidden.gridy = 7;
				add(getTxtHidden(), gbc_txtHidden);
				GridBagConstraints gbc_lblNoOfIterations = new GridBagConstraints();
				gbc_lblNoOfIterations.gridwidth = 2;
				gbc_lblNoOfIterations.anchor = GridBagConstraints.EAST;
				gbc_lblNoOfIterations.fill = GridBagConstraints.VERTICAL;
				gbc_lblNoOfIterations.insets = new Insets(0, 0, 5, 5);
				gbc_lblNoOfIterations.gridx = 0;
				gbc_lblNoOfIterations.gridy = 8;
				add(getLblNoOfIterations(), gbc_lblNoOfIterations);
				GridBagConstraints gbc_txtIterNN = new GridBagConstraints();
				gbc_txtIterNN.fill = GridBagConstraints.BOTH;
				gbc_txtIterNN.anchor = GridBagConstraints.SOUTHWEST;
				gbc_txtIterNN.insets = new Insets(0, 0, 5, 5);
				gbc_txtIterNN.gridx = 2;
				gbc_txtIterNN.gridy = 8;
				add(getTxtIterNN(), gbc_txtIterNN);
				GridBagConstraints gbc_lblModel = new GridBagConstraints();
				gbc_lblModel.anchor = GridBagConstraints.NORTH;
				gbc_lblModel.fill = GridBagConstraints.BOTH;
				gbc_lblModel.insets = new Insets(0, 0, 5, 0);
				gbc_lblModel.gridwidth = 10;
				gbc_lblModel.gridx = 0;
				gbc_lblModel.gridy = 11;
				add(getLblModel(), gbc_lblModel);

				GridBagConstraints gbc_lblMethod = new GridBagConstraints();
				gbc_lblMethod.gridwidth = 2;
				gbc_lblMethod.fill = GridBagConstraints.BOTH;
				gbc_lblMethod.insets = new Insets(0, 0, 5, 5);
				gbc_lblMethod.gridx = 0;
				gbc_lblMethod.gridy = 12;
				add(getLblMethod(), gbc_lblMethod);
				GridBagConstraints gbc_cmbMethod = new GridBagConstraints();
				gbc_cmbMethod.gridwidth = 3;
				gbc_cmbMethod.anchor = GridBagConstraints.NORTH;
				gbc_cmbMethod.fill = GridBagConstraints.BOTH;
				gbc_cmbMethod.insets = new Insets(0, 0, 5, 5);
				gbc_cmbMethod.gridx = 2;
				gbc_cmbMethod.gridy = 12;
				add(getCmbMethod(), gbc_cmbMethod);
				GridBagConstraints gbc_btnQuestionMethods = new GridBagConstraints();
				gbc_btnQuestionMethods.anchor = GridBagConstraints.WEST;
				gbc_btnQuestionMethods.insets = new Insets(0, 0, 5, 5);
				gbc_btnQuestionMethods.gridx = 5;
				gbc_btnQuestionMethods.gridy = 12;
				add(getBtnQuestionMethods(), gbc_btnQuestionMethods);
				GridBagConstraints gbc_lblAlpha = new GridBagConstraints();
				gbc_lblAlpha.gridwidth = 2;
				gbc_lblAlpha.fill = GridBagConstraints.BOTH;
				gbc_lblAlpha.insets = new Insets(0, 0, 5, 5);
				gbc_lblAlpha.gridx = 0;
				gbc_lblAlpha.gridy = 13;
				add(getLblAlpha(), gbc_lblAlpha);
				GridBagConstraints gbc_txtAlpha = new GridBagConstraints();
				gbc_txtAlpha.anchor = GridBagConstraints.WEST;
				gbc_txtAlpha.fill = GridBagConstraints.BOTH;
				gbc_txtAlpha.insets = new Insets(0, 0, 5, 5);
				gbc_txtAlpha.gridx = 2;
				gbc_txtAlpha.gridy = 13;
				add(getTxtAlpha(), gbc_txtAlpha);
				GridBagConstraints gbc_lblBeta = new GridBagConstraints();
				gbc_lblBeta.gridwidth = 2;
				gbc_lblBeta.fill = GridBagConstraints.BOTH;
				gbc_lblBeta.insets = new Insets(0, 0, 5, 5);
				gbc_lblBeta.gridx = 0;
				gbc_lblBeta.gridy = 14;
				add(getLblBeta(), gbc_lblBeta);
				GridBagConstraints gbc_txtBeta = new GridBagConstraints();
				gbc_txtBeta.fill = GridBagConstraints.BOTH;
				gbc_txtBeta.anchor = GridBagConstraints.WEST;
				gbc_txtBeta.insets = new Insets(0, 0, 5, 5);
				gbc_txtBeta.gridx = 2;
				gbc_txtBeta.gridy = 14;
				add(getTxtBeta(), gbc_txtBeta);
				GridBagConstraints gbc_lblLearningRate = new GridBagConstraints();
				gbc_lblLearningRate.gridwidth = 2;
				gbc_lblLearningRate.anchor = GridBagConstraints.EAST;
				gbc_lblLearningRate.fill = GridBagConstraints.VERTICAL;
				gbc_lblLearningRate.insets = new Insets(0, 0, 5, 5);
				gbc_lblLearningRate.gridx = 0;
				gbc_lblLearningRate.gridy = 15;
				add(getLblLearningRate(), gbc_lblLearningRate);
				GridBagConstraints gbc_txtLR = new GridBagConstraints();
				gbc_txtLR.anchor = GridBagConstraints.WEST;
				gbc_txtLR.fill = GridBagConstraints.BOTH;
				gbc_txtLR.insets = new Insets(0, 0, 5, 5);
				gbc_txtLR.gridx = 2;
				gbc_txtLR.gridy = 15;
				add(getTxtLr(), gbc_txtLR);
				GridBagConstraints gbc_lblMaxIterations = new GridBagConstraints();
				gbc_lblMaxIterations.anchor = GridBagConstraints.EAST;
				gbc_lblMaxIterations.fill = GridBagConstraints.VERTICAL;
				gbc_lblMaxIterations.insets = new Insets(0, 0, 5, 5);
				gbc_lblMaxIterations.gridwidth = 2;
				gbc_lblMaxIterations.gridx = 0;
				gbc_lblMaxIterations.gridy = 16;
				add(getLblMaxIterations(), gbc_lblMaxIterations);
				GridBagConstraints gbc_txtIter = new GridBagConstraints();
				gbc_txtIter.anchor = GridBagConstraints.WEST;
				gbc_txtIter.fill = GridBagConstraints.BOTH;
				gbc_txtIter.insets = new Insets(0, 0, 5, 5);
				gbc_txtIter.gridx = 2;
				gbc_txtIter.gridy = 16;
				add(getTxtMaxIter(), gbc_txtIter);
				GridBagConstraints gbc_lblStandard = new GridBagConstraints();
				gbc_lblStandard.fill = GridBagConstraints.BOTH;
				gbc_lblStandard.insets = new Insets(0, 0, 5, 5);
				gbc_lblStandard.gridwidth = 2;
				gbc_lblStandard.gridx = 0;
				gbc_lblStandard.gridy = 17;
				add(getLblStandard(), gbc_lblStandard);
				GridBagConstraints gbc_chckbxStandard = new GridBagConstraints();
				gbc_chckbxStandard.anchor = GridBagConstraints.SOUTHWEST;
				gbc_chckbxStandard.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxStandard.gridx = 2;
				gbc_chckbxStandard.gridy = 17;
				add(getChckbxStandard(), gbc_chckbxStandard);
				GridBagConstraints gbc_cmbPredictor = new GridBagConstraints();
				gbc_cmbPredictor.gridwidth = 3;
				gbc_cmbPredictor.fill = GridBagConstraints.BOTH;
				gbc_cmbPredictor.insets = new Insets(0, 0, 5, 5);
				gbc_cmbPredictor.gridx = 2;
				gbc_cmbPredictor.gridy = 6;
				add(getCmbPredictor(), gbc_cmbPredictor);
				fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				fc.setFileFilter(filter);
				fc.setCurrentDirectory(Reader.jarFile());

				this.mainFrame = mainFrame;
				GridBagConstraints gbc_btnQuestionApplyStandard = new GridBagConstraints();
				gbc_btnQuestionApplyStandard.anchor = GridBagConstraints.WEST;
				gbc_btnQuestionApplyStandard.insets = new Insets(0, 0, 5, 5);
				gbc_btnQuestionApplyStandard.gridx = 5;
				gbc_btnQuestionApplyStandard.gridy = 17;
				add(getBtnQuestionApplyStandard(), gbc_btnQuestionApplyStandard);
				GridBagConstraints gbc_btnTrain = new GridBagConstraints();
				gbc_btnTrain.insets = new Insets(0, 0, 5, 5);
				gbc_btnTrain.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnTrain.anchor = GridBagConstraints.NORTHWEST;
				gbc_btnTrain.gridx = 2;
				gbc_btnTrain.gridy = 19;
				add(getBtnTrain(), gbc_btnTrain);
				setTxtValues();

			}
		} else {
			JOptionPane.showMessageDialog(mainFrame, "Please configure parameters values in Settings->Configuration.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JLabel getLblDataset() {
		if (lblDataset == null) {
			lblDataset = new JLabel("Dataset:");
			lblDataset.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDataset.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblDataset;
	}

	private JButton getBtnQuestionDataset() {
		if (btnQuestionDataset == null) {
			btnQuestionDataset = new JButton("");
			Style.questionButtonStyle(btnQuestionDataset);
			btnQuestionDataset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane.showMessageDialog(mainFrame,
							"Dataset samples are provided by default."
									+ "\nUse samples or add your own dataset in Datasets -> Add dataset menu item."
									+ "\nInformation for datasets samples can be found in Help -> Datasets menu item.",
							"Help", JOptionPane.QUESTION_MESSAGE, Style.questionIcon());
				}
			});
		}
		return btnQuestionDataset;
	}

	private JLabel getLblModelName() {
		if (lblModelName == null) {
			lblModelName = new JLabel("Model name:");
			lblModelName.setHorizontalAlignment(SwingConstants.RIGHT);
			lblModelName.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblModelName;
	}

	private JTextField getTxtModelName() {
		if (txtModelName == null) {
			txtModelName = new JTextField();
			txtModelName.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtModelName.setColumns(10);
			PromptSupport.setPrompt("Insert the name of your model", txtModelName);
		}
		return txtModelName;
	}

	private JLabel getLblAlpha() {
		if (lblAlpha == null) {
			lblAlpha = new JLabel("First alpha:");
			lblAlpha.setVisible(false);
			lblAlpha.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAlpha.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblAlpha;
	}

	private JLabel getLblBeta() {
		if (lblBeta == null) {
			lblBeta = new JLabel("First beta:");
			lblBeta.setVisible(false);
			lblBeta.setHorizontalAlignment(SwingConstants.RIGHT);
			lblBeta.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblBeta;
	}

	private JTextField getTxtAlpha() {
		if (txtAlpha == null) {
			txtAlpha = new JTextField();
			txtAlpha.setVisible(false);
			txtAlpha.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtAlpha.setColumns(10);
		}
		return txtAlpha;
	}

	private JTextField getTxtBeta() {
		if (txtBeta == null) {
			txtBeta = new JTextField();
			txtBeta.setVisible(false);
			txtBeta.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtBeta.setColumns(10);
		}
		return txtBeta;
	}

	private JLabel getLblLearningRate() {
		if (lblLearningRate == null) {
			lblLearningRate = new JLabel("Learning rate:");
			lblLearningRate.setVisible(false);
			lblLearningRate.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLearningRate.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblLearningRate;
	}

	private JTextField getTxtLr() {
		if (txtLR == null) {
			txtLR = new JTextField();
			txtLR.setVisible(false);
			txtLR.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLR.setColumns(10);
		}
		return txtLR;
	}

	private JButton getBtnTrain() {
		if (btnTrain == null) {
			btnTrain = new JButton("TRAIN and TEST");
			btnTrain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String message = train();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				
				}

			});
			Style.buttonStyle(btnTrain);
		}
		return btnTrain;

	}

	private String createFolderAndSaveData(String method) {
		File matrixFile = new File(xPath);
		File xFile = new File(yPath);
		File yFile = new File(sPath);

		Writer.createFolder(Reader.jarFile() + "/MyModels" + method);
		String path = Reader.jarFile() + "/MyModels" + method + "/" + txtModelName.getText();
		Writer.createFolder(path);

		String dataPath = Reader.jarFile() + "/MyModels" + method + "/" + txtModelName.getText() + "/data";
		Writer.createFolder(dataPath);
		Writer.copyFile(matrixFile, dataPath + "/matrix.txt");
		Writer.copyFile(xFile, dataPath + "/x.txt");

		Writer.copyFile(yFile, dataPath + "/y.txt");
		Writer.createFolder(path);
		return path;
	}

	private JLabel getLblMaxIterations() {
		if (lblMaxIterations == null) {
			lblMaxIterations = new JLabel("Max. iterations:");
			lblMaxIterations.setVisible(false);
			lblMaxIterations.setHorizontalAlignment(SwingConstants.RIGHT);
			lblMaxIterations.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblMaxIterations;
	}

	public String validateData() {

		if (cmbDataset.getSelectedIndex() == 0) {
			return "Choose dataset.";
		}
		if (txtModelName.getText().equals("")) {
			return "Insert model name.";
		}

		if (cmbPredictor.getSelectedIndex() == 0) {
			return "Choose predictor.";
		}
		if (cmbMethod.getSelectedIndex() == 0) {
			return "Choose method.";
		}
		String method = cmbMethod.getSelectedItem().toString();

		if (Writer.checkFolder(Reader.jarFile() + "/MyModels" + method + "/" + txtModelName.getText())) {
			return "Model with name " + txtModelName.getText() + " already exists.";
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
		if (cmbPredictor.getSelectedItem().toString().contains("neural")) {

			try {
				Integer.parseInt(txtHidden.getText());
			} catch (NumberFormatException e) {
				return "No. of hidden neurons should be integer.";
			}

			try {
				Integer.parseInt(txtIterNN.getText());
			} catch (NumberFormatException e) {
				return "No. of iterations should be integer.";
			}
		}
		return null;
	}

	public String validateDataForTestPredictor() {
		if (cmbDataset.getSelectedIndex() == 0) {
			return "Choose dataset.";
		}
		if (cmbPredictor.getSelectedIndex() == 0) {
			return "Choose predictor.";
		}
		if (cmbPredictor.getSelectedItem().toString().contains("neural")) {

			try {
				Integer.parseInt(txtHidden.getText());
			} catch (NumberFormatException e) {
				return "No. of hidden neurons should be integer.";
			}

			try {
				Integer.parseInt(txtIterNN.getText());
			} catch (NumberFormatException e) {
				return "No. of iterations should be integer.";
			}
		}

		return null;
	}

	private JTextField getTxtMaxIter() {
		if (txtIter == null) {
			txtIter = new JTextField();
			txtIter.setVisible(false);
			txtIter.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtIter.setColumns(10);
		}
		return txtIter;
	}

	private JCheckBox getChckbxStandard() {
		if (chckbxStandard == null) {
			chckbxStandard = new JCheckBox("");
			chckbxStandard.setVisible(false);
			chckbxStandard.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		}
		return chckbxStandard;
	}

	private JLabel getLblStandard() {
		if (lblStandard == null) {
			lblStandard = new JLabel("Apply standard GCRF:");
			lblStandard.setVisible(false);
			lblStandard.setHorizontalAlignment(SwingConstants.RIGHT);
			lblStandard.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblStandard;
	}

	// private JPanel getPanelForTable() {
	// if (panelForTable == null) {
	// panelForTable = new JPanel();
	// panelForTable.setLayout(null);
	// panelForTable.setBounds(35, 496, 850, 114);
	// }
	// return panelForTable;
	// }

	// private JLabel getLblTime() {
	// if (lblTime == null) {
	// lblTime = new JLabel("Time:");
	// lblTime.setVisible(false);
	// lblTime.setHorizontalAlignment(SwingConstants.LEFT);
	// lblTime.setFont(new Font("Segoe UI", Font.BOLD, 15));
	// lblTime.setBounds(35, 610, 421, 30);
	// }
	// return lblTime;
	// }

	public String readParametersFromCfg() {
		try {
			Map<String, String> params = Reader.readCfg();
			if (params == null) {
				return "Configuration file reading failed";
			}

			try {
				alpha = Integer.parseInt(params.get("Alpha").toString());
				beta = Integer.parseInt(params.get("Beta").toString());
				lr = Double.parseDouble(params.get("LR").toString());
				iterations = Integer.parseInt(params.get("Iterations").toString());
				hidden = Integer.parseInt(params.get("NN hidden").toString());
				iterNN = Integer.parseInt(params.get("Iterations NN").toString());
				proxy = Integer.parseInt(params.get("Proxy").toString());
			} catch (NumberFormatException e) {
				return "Configuration file reading failed. File has wrong format.";
			}
			if (params.get("Use MATLAB").toString().contains("true")) {
				useMatlab = true;
			} else {
				useMatlab = false;
			}
			matlabPath = params.get("Path").toString();
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
		txtIterNN.setText(iterNN + "");
		txtHidden.setText(hidden + "");
	}

	private JLabel getLblData() {
		if (lblData == null) {
			lblData = new JLabel("DATA:");
			lblData.setForeground(Color.WHITE);
			lblData.setBackground(Color.GRAY);
			lblData.setHorizontalAlignment(SwingConstants.CENTER);
			lblData.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblData.setOpaque(true);
		}
		return lblData;
	}

	private JLabel getLblPredictor() {
		if (lblPredictor == null) {
			lblPredictor = new JLabel("UNSTRUCTURED PREDICTOR:");
			lblPredictor.setOpaque(true);
			lblPredictor.setHorizontalAlignment(SwingConstants.CENTER);
			lblPredictor.setForeground(Color.WHITE);
			lblPredictor.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblPredictor.setBackground(Color.GRAY);
		}
		return lblPredictor;
	}

	private JLabel getLblModel() {
		if (lblModel == null) {
			lblModel = new JLabel("METHOD:");
			lblModel.setOpaque(true);
			lblModel.setHorizontalAlignment(SwingConstants.CENTER);
			lblModel.setForeground(Color.WHITE);
			lblModel.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblModel.setBackground(Color.GRAY);
		}
		return lblModel;
	}

	private JLabel getLblPredictor_1() {
		if (lblPredictor_1 == null) {
			lblPredictor_1 = new JLabel("Unstructured predictor:");
			lblPredictor_1.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPredictor_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblPredictor_1;
	}

	private JComboBox<String> getCmbPredictor() {
		if (cmbPredictor == null) {
			cmbPredictor = new JComboBox<String>();
			cmbPredictor.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (cmbPredictor.getSelectedItem().toString().contains("neural")) {
						lblNoOfHidden.setVisible(true);
						txtHidden.setVisible(true);
						lblNoOfIterations.setVisible(true);
						txtIterNN.setVisible(true);
					} else {
						lblNoOfHidden.setVisible(false);
						txtHidden.setVisible(false);
						lblNoOfIterations.setVisible(false);
						txtIterNN.setVisible(false);
					}
				}
			});
			cmbPredictor.addItem("choose predictor");
			cmbPredictor.addItem("neural network");
			cmbPredictor.addItem("linear regression");
		}
		return cmbPredictor;
	}

	private JButton getBtnTestPredictr() {
		if (btnTestPredictr == null) {
			btnTestPredictr = new JButton("Test predictor");
			Style.buttonStyle(btnTestPredictr);
			btnTestPredictr.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String message = validateDataForTestPredictor();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
					} else {

						String mainPath = Reader.jarFile() + "/Datasets/Networks/";

						xPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/xTrain.txt";
						yPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/yTrain.txt";
						sPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/sTrain.txt";

						String readme = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/readme.txt";
						int noOfNodes = 0;
						try {
							String nodesTrain = Reader.read(readme)[0];
							noOfNodes = Integer.parseInt(nodesTrain.substring(nodesTrain.indexOf(":") + 2));
							if (noOfNodes <= 0) {
								JOptionPane.showMessageDialog(mainFrame, "No. of nodes should be greater than 0.",
										"Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(mainFrame, "Reading dataset error.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						String[] x = Reader.read(xPath);
						double[] y = Reader.readArray(yPath, noOfNodes);

						double result = TrainTestOnNetworks.callPredictorTrain(
								cmbPredictor.getSelectedItem().toString(), null, x, y, txtHidden.getText(),
								txtIterNN.getText());
						if (result == -7000) {
							JOptionPane.showMessageDialog(mainFrame, "Unknown predictor.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (result == -3000) {
							JOptionPane.showMessageDialog(mainFrame,
									cmbPredictor.getSelectedItem().toString()
											+ " cannot be applied to your data. Choose different predictor.",
									"Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (result == -5000) {
							JOptionPane.showMessageDialog(mainFrame, "File with attributes is not in correct format.",
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							DecimalFormat df = new DecimalFormat("#.####");
							JOptionPane.showMessageDialog(
									mainFrame, "Testing with same data\nR^2 value for "
											+ cmbPredictor.getSelectedItem().toString() + " is: " + df.format(result),
									"Results", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			});
		}
		return btnTestPredictr;
	}

	private JLabel getLblNoOfHidden() {
		if (lblNoOfHidden == null) {
			lblNoOfHidden = new JLabel("No. of hidden neurons:");
			lblNoOfHidden.setVisible(false);
			lblNoOfHidden.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNoOfHidden.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblNoOfHidden;
	}

	private JTextField getTxtHidden() {
		if (txtHidden == null) {
			txtHidden = new JTextField();
			txtHidden.setVisible(false);
			txtHidden.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtHidden.setColumns(10);
		}
		return txtHidden;
	}

	private JLabel getLblNoOfIterations() {
		if (lblNoOfIterations == null) {
			lblNoOfIterations = new JLabel("No. of iterations:");
			lblNoOfIterations.setVisible(false);
			lblNoOfIterations.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNoOfIterations.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblNoOfIterations;
	}

	private JTextField getTxtIterNN() {
		if (txtIterNN == null) {
			txtIterNN = new JTextField();
			txtIterNN.setVisible(false);
			txtIterNN.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtIterNN.setColumns(10);
		}
		return txtIterNN;
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
			cmbMethod.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					String method = cmbMethod.getSelectedItem().toString();
					if (method.contains("Dir")) {
						lblStandard.setVisible(true);
						btnQuestionApplyStandard.setVisible(true);
						chckbxStandard.setVisible(true);
					} else {
						lblStandard.setVisible(false);
						chckbxStandard.setVisible(false);
						btnQuestionApplyStandard.setVisible(false);
					}
					if (method.equalsIgnoreCase("DirGCRF") || method.equalsIgnoreCase("GCRF")) {
						showGradientDescentParams();
					} else {
						hideGradientDescentParams();
					}
				}
			});
		}
		return cmbMethod;
	}

	public void showGradientDescentParams() {
		lblAlpha.setVisible(true);
		txtAlpha.setVisible(true);
		lblBeta.setVisible(true);
		txtBeta.setVisible(true);
		lblLearningRate.setVisible(true);
		txtLR.setVisible(true);
		lblMaxIterations.setVisible(true);
		txtIter.setVisible(true);
	}

	public void hideGradientDescentParams() {
		lblAlpha.setVisible(false);
		txtAlpha.setVisible(false);
		lblBeta.setVisible(false);
		txtBeta.setVisible(false);
		lblLearningRate.setVisible(false);
		txtLR.setVisible(false);
		lblMaxIterations.setVisible(false);
		txtIter.setVisible(false);
	}

	private JLabel getLblMethod() {
		if (lblMethod == null) {
			lblMethod = new JLabel("Method:");
			lblMethod.setHorizontalAlignment(SwingConstants.RIGHT);
			lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblMethod;
	}

	private JComboBox<String> getCmbDataset() {
		if (cmbDataset == null) {
			cmbDataset = new JComboBox<String>();
			cmbDataset.addItem("choose dataset");
			String[] files = Reader.getAllFolders(Reader.jarFile() + "/Datasets/Networks");
			for (int i = 0; i < files.length; i++) {
				cmbDataset.addItem(files[i]);
			}
		}
		return cmbDataset;
	}

	private JButton getBtnQuestionMethods() {
		if (btnQuestionMethods == null) {
			btnQuestionMethods = new JButton("");
			Style.questionButtonStyle(btnQuestionMethods);
			btnQuestionMethods.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String text = "Standard GCRF - incorporates the outputs of unstructured predictors and the correlation between output variables. "
							+ "\nDirected GCRF (DirGCRF) – extends the GCRF to allow modeling asymmetric relationships (directed graphs).";

					if (useMatlab) {
						text += "\nUnimodal GCRF (UmGCRF) – method that extends the GCRF parameter space to include negative values. ";
					}

					JOptionPane.showMessageDialog(mainFrame, text, "Help", JOptionPane.QUESTION_MESSAGE,
							Style.questionIcon());
				}
			});
		}
		return btnQuestionMethods;
	}

	private JButton getBtnQuestionPredictors() {
		if (btnQuestionPredictors == null) {
			btnQuestionPredictors = new JButton("");
			Style.questionButtonStyle(btnQuestionPredictors);
			btnQuestionPredictors.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane.showMessageDialog(mainFrame,
							"Neural network - Neurons in an artificial neural network are grouped in three layers: input, output, and hidden layer. "
									+ "\nThe number of neurons in the input layer is same as the number of attributes in the chosen dataset. "
									+ "\nThe number of neurons in the output layer is 1 (all datasets have one predicted variable). "
									+ "\nYou have to insert the number of neurons"
									+ " in the hidden layer and number of iterations for training."
									+ "\n\nLinear Regression - Approach for modeling the relationship between a dependent variable y and one or more explanatory variables x. "
									+ "\nThis relationship is modeled using linear predictor functions whose "
									+ " parameters are estimated from the data. "
									+ "\nStandard or multivariate linear regression is used depending on the number of attributes in the dataset.",
							"Help", JOptionPane.QUESTION_MESSAGE, Style.questionIcon());
				}
			});
		}
		return btnQuestionPredictors;
	}

	private JButton getBtnQuestionApplyStandard() {
		if (btnQuestionApplyStandard == null) {
			btnQuestionApplyStandard = new JButton("");
			Style.questionButtonStyle(btnQuestionApplyStandard);
			btnQuestionApplyStandard.setVisible(false);
			btnQuestionApplyStandard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane.showMessageDialog(mainFrame,
							"If you check this option the similarity matrix will be automatically converted from asymmetric to symmetric in order to apply standard GCRF and get its accuracy for comparison.",
							"Help", JOptionPane.QUESTION_MESSAGE, Style.questionIcon());
				}
			});
		}
		return btnQuestionApplyStandard;
	}

	private String train() {
		String validation = validateData();
		if (validation != null) {
			return validation;
		} else {

			String mainPath = Reader.jarFile() + "/Datasets/Networks/";

			xPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/xTrain.txt";
			yPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/yTrain.txt";
			sPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/sTrain.txt";

			String readme = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/readme.txt";
			int noOfNodes = 0;
			try {
				String nodesTrain = Reader.read(readme)[0];
				noOfNodes = Integer.parseInt(nodesTrain.substring(nodesTrain.indexOf(":") + 2));
				if (noOfNodes <= 0) {
					return "No. of nodes should be greater than 0.";
				}
			} catch (NumberFormatException e1) {
				return "Reading dataset error.";
			}

			String method = cmbMethod.getSelectedItem().toString();

			double alpha = Double.parseDouble(txtAlpha.getText());
			double beta = Double.parseDouble(txtBeta.getText());
			double lr = Double.parseDouble(txtLR.getText());
			int maxIter = Integer.parseInt(txtIter.getText());

			String[] x = Reader.read(xPath);
			double[] y = Reader.readArray(yPath, noOfNodes);
			double[][] s = Reader.readGraph(sPath, noOfNodes);

			String path = createFolderAndSaveData(method);
			String message = null;
			double result = TrainTestOnNetworks.callPredictorTrain(cmbPredictor.getSelectedItem().toString(), path, x,
					y, txtHidden.getText(), txtIterNN.getText());

			if (result == -7000) {
				message = "Unknown predictor.";
			}

			if (result == -3000) {
				message = cmbPredictor.getSelectedItem().toString()
						+ " cannot be applied to your data. Choose different predictor.";
			}
			if (result == -5000) {
				message = "File with attributes is not in correct format.";
			}
			
			// test data
			

			String xTestPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/xTest.txt";
			String yTestPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/yTest.txt";
			String sTestPath = mainPath + "/" + cmbDataset.getSelectedItem().toString() + "/sTest.txt";

			int noOfNodesTest = 0;
			try {
				String nodesTest = Reader.read(readme)[1];
				noOfNodesTest = Integer.parseInt(nodesTest.substring(nodesTest.indexOf(":") + 2));
				if (noOfNodesTest <= 0) {
					return "No. of nodes should be greater than 0.";
				}
			} catch (NumberFormatException e1) {
				return "Reading dataset error.";
			}

			Writer.createFolder(Reader.jarFile() + "/MyModels" + method);
			String dataPath = Reader.jarFile() + "/MyModels" + method + "/" + txtModelName.getText();
			File matrixFile = new File(sTestPath);
			Writer.copyFile(matrixFile, dataPath + "/data/matrixTest.txt");
			File xFile = new File(xTestPath);
			Writer.copyFile(xFile, dataPath + "/data/xTest.txt");
			File yFile = new File(yTestPath);
			Writer.copyFile(yFile, dataPath + "/data/yTest.txt");

			String[] xTest = Reader.read(xTestPath);
			if (xTest == null) {
				return "Reading file error.";
			}

			if (xTest.length != noOfNodesTest) {
				return "Number of lines in the file with attributes should be " + noOfNodesTest + ".";
			}
			double[] yTest = Reader.readArray(yTestPath, noOfNodesTest);
			if (yTest == null) {
				return "Number of lines in in the file with outputs should be " + noOfNodesTest + ".";
			}
			double resultTest = TrainTestOnNetworks.callPredictorTest(dataPath, xTest, yTest);
			if (resultTest == -9000) {
				return "Selected dataset is not suitable for this model.";
			}
			if (resultTest == -7000) {
				return "Unknown predictor.";
			}
			if (resultTest == -3000) {
				return "Predictor cannot be applied to your data. Choose different predictor.";
			}
			if (resultTest == -5000) {
				return "File with attributes is not in correct format.";
			}

			double[] rTest = Reader.readArray(dataPath + "/data/rTest.txt", noOfNodesTest);
			double[][] sTest = Reader.readGraph(sTestPath, noOfNodesTest);

			if (message == null) {

				double[] r = Reader.readArray(path + "/data/r.txt", noOfNodes);
				
				
				if (s == null) {
					return "Ordinal number of node can be between 1 and " + noOfNodes + ".";
				}

				String okMethod = TrainTestOnNetworks.callMethodTrain(method, path, noOfNodes, alpha, beta, lr, maxIter,
						y, r, s, mainFrame, chckbxStandard.isSelected(), matlabPath, proxy,sTest,rTest,yTest);

				if (okMethod != null) {
					message = okMethod;
				}
			}
			if (message != null) {
				String delPath = Reader.jarFile() + "/MyModels" + method + "/" + txtModelName.getText();
				Reader.deleteDir(new File(delPath));
			}
			return message;
		}

	}


}
