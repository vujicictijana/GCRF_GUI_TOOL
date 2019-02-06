
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

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.exceptions.ConfigurationParameterseException;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.gui.threads.MGCRFTrainMyModelForGUI;
import gcrf_tool.gui.threads.RLSRTrainMyModelForGUI;
import gcrf_tool.gui.threads.UpGCRFTrainMyModelForGUI;
import gcrf_tool.predictors.helper.Helper;
import gcrf_tool.predictors.linearregression.LinearRegression;
import gcrf_tool.predictors.linearregression.MultipleLinearRegression;
import gcrf_tool.predictors.linearregression.MyLR;
import gcrf_tool.predictors.linearregression.TemporalData;
import gcrf_tool.predictors.neuralnetwork.MyNN;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComboBox;

import org.neuroph.core.data.DataSet;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TrainTemporalPanel extends JPanel {

	private static final long serialVersionUID = 7542338256284881226L;
	private JLabel lblModelName;
	private JTextField txtModelName;
	private JLabel lblAlpha;
	private JLabel lblBeta;
	private JTextField txtAlpha;
	private JTextField txtBeta;
	private JButton btnTrain;
	private JLabel lblMaxIterations;
	private JTextField txtIter;
	private JFileChooser fc;
	private JPanel panel;
	private JFrame mainFrame;
	// private JPanel panelForTable;
	// private JLabel lblTime;

	// params

	private int alphaReg;
	private int betaReg;
	private int iterTemp;
	private int hidden;
	private int iterNN;
	private String matlabPath;
	private boolean useMatlab;
	private String lambda;
	private int rlsrHidden;
	private int rlsrIterNN;
	private int sseIter;
	private int lsIter;
	private long proxy;

	private JLabel lblData;
	private JLabel lblModel;
	private JLabel lblPredictor;
	private JComboBox<String> cmbPredictor;
	private JLabel lblNoOfHidden;
	private JTextField txtHidden;
	private JLabel lblNoOfIterations;
	private JTextField txtIterNN;
	private JComboBox<String> cmbMethod;
	private JLabel lblMethod;
	private JLabel lblNoOfTime;
	private JTextField txtNoTime;
	private JTextField txtNoTimeTrain;
	private JLabel lblNoOfTime_1;
	private JCheckBox chkLearn;
	private JButton btnQuestionRegAlpha;
	private JLabel lblProvideTrainAn;
	private JLabel lblLag;
	private JTextField txtLag;
	private JButton btnQuestionLag;
	private JCheckBox chckUseX;
	private JTextField txtLFSize;
	private JTextField txtLambda;
	private JButton btnQuestionLambda;
	private JTextField txtNoTest;
	private JLabel lblLfSize;
	private JLabel lblSseIter;
	private JTextField txtSseIter;
	private JLabel lblLsIter;
	private JTextField txtLsIter;
	private JLabel label;
	private JComboBox<String> cmbDataset;

	private String xPath = "";
	private String yPath = "";
	private String sPath = "";
	private JButton button;

	public TrainTemporalPanel(JFrame mainFrame) {
		setBounds(new Rectangle(0, 0, 900, 650));
		setMinimumSize(new Dimension(500, 500));
		if (Reader.checkFile( Reader.jarFile() + "/cfg.txt")) {
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
				GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.columnWidths = new int[]{238, 91, 39, 157, 47, 0, 150, 114, 0};
				gridBagLayout.rowHeights = new int[]{45, 33, 23, 32, 32, 30, 41, 30, 36, 33, 32, 32, 36, 0, 32, 45, 0};
				gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				setLayout(gridBagLayout);
				GridBagConstraints gbc_lblProvideTrainAn = new GridBagConstraints();
				gbc_lblProvideTrainAn.anchor = GridBagConstraints.SOUTH;
				gbc_lblProvideTrainAn.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblProvideTrainAn.insets = new Insets(0, 0, 5, 0);
				gbc_lblProvideTrainAn.gridwidth = 11;
				gbc_lblProvideTrainAn.gridx = 0;
				gbc_lblProvideTrainAn.gridy = 0;
				add(getLblProvideTrainAn(), gbc_lblProvideTrainAn);
				GridBagConstraints gbc_lblData = new GridBagConstraints();
				gbc_lblData.anchor = GridBagConstraints.NORTH;
				gbc_lblData.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblData.insets = new Insets(0, 0, 5, 0);
				gbc_lblData.gridwidth = 11;
				gbc_lblData.gridx = 0;
				gbc_lblData.gridy = 0;
				add(getLblData(), gbc_lblData);
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.anchor = GridBagConstraints.EAST;
				gbc_label.fill = GridBagConstraints.VERTICAL;
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 1;
				add(getLabel(), gbc_label);
				GridBagConstraints gbc_cmbDataset = new GridBagConstraints();
				gbc_cmbDataset.fill = GridBagConstraints.BOTH;
				gbc_cmbDataset.insets = new Insets(0, 0, 5, 5);
				gbc_cmbDataset.gridwidth = 3;
				gbc_cmbDataset.gridx = 1;
				gbc_cmbDataset.gridy = 1;
				add(getCmbDataset(), gbc_cmbDataset);
				GridBagConstraints gbc_button = new GridBagConstraints();
				gbc_button.anchor = GridBagConstraints.WEST;
				gbc_button.fill = GridBagConstraints.VERTICAL;
				gbc_button.insets = new Insets(0, 0, 5, 5);
				gbc_button.gridx = 4;
				gbc_button.gridy = 1;
				add(getButton(), gbc_button);
				GridBagConstraints gbc_chkLearn = new GridBagConstraints();
				gbc_chkLearn.anchor = GridBagConstraints.NORTH;
				gbc_chkLearn.fill = GridBagConstraints.HORIZONTAL;
				gbc_chkLearn.insets = new Insets(0, 0, 5, 5);
				gbc_chkLearn.gridwidth = 2;
				gbc_chkLearn.gridx = 1;
				gbc_chkLearn.gridy = 2;
				add(getChkLearn(), gbc_chkLearn);
				GridBagConstraints gbc_lblNoOfTime = new GridBagConstraints();
				gbc_lblNoOfTime.anchor = GridBagConstraints.EAST;
				gbc_lblNoOfTime.fill = GridBagConstraints.VERTICAL;
				gbc_lblNoOfTime.insets = new Insets(0, 0, 5, 5);
				gbc_lblNoOfTime.gridx = 0;
				gbc_lblNoOfTime.gridy = 3;
				add(getLblNoOfTime(), gbc_lblNoOfTime);
				GridBagConstraints gbc_txtNoTime = new GridBagConstraints();
				gbc_txtNoTime.fill = GridBagConstraints.BOTH;
				gbc_txtNoTime.insets = new Insets(0, 0, 5, 5);
				gbc_txtNoTime.gridx = 1;
				gbc_txtNoTime.gridy = 3;
				add(getTxtNoTime(), gbc_txtNoTime);
				GridBagConstraints gbc_lblNoOfTime_1 = new GridBagConstraints();
				gbc_lblNoOfTime_1.fill = GridBagConstraints.BOTH;
				gbc_lblNoOfTime_1.insets = new Insets(0, 0, 5, 5);
				gbc_lblNoOfTime_1.gridx = 0;
				gbc_lblNoOfTime_1.gridy = 4;
				add(getLblNoOfTime_1(), gbc_lblNoOfTime_1);
				GridBagConstraints gbc_txtNoTimeTrain = new GridBagConstraints();
				gbc_txtNoTimeTrain.fill = GridBagConstraints.BOTH;
				gbc_txtNoTimeTrain.insets = new Insets(0, 0, 5, 5);
				gbc_txtNoTimeTrain.gridx = 1;
				gbc_txtNoTimeTrain.gridy = 4;
				add(getTxtNoTimeTrain(), gbc_txtNoTimeTrain);
				GridBagConstraints gbc_lblModelName = new GridBagConstraints();
				gbc_lblModelName.anchor = GridBagConstraints.EAST;
				gbc_lblModelName.fill = GridBagConstraints.VERTICAL;
				gbc_lblModelName.insets = new Insets(0, 0, 5, 5);
				gbc_lblModelName.gridx = 0;
				gbc_lblModelName.gridy = 5;
				add(getLblModelName(), gbc_lblModelName);
				GridBagConstraints gbc_txtModelName = new GridBagConstraints();
				gbc_txtModelName.fill = GridBagConstraints.BOTH;
				gbc_txtModelName.insets = new Insets(0, 0, 5, 5);
				gbc_txtModelName.gridwidth = 3;
				gbc_txtModelName.gridx = 1;
				gbc_txtModelName.gridy = 5;
				add(getTxtModelName(), gbc_txtModelName);
				GridBagConstraints gbc_lblModel = new GridBagConstraints();
				gbc_lblModel.fill = GridBagConstraints.BOTH;
				gbc_lblModel.insets = new Insets(0, 0, 5, 0);
				gbc_lblModel.gridwidth = 11;
				gbc_lblModel.gridx = 0;
				gbc_lblModel.gridy = 7;
				add(getLblModel(), gbc_lblModel);
				GridBagConstraints gbc_lblMethod = new GridBagConstraints();
				gbc_lblMethod.anchor = GridBagConstraints.EAST;
				gbc_lblMethod.fill = GridBagConstraints.VERTICAL;
				gbc_lblMethod.insets = new Insets(0, 0, 5, 5);
				gbc_lblMethod.gridx = 0;
				gbc_lblMethod.gridy = 8;
				add(getLblMethod(), gbc_lblMethod);
				GridBagConstraints gbc_cmbMethod = new GridBagConstraints();
				gbc_cmbMethod.fill = GridBagConstraints.BOTH;
				gbc_cmbMethod.insets = new Insets(0, 0, 5, 5);
				gbc_cmbMethod.gridwidth = 3;
				gbc_cmbMethod.gridx = 1;
				gbc_cmbMethod.gridy = 8;
				add(getCmbMethod(), gbc_cmbMethod);
				GridBagConstraints gbc_lblAlpha = new GridBagConstraints();
				gbc_lblAlpha.fill = GridBagConstraints.BOTH;
				gbc_lblAlpha.insets = new Insets(0, 0, 5, 5);
				gbc_lblAlpha.gridx = 0;
				gbc_lblAlpha.gridy = 9;
				add(getLblAlpha(), gbc_lblAlpha);
				GridBagConstraints gbc_lblLag = new GridBagConstraints();
				gbc_lblLag.fill = GridBagConstraints.BOTH;
				gbc_lblLag.insets = new Insets(0, 0, 5, 5);
				gbc_lblLag.gridx = 0;
				gbc_lblLag.gridy = 9;
				add(getLblLag(), gbc_lblLag);
				GridBagConstraints gbc_txtAlpha = new GridBagConstraints();
				gbc_txtAlpha.fill = GridBagConstraints.BOTH;
				gbc_txtAlpha.insets = new Insets(0, 0, 5, 5);
				gbc_txtAlpha.gridx = 1;
				gbc_txtAlpha.gridy = 9;
				add(getTxtAlpha(), gbc_txtAlpha);
				GridBagConstraints gbc_txtLag = new GridBagConstraints();
				gbc_txtLag.fill = GridBagConstraints.BOTH;
				gbc_txtLag.insets = new Insets(0, 0, 5, 5);
				gbc_txtLag.gridx = 1;
				gbc_txtLag.gridy = 9;
				add(getTxtLag(), gbc_txtLag);
				GridBagConstraints gbc_btnQuestionLag = new GridBagConstraints();
				gbc_btnQuestionLag.anchor = GridBagConstraints.WEST;
				gbc_btnQuestionLag.fill = GridBagConstraints.VERTICAL;
				gbc_btnQuestionLag.insets = new Insets(0, 0, 5, 5);
				gbc_btnQuestionLag.gridx = 2;
				gbc_btnQuestionLag.gridy = 9;
				add(getBtnQuestionLag(), gbc_btnQuestionLag);
				GridBagConstraints gbc_btnQuestionRegAlpha = new GridBagConstraints();
				gbc_btnQuestionRegAlpha.anchor = GridBagConstraints.WEST;
				gbc_btnQuestionRegAlpha.fill = GridBagConstraints.VERTICAL;
				gbc_btnQuestionRegAlpha.insets = new Insets(0, 0, 5, 5);
				gbc_btnQuestionRegAlpha.gridx = 2;
				gbc_btnQuestionRegAlpha.gridy = 9;
				add(getBtnQuestionRegAlpha(), gbc_btnQuestionRegAlpha);
				GridBagConstraints gbc_lblPredictor = new GridBagConstraints();
				gbc_lblPredictor.fill = GridBagConstraints.VERTICAL;
				gbc_lblPredictor.insets = new Insets(0, 0, 5, 5);
				gbc_lblPredictor.gridwidth = 2;
				gbc_lblPredictor.gridx = 3;
				gbc_lblPredictor.gridy = 9;
				add(getLblPredictor(), gbc_lblPredictor);
		
				GridBagConstraints gbc_txtLambda = new GridBagConstraints();
				gbc_txtLambda.fill = GridBagConstraints.BOTH;
				gbc_txtLambda.insets = new Insets(0, 0, 5, 5);
				gbc_txtLambda.gridx = 6;
				gbc_txtLambda.gridy = 9;
				add(getTxtLambda(), gbc_txtLambda);
				GridBagConstraints gbc_btnQuestionLambda = new GridBagConstraints();
				gbc_btnQuestionLambda.anchor = GridBagConstraints.WEST;
				gbc_btnQuestionLambda.fill = GridBagConstraints.VERTICAL;
				gbc_btnQuestionLambda.insets = new Insets(0, 0, 5, 5);
				gbc_btnQuestionLambda.gridx = 7;
				gbc_btnQuestionLambda.gridy = 9;
				add(getBtnQuestionLambda(), gbc_btnQuestionLambda);
				GridBagConstraints gbc_lblBeta = new GridBagConstraints();
				gbc_lblBeta.fill = GridBagConstraints.BOTH;
				gbc_lblBeta.insets = new Insets(0, 0, 5, 5);
				gbc_lblBeta.gridx = 0;
				gbc_lblBeta.gridy = 10;
				add(getLblBeta(), gbc_lblBeta);
				GridBagConstraints gbc_txtBeta = new GridBagConstraints();
				gbc_txtBeta.fill = GridBagConstraints.BOTH;
				gbc_txtBeta.insets = new Insets(0, 0, 5, 5);
				gbc_txtBeta.gridx = 1;
				gbc_txtBeta.gridy = 10;
				add(getTxtBeta(), gbc_txtBeta);
				GridBagConstraints gbc_txtNoTest = new GridBagConstraints();
				gbc_txtNoTest.fill = GridBagConstraints.BOTH;
				gbc_txtNoTest.insets = new Insets(0, 0, 5, 5);
				gbc_txtNoTest.gridx = 1;
				gbc_txtNoTest.gridy = 10;
				add(getTxtNoTest(), gbc_txtNoTest);
				GridBagConstraints gbc_lblNoOfHidden = new GridBagConstraints();
				gbc_lblNoOfHidden.anchor = GridBagConstraints.WEST;
				gbc_lblNoOfHidden.fill = GridBagConstraints.VERTICAL;
				gbc_lblNoOfHidden.insets = new Insets(0, 0, 5, 5);
				gbc_lblNoOfHidden.gridwidth = 2;
				gbc_lblNoOfHidden.gridx = 3;
				gbc_lblNoOfHidden.gridy = 10;
				add(getLblNoOfHidden(), gbc_lblNoOfHidden);
				GridBagConstraints gbc_txtHidden = new GridBagConstraints();
				gbc_txtHidden.anchor = GridBagConstraints.WEST;
				gbc_txtHidden.fill = GridBagConstraints.BOTH;
				gbc_txtHidden.insets = new Insets(0, 0, 5, 5);
				gbc_txtHidden.gridx = 6;
				gbc_txtHidden.gridy = 10;
				add(getTxtHidden(), gbc_txtHidden);
				GridBagConstraints gbc_lblMaxIterations = new GridBagConstraints();
				gbc_lblMaxIterations.anchor = GridBagConstraints.EAST;
				gbc_lblMaxIterations.fill = GridBagConstraints.VERTICAL;
				gbc_lblMaxIterations.insets = new Insets(0, 0, 5, 5);
				gbc_lblMaxIterations.gridx = 0;
				gbc_lblMaxIterations.gridy = 11;
				add(getLblMaxIterations(), gbc_lblMaxIterations);
								GridBagConstraints gbc_txtIter = new GridBagConstraints();
								gbc_txtIter.fill = GridBagConstraints.BOTH;
								gbc_txtIter.insets = new Insets(0, 0, 5, 5);
								gbc_txtIter.gridx = 1;
								gbc_txtIter.gridy = 11;
								add(getTxtMaxIter(), gbc_txtIter);
				
								GridBagConstraints gbc_lblNoOfIterations = new GridBagConstraints();
								gbc_lblNoOfIterations.anchor = GridBagConstraints.EAST;
								gbc_lblNoOfIterations.fill = GridBagConstraints.VERTICAL;
								gbc_lblNoOfIterations.insets = new Insets(0, 0, 5, 5);
								gbc_lblNoOfIterations.gridwidth = 2;
								gbc_lblNoOfIterations.gridx = 3;
								gbc_lblNoOfIterations.gridy = 11;
								add(getLblNoOfIterations(), gbc_lblNoOfIterations);
				GridBagConstraints gbc_txtIterNN = new GridBagConstraints();
				gbc_txtIterNN.anchor = GridBagConstraints.WEST;
				gbc_txtIterNN.fill = GridBagConstraints.BOTH;
				gbc_txtIterNN.insets = new Insets(0, 0, 5, 5);
				gbc_txtIterNN.gridx = 6;
				gbc_txtIterNN.gridy = 11;
				add(getTxtIterNN(), gbc_txtIterNN);
				GridBagConstraints gbc_lblLfSize = new GridBagConstraints();
				gbc_lblLfSize.anchor = GridBagConstraints.EAST;
				gbc_lblLfSize.fill = GridBagConstraints.VERTICAL;
				gbc_lblLfSize.insets = new Insets(0, 0, 5, 5);
				gbc_lblLfSize.gridx = 0;
				gbc_lblLfSize.gridy = 12;
				add(getLblLfSize(), gbc_lblLfSize);
				GridBagConstraints gbc_chckUseX = new GridBagConstraints();
				gbc_chckUseX.anchor = GridBagConstraints.NORTH;
				gbc_chckUseX.fill = GridBagConstraints.HORIZONTAL;
				gbc_chckUseX.insets = new Insets(0, 0, 5, 5);
				gbc_chckUseX.gridwidth = 2;
				gbc_chckUseX.gridx = 1;
				gbc_chckUseX.gridy = 12;
				add(getChckUseX(), gbc_chckUseX);
				GridBagConstraints gbc_txtLFSize = new GridBagConstraints();
				gbc_txtLFSize.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtLFSize.insets = new Insets(0, 0, 5, 5);
				gbc_txtLFSize.gridx = 1;
				gbc_txtLFSize.gridy = 12;
				add(getTxtLFSize(), gbc_txtLFSize);
				GridBagConstraints gbc_lblSseIter = new GridBagConstraints();
				gbc_lblSseIter.anchor = GridBagConstraints.EAST;
				gbc_lblSseIter.fill = GridBagConstraints.VERTICAL;
				gbc_lblSseIter.insets = new Insets(0, 0, 5, 5);
				gbc_lblSseIter.gridwidth = 2;
				gbc_lblSseIter.gridx = 3;
				gbc_lblSseIter.gridy = 12;
				add(getLblSseIter(), gbc_lblSseIter);
				GridBagConstraints gbc_txtSseIter = new GridBagConstraints();
				gbc_txtSseIter.fill = GridBagConstraints.BOTH;
				gbc_txtSseIter.anchor = GridBagConstraints.SOUTHWEST;
				gbc_txtSseIter.insets = new Insets(0, 0, 5, 5);
				gbc_txtSseIter.gridx = 6;
				gbc_txtSseIter.gridy = 12;
				add(getTxtSseIter(), gbc_txtSseIter);
				GridBagConstraints gbc_lblLsIter = new GridBagConstraints();
				gbc_lblLsIter.anchor = GridBagConstraints.EAST;
				gbc_lblLsIter.fill = GridBagConstraints.VERTICAL;
				gbc_lblLsIter.insets = new Insets(0, 0, 5, 5);
				gbc_lblLsIter.gridwidth = 2;
				gbc_lblLsIter.gridx = 3;
				gbc_lblLsIter.gridy = 13;
				add(getLblLsIter(), gbc_lblLsIter);
				GridBagConstraints gbc_txtLsIter = new GridBagConstraints();
				gbc_txtLsIter.anchor = GridBagConstraints.WEST;
				gbc_txtLsIter.fill = GridBagConstraints.BOTH;
				gbc_txtLsIter.insets = new Insets(0, 0, 5, 5);
				gbc_txtLsIter.gridx = 6;
				gbc_txtLsIter.gridy = 13;
				add(getTxtLsIter(), gbc_txtLsIter);
				GridBagConstraints gbc_btnTrain = new GridBagConstraints();
				gbc_btnTrain.fill = GridBagConstraints.BOTH;
				gbc_btnTrain.insets = new Insets(0, 0, 0, 5);
				gbc_btnTrain.gridwidth = 3;
				gbc_btnTrain.gridx = 2;
				gbc_btnTrain.gridy = 15;
				add(getBtnTrain(), gbc_btnTrain);
				GridBagConstraints gbc_cmbPredictor = new GridBagConstraints();
				gbc_cmbPredictor.fill = GridBagConstraints.BOTH;
				gbc_cmbPredictor.insets = new Insets(0, 0, 5, 5);
				gbc_cmbPredictor.gridx = 6;
				gbc_cmbPredictor.gridy = 9;
				add(getCmbPredictor(), gbc_cmbPredictor);
				fc = new JFileChooser();
				panel = this;
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"TEXT FILES", "txt", "text");
				fc.setFileFilter(filter);
				this.mainFrame = mainFrame;

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
		}
		return txtModelName;
	}

	private JLabel getLblAlpha() {
		if (lblAlpha == null) {
			lblAlpha = new JLabel("Regularization alpha:");
			lblAlpha.setVisible(false);
			lblAlpha.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAlpha.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblAlpha;
	}

	private JLabel getLblBeta() {
		if (lblBeta == null) {
			lblBeta = new JLabel("Regularization beta:");
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

	private JButton getBtnTrain() {
		if (btnTrain == null) {
			btnTrain = new JButton("TRAIN & TEST");
			btnTrain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String message = validateData();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message,
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {

						String mainPathDatasets = Reader.jarFile() 
								+ "/Datasets/TemporalNetworks";

						xPath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/x.txt";
						yPath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/y.txt";
						sPath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/s.txt";

						String readMePath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/readme.txt";
						int noOfNodes = 0;
						try {
							String nodesTrain = Reader.read(readMePath)[0];
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

						int noOfX = 0;
						try {
							String xTrain = Reader.read(readMePath)[2];
							noOfX = Integer.parseInt(xTrain.substring(xTrain
									.indexOf(":") + 2));
							if (noOfNodes <= 0) {
								JOptionPane
										.showMessageDialog(
												mainFrame,
												"No. of attributes per node should be greater than 0.",
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

						// System.out.println(noOfX);
						int noOfTime = Integer.parseInt(txtNoTime.getText());
						int noOfTimeTrain = Integer.parseInt(txtNoTimeTrain
								.getText());

						String[] x = Reader.read(xPath);
						String[] y = Reader.read(yPath);

						if (isMGCRF()) {
							double[][] s = Reader.readGraph(sPath, noOfNodes);


							String path = createFolderAndSaveData();
							runMGCRF(noOfNodes, noOfTime, noOfTimeTrain, noOfX,
									x, y, s, path);
						} else {
							int maxIter = Integer.parseInt(txtIter.getText());
							
							double[][] yMatrix = Reader.readMatrix(yPath,
									noOfNodes, noOfTime);

							if (isUpGCRF()) {

								double[][] x1 = null;
								if (chckUseX.isSelected()) {
									
									x1 = Reader.readMatrix(xPath, noOfNodes,
											noOfTime * noOfX);
								}
								double[][] s = null;
								if (!chkLearn.isSelected()) {
									s = Reader.readGraph(sPath, noOfNodes);
								}

								String path = createFolderAndSaveData();
								if (s == null) {
									s = new double[noOfNodes][noOfNodes];
								}
								int lag = Integer.parseInt(txtLag.getText());
								int test = Integer.parseInt(txtNoTest.getText());
								trainUpGCRF(matlabPath, path, x1, yMatrix, s,
										noOfTime, noOfTimeTrain, maxIter,
										noOfNodes, lag, noOfX, test);
							}
							if (isRLSR()) {
								double[][] x1 = Reader.readMatrix(xPath,
										noOfNodes, noOfTime * noOfX);

								int validation = Integer.parseInt(txtLag
										.getText());
								int lfSize = Integer.parseInt(txtLFSize
										.getText());
								int test = Integer.parseInt(txtNoTest.getText());
								int iterNN = Integer.parseInt(txtIterNN
										.getText());
								int hidden = Integer.parseInt(txtHidden
										.getText());
								int iterSSE = Integer.parseInt(txtSseIter
										.getText());
								int iterLs = Integer.parseInt(txtLsIter
										.getText());
								String lambda = txtLambda.getText();
								String path = createFolderAndSaveData();
								trainRLSR(matlabPath, path, x1, yMatrix,
										noOfTime, noOfTimeTrain, maxIter,
										noOfNodes, validation, noOfX, lfSize,
										lambda, test, iterNN, hidden, iterSSE,
										iterLs);
							}

						}

					}
				}

			});
			Style.buttonStyle(btnTrain);
		}
		return btnTrain;
	}

	public void trainMGCRF(String matlabPath, String modelFolder, double[][] r,
			double[][] y, double[][] s, int noTime, int training, int maxIter,
			int regAlpha, int regBeta) {
		ProgressBar frame = new ProgressBar("Training");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		MGCRFTrainMyModelForGUI t = new MGCRFTrainMyModelForGUI(matlabPath,
				modelFolder, frame, frame, s, r, y, noTime, training, maxIter,
				regAlpha, regBeta, proxy);

		t.start();
	}

	public void trainUpGCRF(String matlabPath, String modelFolder,
			double[][] r, double[][] y, double[][] s, int noTime, int training,
			int maxIter, int noOfNodes, int lag, int noX, int test) {
		ProgressBar frame = new ProgressBar("Training");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		boolean useX = true;
		if (!chckUseX.isSelected()) {
			useX = false;
		}
		UpGCRFTrainMyModelForGUI t = new UpGCRFTrainMyModelForGUI(matlabPath,
				modelFolder, frame, frame, s, r, y, noTime, training, maxIter,
				noOfNodes, lag, noX, useX, test, proxy);

		t.start();
	}

	public void trainRLSR(String matlabPath, String modelFolder, double[][] r,
			double[][] y, int noTime, int training, int maxIter, int noOfNodes,
			int validation, int noX, int lfSize, String lambda, int test,
			int iterNN, int hidden, int iterSSE, int iterLs) {
		ProgressBar frame = new ProgressBar("Training");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		RLSRTrainMyModelForGUI t = new RLSRTrainMyModelForGUI(matlabPath,
				modelFolder, frame, frame, r, y, noTime, training, maxIter,
				noOfNodes, validation, noX, lfSize, lambda, test, iterNN,
				hidden, iterSSE, iterLs, proxy);

		t.start();
	}


	

	

	private void runMGCRF(int noOfNodes, int noOfTime, int noOfTimeTrain,
			int noOfX, String[] x, String[] y, double[][] s, String path) {
		double result = callPredictor(path, x, y, noOfX, noOfTime,
				noOfTimeTrain, noOfNodes);

		if (result == -7000) {
			JOptionPane.showMessageDialog(mainFrame, "Unknown predictor.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (result == -3000) {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							cmbPredictor.getSelectedItem().toString()
									+ " cannot be applied to your data. Choose different predictor.",
							"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (result == -5000) {
			JOptionPane.showMessageDialog(mainFrame,
					"Files are not in correct format.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		if (result == -10000) {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							"Values should be normalized (range from 0 to 1) for neural network.",
							"Error", JOptionPane.ERROR_MESSAGE);
		} else {
			double[][] r = Reader.readMatrixTwoFiles(path + "/data/r.txt", path
					+ "/data/rTest.txt", noOfNodes, noOfTime, noOfTimeTrain);
			double[][] yMatrix = Reader.readMatrix(path + "/data/y.txt",
					noOfNodes, noOfTime);
			if (BasicCalcs.isSymmetric(s)) {
				int maxIter = Integer.parseInt(txtIter.getText());
				int regAlpha = Integer.parseInt(txtAlpha.getText());
				int regBeta = Integer.parseInt(txtBeta.getText());
				trainMGCRF(matlabPath, path, r, yMatrix, s, noOfTime,
						noOfTimeTrain, maxIter, regAlpha, regBeta);
			} else {
				JOptionPane.showMessageDialog(mainFrame,
						"For m-GCRF method matrix should be symmetric.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private String createFolderAndSaveData() {
		File matrixFile = null;
		if (!chkLearn.isSelected()) {
			matrixFile = new File(sPath);
		}
		File xFile = new File(xPath);
		File yFile = new File(yPath);
		String method = cmbMethod.getSelectedItem().toString();

		
		Writer.createFolder(Reader.jarFile()  + "/MyModels" + method);
		String path = Reader.jarFile()  + "/MyModels" + method + "/"
				+ txtModelName.getText();
		Writer.createFolder(path);
		String dataPath = Reader.jarFile()  + "/MyModels" + method + "/"
				+ txtModelName.getText() + "/data";
		Writer.createFolder(dataPath);
		Writer.copyFile(xFile, dataPath + "/x.txt");
		Writer.copyFile(yFile, dataPath + "/y.txt");
		if (matrixFile != null) {
			Writer.copyFile(matrixFile, dataPath + "/matrix.txt");
		}
		return path;
	}

	private double callPredictor(String path, String[] x, String[] y, int noX,
			int noT, int noTrain, int nodes) {
		if (cmbPredictor.getSelectedItem().toString().contains("neural")) {
			int noOfHidden = Integer.parseInt(txtHidden.getText());
			int noOfIter = Integer.parseInt(txtIterNN.getText());
			DataSet trainingSet = Helper.prepareTemporalDataForNN(x, y, noX,
					noT, true);
			if (trainingSet == null) {
				return -10000;
			}
			return MyNN.learnAndTest(noOfHidden, trainingSet, 0.003, noOfIter,
					path, noT, noTrain);
		}
		if (cmbPredictor.getSelectedItem().toString().contains("linear")) {
			TemporalData t = Helper.prepareTemporalDataForLR(x, y, noX, noT,
					nodes, noTrain);
			MyLR.learn(t.getxTrain(), t.getyTrain(), path);
			if (Writer.checkFolder(path + "/mlr")) {
				MultipleLinearRegression m = (MultipleLinearRegression) Helper
						.deserilazie(path + "/mlr/lr.txt");
				return m.test(t.getyTest(), t.getxTest(), path, true);
			}
			if (Writer.checkFolder(path + "/lr")) {
				double[][] xMlr = t.getxTest();
				double[] xOne = new double[xMlr.length];
				for (int i = 0; i < xOne.length; i++) {
					xOne[i] = xMlr[i][0];
				}
				LinearRegression lr = (LinearRegression) Helper
						.deserilazie(path + "/lr/lr.txt");
				return LinearRegression
						.test(t.getyTest(), xOne, path, lr, true);
			}
		}
		return -7000;

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

		int t = 0;
		try {
			t = Integer.parseInt(txtNoTime.getText());
			if (t <= 0) {
				return "No. of time points should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of time points should be integer.";
		}
		try {
			int a = Integer.parseInt(txtNoTimeTrain.getText());
			if (a <= 0) {
				return "No. of time points for training should be greater than 0.";
			}
			if (a >= t) {
				return "No. of time points for training should be lower than total no. of time points.";
			}

		} catch (NumberFormatException e) {
			return "No. of time points for training should be integer.";
		}

		if (txtModelName.getText().equals("")) {
			return "Insert model name.";
		}

		if (cmbMethod.getSelectedIndex() == 0) {
			return "Choose method.";
		}

		if (isMGCRF()) {
			String validate = validateDataForMGCRF();

			if (validate != null) {
				return validate;
			}
		}

		if (isUpGCRF()) {
			String validate = validateDataForUpGCRF();

			if (validate != null) {
				return validate;
			}
		}

		if (isRLSR()) {
			String validate = validateDataForRLSR();

			if (validate != null) {
				return validate;
			}
		}

		String method = cmbMethod.getSelectedItem().toString();
		if (Writer.checkFolder(Reader.jarFile()  + "/MyModels" + method + "/"
				+ txtModelName.getText())) {
			return "Model with name " + txtModelName.getText()
					+ " already exists.";
		}

		return null;
	}

	private String validateDataForMGCRF() {
		try {
			int a = Integer.parseInt(txtAlpha.getText());
			if (a <= 0) {
				return "Alpha for regularization should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Alpha for regularization should be integer.";
		}
		try {
			int b = Integer.parseInt(txtBeta.getText());
			if (b <= 0) {
				return "Beta for regularization should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Beta for regularization should be integer.";
		}

		try {
			int b = Integer.parseInt(txtIter.getText());
			if (b <= 0) {
				return "No. of iteration for m-GCRF training should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of iteration for m-GCRF training should be integer.";
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

	private String validateDataForUpGCRF() {

		int t = Integer.parseInt(txtNoTime.getText());
		int train = Integer.parseInt(txtNoTimeTrain.getText());
		int lag = 0;
		try {
			lag = Integer.parseInt(txtLag.getText());
			if (lag <= 0) {
				return "Lag should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Lag should be integer.";
		}
		try {
			int a = Integer.parseInt(txtNoTest.getText());
			if (a <= 0) {
				return "No. of time points for test should be greater than 0.";
			}
			if ((train + a + lag) > t) {
				return "Sum of no. of time points for training, for test and Lag should be lower than or equal to total no. of time points.";
			}
		} catch (NumberFormatException e) {
			return "No. of time points for test should be integer.";
		}

		try {
			int b = Integer.parseInt(txtIter.getText());
			if (b <= 0) {
				return "No. of iteration for m-GCRF training should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of iteration for m-GCRF training should be integer.";
		}
		return null;
	}

	private String validateDataForRLSR() {
		int t = Integer.parseInt(txtNoTime.getText());
		int train = Integer.parseInt(txtNoTimeTrain.getText());
		int valid = 0;
		try {
			valid = Integer.parseInt(txtLag.getText());
			if (valid <= 0) {
				return "No. of time points for validation should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of time points for validation should be integer.";
		}

		try {
			int a = Integer.parseInt(txtNoTest.getText());
			if (a <= 0) {
				return "No. of time points for test should be greater than 0.";
			}
			if ((train + a + valid) > t) {
				return "Sum of no. of time points for training, validation and test should be lower than or equal to total no. of time points.";
			}
		} catch (NumberFormatException e) {
			return "No. of time points for test should be integer.";
		}

		int iter = 0;
		try {
			iter = Integer.parseInt(txtIter.getText());
			if (iter <= 0) {
				return "No. of iteration for RLSR training should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of iteration for RLSR training should be integer.";
		}
		try {
			int b = Integer.parseInt(txtLFSize.getText());
			if (b <= 0 || b >= iter) {
				return "LFSize should be greater than 0 and lower than max. iterations.";
			}
		} catch (NumberFormatException e) {
			return "LFSize should be integer.";
		}

		String lambda = txtLambda.getText();
		if (lambda.contains(",")) {
			String[] allLambda = lambda.split(",");
			for (int i = 0; i < allLambda.length; i++) {
				try {
					Double.parseDouble(allLambda[i]);
				} catch (NumberFormatException e) {
					return "Lambda should be comma separated array of double values or single double value.";
				}
			}

		} else {
			try {
				Double.parseDouble(txtLambda.getText());
			} catch (NumberFormatException e) {
				return "Lambda should be comma separated array of double values or single double value.";
			}
		}
		return null;
	}

	private boolean isMGCRF() {
		return cmbMethod.getSelectedItem().toString().contains("m-GCRF");
	}

	private boolean isUpGCRF() {
		return cmbMethod.getSelectedItem().toString().contains("up-GCRF");
	}

	private boolean isRLSR() {
		return cmbMethod.getSelectedItem().toString().contains("RLSR");
	}

	public String validateDataForTestPredictor() {

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
			try {
				alphaReg = Integer.parseInt(params.get("AlphaReg").toString());
				betaReg = Integer.parseInt(params.get("BetaReg").toString());
				hidden = Integer.parseInt(params.get("NN hidden").toString());
				iterNN = Integer.parseInt(params.get("Iterations NN")
						.toString());
				iterTemp = Integer.parseInt(params.get("Iterations temporal")
						.toString());
				lambda = params.get("Lambda").toString();
				rlsrHidden = Integer.parseInt(params.get("RLSR hidden NN")
						.toString());
				rlsrIterNN = Integer.parseInt(params.get("RLSR iterations NN")
						.toString());
				sseIter = Integer.parseInt(params.get("RLSR SSE iterations")
						.toString());
				lsIter = Integer.parseInt(params.get("RLSR SSE LS iterations")
						.toString());
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
		txtAlpha.setText(alphaReg + "");
		txtBeta.setText(betaReg + "");
		txtIter.setText(iterTemp + "");
		txtLambda.setText(lambda);
		txtSseIter.setText(sseIter + "");
		txtLsIter.setText(lsIter + "");
	}

	private JLabel getLblData() {
		if (lblData == null) {
			lblData = new JLabel("DATA");
			lblData.setForeground(Color.WHITE);
			lblData.setBackground(Color.GRAY);
			lblData.setHorizontalAlignment(SwingConstants.CENTER);
			lblData.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblData.setOpaque(true);
		}
		return lblData;
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

	private JLabel getLblPredictor() {
		if (lblPredictor == null) {
			lblPredictor = new JLabel("Unstructured predictor:");
			lblPredictor.setVisible(false);
			lblPredictor.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPredictor.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblPredictor;
	}

	private JComboBox<String> getCmbPredictor() {
		if (cmbPredictor == null) {
			cmbPredictor = new JComboBox<String>();
			cmbPredictor.setVisible(false);
			cmbPredictor.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (cmbPredictor.getSelectedItem().toString()
							.contains("neural")) {
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

	private JLabel getLblNoOfHidden() {
		if (lblNoOfHidden == null) {
			lblNoOfHidden = new JLabel("No. of hidden neurons for NN:");
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
			lblNoOfIterations = new JLabel("No. of iterations for NN:");
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

			if (useMatlab) {
				cmbMethod.addItem("choose method");
				cmbMethod.addItem("RLSR");
				cmbMethod.addItem("up-GCRF");
				cmbMethod.addItem("m-GCRF");
			} else {
				cmbMethod.addItem("no available methods without MATLAB");
			}
			cmbMethod.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					String method = cmbMethod.getSelectedItem().toString();
					switch (method) {
					case "m-GCRF":
						hideParamsUpGCRF();
						hideParamsRLSR();
						showParamsMGCRF();
						break;
					case "up-GCRF":
						hideParamsRLSR();
						hideParamsMGCRF();
						showParamsUpGCRF();
						break;
					case "RLSR":
						hideParamsMGCRF();
						hideParamsUpGCRF();
						showParamsRLSR();
						break;
					default:
						hideParamsMGCRF();
						hideParamsUpGCRF();
						hideParamsRLSR();
						break;
					}
					// if (isUpGCRF() || method.contains("Chao")) {
					// chkLearn.setSelected(true);
					// } else {
					// chkLearn.setSelected(false);
					// }
				}
			});
		}
		return cmbMethod;
	}

	public void showParamsMGCRF() {
		lblAlpha.setVisible(true);
		txtAlpha.setVisible(true);
		lblBeta.setText("Regularization beta:");
		lblBeta.setVisible(true);
		txtBeta.setVisible(true);
		lblMaxIterations.setVisible(true);
		txtIter.setVisible(true);
		lblPredictor.setText("Unstructured predictor:");
		lblPredictor.setVisible(true);
		cmbPredictor.setVisible(true);
		btnQuestionRegAlpha.setVisible(true);
		txtIterNN.setText(iterNN + "");
		txtHidden.setText(hidden + "");
		cmbPredictor.setSelectedIndex(0);
	}

	public void showParamsUpGCRF() {
		lblLag.setText("Lag:");
		lblLag.setVisible(true);
		txtLag.setText("");
		txtLag.setVisible(true);
		btnQuestionLag.setVisible(true);
		chckUseX.setVisible(true);
		lblMaxIterations.setVisible(true);
		txtIter.setVisible(true);
		lblBeta.setText("No. of time points for test:");
		lblBeta.setVisible(true);
		txtNoTest.setVisible(true);
	}

	public void showParamsRLSR() {
		lblLag.setText("No. of time points for validation:");
		lblLag.setVisible(true);
		txtLag.setText("");
		txtLag.setVisible(true);
		lblMaxIterations.setVisible(true);
		txtIter.setVisible(true);
		lblBeta.setText("No. of time points for test:");
		lblBeta.setVisible(true);
		txtLFSize.setVisible(true);
		lblPredictor.setText("Lambda set:");
		lblPredictor.setVisible(true);
		btnQuestionLambda.setVisible(true);
		txtLambda.setVisible(true);
		lblLfSize.setVisible(true);
		txtNoTest.setVisible(true);
		lblNoOfHidden.setVisible(true);
		txtHidden.setVisible(true);
		lblNoOfIterations.setVisible(true);
		txtIterNN.setVisible(true);
		lblLsIter.setVisible(true);
		txtLsIter.setVisible(true);
		lblSseIter.setVisible(true);
		txtSseIter.setVisible(true);
		txtHidden.setText(rlsrHidden + "");
		txtIterNN.setText(rlsrIterNN + "");
	}

	public void hideParamsMGCRF() {
		lblAlpha.setVisible(false);
		txtAlpha.setVisible(false);
		lblBeta.setVisible(false);
		txtBeta.setVisible(false);
		lblMaxIterations.setVisible(false);
		txtIter.setVisible(false);
		lblPredictor.setVisible(false);
		cmbPredictor.setVisible(false);
		btnQuestionRegAlpha.setVisible(false);
	}

	public void hideParamsUpGCRF() {
		lblLag.setVisible(false);
		txtLag.setVisible(false);
		btnQuestionLag.setVisible(false);
		chckUseX.setVisible(false);
		lblMaxIterations.setVisible(false);
		txtIter.setVisible(false);
		txtNoTest.setVisible(false);
	}

	public void hideParamsRLSR() {
		lblLag.setVisible(false);
		txtLag.setVisible(false);
		lblMaxIterations.setVisible(false);
		txtIter.setVisible(false);
		lblBeta.setVisible(false);
		txtLFSize.setVisible(false);
		btnQuestionLambda.setVisible(false);
		txtLambda.setVisible(false);
		lblLfSize.setVisible(false);
		txtNoTest.setVisible(false);
		lblNoOfHidden.setVisible(false);
		txtHidden.setVisible(false);
		lblNoOfIterations.setVisible(false);
		txtIterNN.setVisible(false);
		lblLsIter.setVisible(false);
		txtLsIter.setVisible(false);
		lblSseIter.setVisible(false);
		txtSseIter.setVisible(false);
	}

	private JLabel getLblMethod() {
		if (lblMethod == null) {
			lblMethod = new JLabel("Method:");
			lblMethod.setHorizontalAlignment(SwingConstants.RIGHT);
			lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblMethod;
	}

	private JLabel getLblNoOfTime() {
		if (lblNoOfTime == null) {
			lblNoOfTime = new JLabel("No. of time points:");
			lblNoOfTime.setEnabled(false);
			lblNoOfTime.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNoOfTime.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblNoOfTime;
	}

	private JTextField getTxtNoTime() {
		if (txtNoTime == null) {
			txtNoTime = new JTextField();
			txtNoTime.setEnabled(false);
			txtNoTime.setEditable(false);
			txtNoTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtNoTime.setColumns(10);
		}
		return txtNoTime;
	}

	private JTextField getTxtNoTimeTrain() {
		if (txtNoTimeTrain == null) {
			txtNoTimeTrain = new JTextField();
			txtNoTimeTrain.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtNoTimeTrain.setColumns(10);
		}
		return txtNoTimeTrain;
	}

	private JLabel getLblNoOfTime_1() {
		if (lblNoOfTime_1 == null) {
			lblNoOfTime_1 = new JLabel("No. of time points for train:");
			lblNoOfTime_1.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNoOfTime_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblNoOfTime_1;
	}

	private JCheckBox getChkLearn() {
		if (chkLearn == null) {
			chkLearn = new JCheckBox("Learn similarity");
			chkLearn.setEnabled(false);
			chkLearn.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (chkLearn.isSelected()) {
						removeMethod("m-GCRF");
					} else {
						addMethod("m-GCRF");
					}
				}
			});
		}
		return chkLearn;
	}

	public void removeMethod(String name) {
		for (int i = 0; i < cmbMethod.getItemCount(); i++) {
			if (cmbMethod.getItemAt(i).toString().contains(name)) {
				if (isMGCRF()) {
					cmbMethod.setSelectedIndex(0);
				}
				cmbMethod.removeItemAt(i);
				panel.repaint();
				panel.revalidate();

				return;
			}
		}
	}

	public void addMethod(String name) {
		for (int i = 0; i < cmbMethod.getItemCount(); i++) {
			if (cmbMethod.getItemAt(i).toString().equalsIgnoreCase(name)) {
				return;
			}
		}
		cmbMethod.addItem(name);
		panel.repaint();
		panel.revalidate();
	}

	private JButton getBtnQuestionRegAlpha() {
		if (btnQuestionRegAlpha == null) {
			btnQuestionRegAlpha = new JButton("");
			btnQuestionRegAlpha.setVisible(false);
			Style.questionButtonStyle(btnQuestionRegAlpha);
			btnQuestionRegAlpha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane
							.showMessageDialog(
									mainFrame,
									"For higher percentage of missing values put higher value regularization parameter.",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionRegAlpha;
	}

	private JLabel getLblProvideTrainAn() {
		if (lblProvideTrainAn == null) {
			lblProvideTrainAn = new JLabel(
					"Provide train and test data together");
			lblProvideTrainAn.setOpaque(true);
			lblProvideTrainAn.setHorizontalAlignment(SwingConstants.CENTER);
			lblProvideTrainAn.setForeground(Color.WHITE);
			lblProvideTrainAn.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblProvideTrainAn.setBackground(Color.GRAY);
		}
		return lblProvideTrainAn;
	}

	private JLabel getLblLag() {
		if (lblLag == null) {
			lblLag = new JLabel("Lag:");
			lblLag.setVisible(false);
			lblLag.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLag.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblLag;
	}

	private JTextField getTxtLag() {
		if (txtLag == null) {
			txtLag = new JTextField();
			txtLag.setVisible(false);
			txtLag.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLag.setColumns(10);
		}
		return txtLag;
	}

	private JButton getBtnQuestionLag() {
		if (btnQuestionLag == null) {
			btnQuestionLag = new JButton("");
			btnQuestionLag.setVisible(false);
			Style.questionButtonStyle(btnQuestionLag);
			btnQuestionLag.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Number of previous time step values that will be used as inputs",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionLag;
	}

	private JCheckBox getChckUseX() {
		if (chckUseX == null) {
			chckUseX = new JCheckBox("Use attributes");
			chckUseX.setSelected(true);
			chckUseX.setVisible(false);
		}
		return chckUseX;
	}

	private JTextField getTxtLFSize() {
		if (txtLFSize == null) {
			txtLFSize = new JTextField();
			txtLFSize.setVisible(false);
			txtLFSize.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLFSize.setColumns(10);
		}
		return txtLFSize;
	}

	private JTextField getTxtLambda() {
		if (txtLambda == null) {
			txtLambda = new JTextField();
			txtLambda.setVisible(false);
			txtLambda.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLambda.setColumns(10);
		}
		return txtLambda;
	}

	private JButton getBtnQuestionLambda() {
		if (btnQuestionLambda == null) {
			btnQuestionLambda = new JButton("");
			btnQuestionLambda.setVisible(false);
			Style.questionButtonStyle(btnQuestionLambda);
			btnQuestionLambda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Set of hyperparameters for regularizor. It can be array of double values or single value. "
											+ "\nValues should be comma separated."
											+ "\nExample: 0.0001,0.001,0.01,0.01,0.1,1",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionLambda;
	}

	private JTextField getTxtNoTest() {
		if (txtNoTest == null) {
			txtNoTest = new JTextField();
			txtNoTest.setVisible(false);
			txtNoTest.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtNoTest.setColumns(10);
		}
		return txtNoTest;
	}

	private JLabel getLblLfSize() {
		if (lblLfSize == null) {
			lblLfSize = new JLabel("LF size:");
			lblLfSize.setVisible(false);
			lblLfSize.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLfSize.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblLfSize;
	}

	private JLabel getLblSseIter() {
		if (lblSseIter == null) {
			lblSseIter = new JLabel("SSE max. iterations:");
			lblSseIter.setVisible(false);
			lblSseIter.setHorizontalAlignment(SwingConstants.RIGHT);
			lblSseIter.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblSseIter;
	}

	private JTextField getTxtSseIter() {
		if (txtSseIter == null) {
			txtSseIter = new JTextField();
			txtSseIter.setVisible(false);
			txtSseIter.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtSseIter.setColumns(10);
		}
		return txtSseIter;
	}

	private JLabel getLblLsIter() {
		if (lblLsIter == null) {
			lblLsIter = new JLabel("SSE LS max. iterations:");
			lblLsIter.setVisible(false);
			lblLsIter.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLsIter.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblLsIter;
	}

	private JTextField getTxtLsIter() {
		if (txtLsIter == null) {
			txtLsIter = new JTextField();
			txtLsIter.setVisible(false);
			txtLsIter.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLsIter.setColumns(10);
		}
		return txtLsIter;
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
			cmbDataset = new JComboBox<String>();
			cmbDataset.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (cmbDataset.getSelectedIndex() > 0) {

						String mainPathDatasets = Reader.jarFile() 
								+ "/Datasets/TemporalNetworks";

						String sPath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/s.txt";

						if (Reader.checkFile(sPath)) {
							addMethod("m-GCRF");
							chkLearn.setSelected(false);
						} else {
							removeMethod("m-GCRF");
							chkLearn.setSelected(true);
						}

						String readMePath = mainPathDatasets + "/"
								+ cmbDataset.getSelectedItem().toString()
								+ "/readme.txt";

						try {
							String xTrain = Reader.read(readMePath)[1];
							int noOfTime = Integer.parseInt(xTrain
									.substring(xTrain.indexOf(":") + 2));
							txtNoTime.setText(noOfTime + "");
							if (noOfTime <= 0) {
								JOptionPane
										.showMessageDialog(
												mainFrame,
												"No. of time points should be greater than 0.",
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
					}
				}
			});
			cmbDataset.addItem("choose dataset");

			String[] files = Reader.getAllFolders(Reader.jarFile()  + "/Datasets/TemporalNetworks");
			for (int i = 0; i < files.length; i++) {
				cmbDataset.addItem(files[i]);
			}
		}
		return cmbDataset;
	}

	private JButton getButton() {
		if (button == null) {
			button = new JButton("");
			Style.questionButtonStyle(button);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Dataset samples are provided by default."
											+ "\nUse samples or add your own dataset in Datasets -> Add dataset menu item."
											+ "\nInformation for datasets samples can be found in Help -> Datasets menu item.",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return button;
	}
}
