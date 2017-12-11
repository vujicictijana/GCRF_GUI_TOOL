package gcrf_tool.gui.panels;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JTabbedPane;

import gcrf_tool.exceptions.ConfigurationParameterseException;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.MainFrame;
import gcrf_tool.gui.style.Style;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import java.util.Map;


import java.awt.Insets;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JTextField;

import java.awt.SystemColor;

import javax.swing.JCheckBox;

public class ConfigurePanel extends JPanel {

	private static final long serialVersionUID = -4600085582512410106L;
	private JFrame mainFrame;
	private JTabbedPane tabbedPane;

	private JLabel lblManageDatasets;
	private JLabel label;
	private JLabel label_1;
	private JTextField txtAlphaGen;
	private JTextField txtBetaGen;
	private JLabel label_2;
	private JTextField txtAlpha;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	private JTextField txtBeta;
	private JTextField txtLR;
	private JTextField txtIter;
	private JLabel label_6;
	private JLabel label_7;
	private JTextField txtHidden;
	private JTextField txtIterNN;
	private JPanel panel_1;
	private JButton btnSave;
	private JButton btnResetToDefaults;
	private JLabel label_8;
	private JTextField txtIterMGCRF;
	private JLabel label_9;
	private JLabel label_10;
	private JLabel label_11;
	private JTextField txtAlphaReg;
	private JTextField txtBetaReg;
	private JLabel label_12;
	private JLabel lblNewLabel;
	private JLabel label_13;
	private JLabel label_14;
	private JLabel label_15;
	private JLabel label_16;
	private JLabel label_17;
	private JLabel label_18;
	private JTextField txtLambda;
	private JButton button_2;
	private JLabel label_19;
	private JLabel label_20;
	private JLabel label_21;
	private JLabel label_22;
	private JTextField txtRlsrHidden;
	private JTextField txtRlsrIterNN;
	private JTextField txtSseIter;
	private JTextField txtLsIter;
	private JLabel label_23;
	private JLabel label_24;
	private JLabel lblPathToMatlabexe;
	private JLabel lblProxytimeout;
	private JTextField txtProxy;
	private JTextField txtPath;
	private JButton btnBrowse;
	private JLabel lblHelp;
	private JCheckBox chckMatlab;
	private JFileChooser fc;
	
	private JPanel panel;
	// params

	private int alphaGen;
	private int betaGen;
	private int alpha;
	private int beta;
	private double lr;
	private int iterations;
	private int hidden;
	private int iterNN;
	private String matlabPath;
	private boolean useMatlab;
	private int alphaReg;
	private int betaReg;
	private int iterTemp;
	private String lambda;
	private int rlsrHidden;
	private int rlsrIterNN;
	private int sseIter;
	private int lsIter;
	private long proxy;
	private JLabel lblHelpMac;

	/**
	 * Create the panel.
	 */
	public ConfigurePanel(JFrame mainFrame) {
		this.panel = this;
		this.mainFrame = mainFrame;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 479, 45, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblManageDatasets = new GridBagConstraints();
		gbc_lblManageDatasets.fill = GridBagConstraints.BOTH;
		gbc_lblManageDatasets.insets = new Insets(0, 0, 5, 0);
		gbc_lblManageDatasets.gridx = 0;
		gbc_lblManageDatasets.gridy = 0;
		add(getLblManageDatasets(), gbc_lblManageDatasets);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		add(getTabbedPane(), gbc_tabbedPane);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 2;
		add(getPanel_1(), gbc_panel_1);
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"EXE FILES", "exe", "exe");
		fc.setFileFilter(filter);
		if (Reader.checkFile(Reader.jarFile() + "/cfg.txt")) {
			String result = readParametersFromCfg();
			if (result != null) {
				JOptionPane.showMessageDialog(mainFrame, result
						+ " Parameters will be reset to the default values.",
						"Error", JOptionPane.ERROR_MESSAGE);
				setUpDefaultValues();
			}
		} else {
			setUpDefaultValues();
		}
		setTxtValues();

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
				hidden = Integer.parseInt(params.get("NN hidden").toString());
				iterNN = Integer.parseInt(params.get("Iterations NN")
						.toString());
				alphaReg = Integer.parseInt(params.get("AlphaReg").toString());
				betaReg = Integer.parseInt(params.get("BetaReg").toString());
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
				proxy = Integer.parseInt(params.get("Proxy")
						.toString())/1000;
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
	
	public void setUpDefaultValues() {
		alpha = 1;
		beta = 1;
		lr = 0.01;
		iterations = 1000;
		alphaGen = 5;
		betaGen = 1;
		hidden = 1;
		iterNN = 1000;
		useMatlab = true;
		matlabPath = "";
		alphaReg = 10000;
		betaReg = 10;
		iterTemp = 50;
		lambda = "0.01";
		rlsrHidden = 20;
		rlsrIterNN = 200;
		sseIter = 1000;
		lsIter = 1000;
		proxy = 300;
	}
	
	public void setTxtValues() {
		txtAlphaGen.setText(alphaGen + "");
		txtBetaGen.setText(betaGen + "");
		txtAlpha.setText(alpha + "");
		txtBeta.setText(beta + "");
		txtLR.setText(lr + "");
		txtIter.setText(iterations + "");
		txtIterNN.setText(iterNN + "");
		txtHidden.setText(hidden + "");
		chckMatlab.setSelected(useMatlab);
		txtPath.setText(matlabPath);
		txtAlphaReg.setText(alphaReg + "");
		txtBetaReg.setText(betaReg + "");
		txtIterMGCRF.setText(iterTemp + "");
		txtLambda.setText(lambda);
		txtRlsrHidden.setText(rlsrHidden + "");
		txtRlsrIterNN.setText(rlsrIterNN + "");
		txtSseIter.setText(sseIter + "");
		txtLsIter.setText(lsIter + "");
		txtProxy.setText(proxy + "");
	}


	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			JComponent panel1 = makePanelRandom();
			tabbedPane.addTab("RANDOM", null, panel1,
					"Parameters for random data generation");

			tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

			JComponent panel2 = makePanelGCRFDirGCRF();
			tabbedPane.addTab("GCRF and DirGCRF", null, panel2,
					"Parameters for training GCRF and DirGCRF");
			tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
			
			JComponent panel3 = makePanelNN();
			tabbedPane.addTab("NEURAL NETWORK", null, panel3,
					"Parameters for training neural network:");
			tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
			
			JComponent panel4 = makePanelTemporal();
			tabbedPane.addTab("TEMPORAL", null, panel4,
					"Parameters for methods for temporal networks");
			tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
			
			JComponent panel5 = makePanelMatlab();
			tabbedPane.addTab("MATLAB", null, panel5,
					"Parameters for MATLAB");
			tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
		}
		return tabbedPane;
	}
	
	protected JComponent makePanelMatlab() {
		JPanel panel = new JPanel(false);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		GridBagConstraints gbc_label_23 = new GridBagConstraints();
		gbc_label_23.insets = new Insets(0, 0, 5, 5);
		gbc_label_23.gridx = 1;
		gbc_label_23.gridy = 0;
		panel.add(getLabel_23(), gbc_label_23);
		GridBagConstraints gbc_chckMatlab = new GridBagConstraints();
		gbc_chckMatlab.anchor = GridBagConstraints.WEST;
		gbc_chckMatlab.insets = new Insets(0, 0, 5, 5);
		gbc_chckMatlab.gridx = 2;
		gbc_chckMatlab.gridy = 1;
		panel.add(getChckMatlab(), gbc_chckMatlab);
		GridBagConstraints gbc_label_24 = new GridBagConstraints();
		gbc_label_24.insets = new Insets(0, 0, 5, 5);
		gbc_label_24.gridx = 1;
		gbc_label_24.gridy = 2;
		panel.add(getLabel_24(), gbc_label_24);
		GridBagConstraints gbc_lblPathToMatlabexe = new GridBagConstraints();
		gbc_lblPathToMatlabexe.anchor = GridBagConstraints.EAST;
		gbc_lblPathToMatlabexe.insets = new Insets(0, 0, 5, 5);
		gbc_lblPathToMatlabexe.gridx = 1;
		gbc_lblPathToMatlabexe.gridy = 3;
		panel.add(getLblPathToMatlabexe(), gbc_lblPathToMatlabexe);
		GridBagConstraints gbc_txtPath = new GridBagConstraints();
		gbc_txtPath.insets = new Insets(0, 0, 5, 5);
		gbc_txtPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPath.gridx = 2;
		gbc_txtPath.gridy = 3;
		panel.add(getTxtPath(), gbc_txtPath);
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowse.gridx = 3;
		gbc_btnBrowse.gridy = 3;
		panel.add(getBtnBrowse(), gbc_btnBrowse);
		GridBagConstraints gbc_lblHelp = new GridBagConstraints();
		gbc_lblHelp.anchor = GridBagConstraints.WEST;
		gbc_lblHelp.insets = new Insets(0, 0, 5, 5);
		gbc_lblHelp.gridx = 2;
		gbc_lblHelp.gridy = 4;
		panel.add(getLblHelp(), gbc_lblHelp);
		GridBagConstraints gbc_lblHelpMac = new GridBagConstraints();
		gbc_lblHelpMac.anchor = GridBagConstraints.WEST;
		gbc_lblHelpMac.insets = new Insets(0, 0, 5, 5);
		gbc_lblHelpMac.gridx = 2;
		gbc_lblHelpMac.gridy = 5;
		panel.add(getLblHelpMac(), gbc_lblHelpMac);
		GridBagConstraints gbc_lblProxytimeout = new GridBagConstraints();
		gbc_lblProxytimeout.insets = new Insets(0, 0, 0, 5);
		gbc_lblProxytimeout.anchor = GridBagConstraints.EAST;
		gbc_lblProxytimeout.gridx = 1;
		gbc_lblProxytimeout.gridy = 6;
		panel.add(getLblProxytimeout(), gbc_lblProxytimeout);
		GridBagConstraints gbc_txtProxy = new GridBagConstraints();
		gbc_txtProxy.insets = new Insets(0, 0, 0, 5);
		gbc_txtProxy.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProxy.gridx = 2;
		gbc_txtProxy.gridy = 6;
		panel.add(getTxtProxy(), gbc_txtProxy);
		return panel;
	}
	
	protected JComponent makePanelTemporal() {
		JPanel panel = new JPanel(false);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 158, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		panel.add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.anchor = GridBagConstraints.EAST;
		gbc_label_8.insets = new Insets(0, 0, 5, 5);
		gbc_label_8.gridx = 2;
		gbc_label_8.gridy = 1;
		panel.add(getLabel_8(), gbc_label_8);
		GridBagConstraints gbc_txtIterMGCRF = new GridBagConstraints();
		gbc_txtIterMGCRF.fill = GridBagConstraints.BOTH;
		gbc_txtIterMGCRF.insets = new Insets(0, 0, 5, 5);
		gbc_txtIterMGCRF.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtIterMGCRF.gridx = 3;
		gbc_txtIterMGCRF.gridy = 1;
		panel.add(getTxtIterMGCRF(), gbc_txtIterMGCRF);
		GridBagConstraints gbc_label_9 = new GridBagConstraints();
		gbc_label_9.insets = new Insets(0, 0, 5, 0);
		gbc_label_9.fill = GridBagConstraints.BOTH;
		gbc_label_9.gridwidth = 16;
		gbc_label_9.gridx = 0;
		gbc_label_9.gridy = 3;
		panel.add(getLabel_9(), gbc_label_9);
		GridBagConstraints gbc_label_16 = new GridBagConstraints();
		gbc_label_16.insets = new Insets(0, 0, 5, 5);
		gbc_label_16.gridx = 2;
		gbc_label_16.gridy = 4;
		panel.add(getLabel_16(), gbc_label_16);
		GridBagConstraints gbc_label_10 = new GridBagConstraints();
		gbc_label_10.anchor = GridBagConstraints.EAST;
		gbc_label_10.insets = new Insets(0, 0, 5, 5);
		gbc_label_10.gridx = 2;
		gbc_label_10.gridy = 5;
		panel.add(getLabel_10(), gbc_label_10);
		GridBagConstraints gbc_txtAlphaReg = new GridBagConstraints();
		gbc_txtAlphaReg.insets = new Insets(0, 0, 5, 5);
		gbc_txtAlphaReg.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAlphaReg.gridx = 3;
		gbc_txtAlphaReg.gridy = 5;
		panel.add(getTxtAlphaReg(), gbc_txtAlphaReg);
		GridBagConstraints gbc_label_11 = new GridBagConstraints();
		gbc_label_11.anchor = GridBagConstraints.EAST;
		gbc_label_11.insets = new Insets(0, 0, 5, 5);
		gbc_label_11.gridx = 2;
		gbc_label_11.gridy = 6;
		panel.add(getLabel_11(), gbc_label_11);
		GridBagConstraints gbc_txtBetaReg = new GridBagConstraints();
		gbc_txtBetaReg.insets = new Insets(0, 0, 5, 5);
		gbc_txtBetaReg.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtBetaReg.gridx = 3;
		gbc_txtBetaReg.gridy = 6;
		panel.add(getTxtBetaReg(), gbc_txtBetaReg);
		GridBagConstraints gbc_label_12 = new GridBagConstraints();
		gbc_label_12.insets = new Insets(0, 0, 5, 0);
		gbc_label_12.fill = GridBagConstraints.BOTH;
		gbc_label_12.gridwidth = 16;
		gbc_label_12.gridx = 0;
		gbc_label_12.gridy = 8;
		panel.add(getLabel_12(), gbc_label_12);
		GridBagConstraints gbc_label_17 = new GridBagConstraints();
		gbc_label_17.insets = new Insets(0, 0, 5, 5);
		gbc_label_17.gridx = 2;
		gbc_label_17.gridy = 9;
		panel.add(getLabel_17(), gbc_label_17);
		GridBagConstraints gbc_label_18 = new GridBagConstraints();
		gbc_label_18.anchor = GridBagConstraints.EAST;
		gbc_label_18.insets = new Insets(0, 0, 5, 5);
		gbc_label_18.gridx = 2;
		gbc_label_18.gridy = 10;
		panel.add(getLabel_18(), gbc_label_18);
		GridBagConstraints gbc_txtLambda = new GridBagConstraints();
		gbc_txtLambda.insets = new Insets(0, 0, 5, 5);
		gbc_txtLambda.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLambda.gridx = 3;
		gbc_txtLambda.gridy = 10;
		panel.add(getTxtLambda(), gbc_txtLambda);
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.fill = GridBagConstraints.BOTH;
		gbc_button_2.insets = new Insets(0, 0, 5, 5);
		gbc_button_2.gridx = 4;
		gbc_button_2.gridy = 10;
		panel.add(getButton_2(), gbc_button_2);
		GridBagConstraints gbc_label_19 = new GridBagConstraints();
		gbc_label_19.anchor = GridBagConstraints.EAST;
		gbc_label_19.insets = new Insets(0, 0, 5, 5);
		gbc_label_19.gridx = 2;
		gbc_label_19.gridy = 11;
		panel.add(getLabel_19(), gbc_label_19);
		GridBagConstraints gbc_txtRlsrHidden = new GridBagConstraints();
		gbc_txtRlsrHidden.insets = new Insets(0, 0, 5, 5);
		gbc_txtRlsrHidden.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRlsrHidden.gridx = 3;
		gbc_txtRlsrHidden.gridy = 11;
		panel.add(getTxtRlsrHidden(), gbc_txtRlsrHidden);
		GridBagConstraints gbc_label_20 = new GridBagConstraints();
		gbc_label_20.anchor = GridBagConstraints.EAST;
		gbc_label_20.insets = new Insets(0, 0, 5, 5);
		gbc_label_20.gridx = 2;
		gbc_label_20.gridy = 12;
		panel.add(getLabel_20(), gbc_label_20);
		GridBagConstraints gbc_txtRlsrIterNN = new GridBagConstraints();
		gbc_txtRlsrIterNN.insets = new Insets(0, 0, 5, 5);
		gbc_txtRlsrIterNN.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRlsrIterNN.gridx = 3;
		gbc_txtRlsrIterNN.gridy = 12;
		panel.add(getTxtRlsrIterNN(), gbc_txtRlsrIterNN);
		GridBagConstraints gbc_label_21 = new GridBagConstraints();
		gbc_label_21.anchor = GridBagConstraints.EAST;
		gbc_label_21.insets = new Insets(0, 0, 5, 5);
		gbc_label_21.gridx = 2;
		gbc_label_21.gridy = 13;
		panel.add(getLabel_21(), gbc_label_21);
		GridBagConstraints gbc_txtSseIter = new GridBagConstraints();
		gbc_txtSseIter.insets = new Insets(0, 0, 5, 5);
		gbc_txtSseIter.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSseIter.gridx = 3;
		gbc_txtSseIter.gridy = 13;
		panel.add(getTxtSseIter(), gbc_txtSseIter);
		GridBagConstraints gbc_label_22 = new GridBagConstraints();
		gbc_label_22.anchor = GridBagConstraints.EAST;
		gbc_label_22.insets = new Insets(0, 0, 0, 5);
		gbc_label_22.gridx = 2;
		gbc_label_22.gridy = 14;
		panel.add(getLabel_22(), gbc_label_22);
		GridBagConstraints gbc_txtLsIter = new GridBagConstraints();
		gbc_txtLsIter.insets = new Insets(0, 0, 0, 5);
		gbc_txtLsIter.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLsIter.gridx = 3;
		gbc_txtLsIter.gridy = 14;
		panel.add(getTxtLsIter(), gbc_txtLsIter);
		return panel;
	}

	
	protected JComponent makePanelNN() {
		JPanel panel = new JPanel(false);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		GridBagConstraints gbc_label_13 = new GridBagConstraints();
		gbc_label_13.insets = new Insets(0, 0, 5, 5);
		gbc_label_13.gridx = 1;
		gbc_label_13.gridy = 0;
		panel.add(getLabel_13(), gbc_label_13);
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.anchor = GridBagConstraints.EAST;
		gbc_label_6.insets = new Insets(0, 0, 5, 5);
		gbc_label_6.gridx = 1;
		gbc_label_6.gridy = 1;
		panel.add(getLabel_6(), gbc_label_6);
		GridBagConstraints gbc_txtHidden = new GridBagConstraints();
		gbc_txtHidden.insets = new Insets(0, 0, 5, 5);
		gbc_txtHidden.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHidden.gridx = 2;
		gbc_txtHidden.gridy = 1;
		panel.add(getTxtHidden(), gbc_txtHidden);
		GridBagConstraints gbc_label_7 = new GridBagConstraints();
		gbc_label_7.anchor = GridBagConstraints.EAST;
		gbc_label_7.insets = new Insets(0, 0, 5, 5);
		gbc_label_7.gridx = 1;
		gbc_label_7.gridy = 2;
		panel.add(getLabel_7(), gbc_label_7);
		GridBagConstraints gbc_txtIterNN = new GridBagConstraints();
		gbc_txtIterNN.insets = new Insets(0, 0, 5, 5);
		gbc_txtIterNN.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIterNN.gridx = 2;
		gbc_txtIterNN.gridy = 2;
		panel.add(getTxtIterNN(), gbc_txtIterNN);
		return panel;
	}

	protected JComponent makePanelRandom() {
		JPanel panel = new JPanel(false);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{38, 45, 180, 36, 136, 0};
		gbl_panel.rowHeights = new int[]{0, 25, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		GridBagConstraints gbc_label_15 = new GridBagConstraints();
		gbc_label_15.insets = new Insets(0, 0, 5, 5);
		gbc_label_15.gridx = 1;
		gbc_label_15.gridy = 0;
		panel.add(getLabel_15(), gbc_label_15);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 1;
		panel.add(getLabel(), gbc_label);
		GridBagConstraints gbc_txtAlphaGen = new GridBagConstraints();
		gbc_txtAlphaGen.fill = GridBagConstraints.BOTH;
		gbc_txtAlphaGen.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtAlphaGen.insets = new Insets(0, 0, 5, 5);
		gbc_txtAlphaGen.gridx = 2;
		gbc_txtAlphaGen.gridy = 1;
		panel.add(getTxtAlphaGen(), gbc_txtAlphaGen);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 2;
		panel.add(getLabel_1(), gbc_label_1);
		GridBagConstraints gbc_txtBetaGen = new GridBagConstraints();
		gbc_txtBetaGen.fill = GridBagConstraints.BOTH;
		gbc_txtBetaGen.insets = new Insets(0, 0, 0, 5);
		gbc_txtBetaGen.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtBetaGen.gridx = 2;
		gbc_txtBetaGen.gridy = 2;
		panel.add(getTxtBetaGen(), gbc_txtBetaGen);
		return panel;
	}

	protected JComponent makePanelGCRFDirGCRF() {
		JPanel panel = new JPanel(false);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		GridBagConstraints gbc_label_14 = new GridBagConstraints();
		gbc_label_14.insets = new Insets(0, 0, 5, 5);
		gbc_label_14.gridx = 1;
		gbc_label_14.gridy = 0;
		panel.add(getLabel_14(), gbc_label_14);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 1;
		gbc_label_2.gridy = 1;
		panel.add(getLabel_2(), gbc_label_2);
		GridBagConstraints gbc_txtAlpha = new GridBagConstraints();
		gbc_txtAlpha.insets = new Insets(0, 0, 5, 5);
		gbc_txtAlpha.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAlpha.gridx = 2;
		gbc_txtAlpha.gridy = 1;
		panel.add(getTxtAlpha(), gbc_txtAlpha);
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 1;
		gbc_label_3.gridy = 2;
		panel.add(getLabel_3(), gbc_label_3);
		GridBagConstraints gbc_txtBeta = new GridBagConstraints();
		gbc_txtBeta.insets = new Insets(0, 0, 5, 5);
		gbc_txtBeta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtBeta.gridx = 2;
		gbc_txtBeta.gridy = 2;
		panel.add(getTxtBeta(), gbc_txtBeta);
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 1;
		gbc_label_4.gridy = 3;
		panel.add(getLabel_4(), gbc_label_4);
		GridBagConstraints gbc_txtLR = new GridBagConstraints();
		gbc_txtLR.insets = new Insets(0, 0, 5, 5);
		gbc_txtLR.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLR.gridx = 2;
		gbc_txtLR.gridy = 3;
		panel.add(getTxtLR(), gbc_txtLR);
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.EAST;
		gbc_label_5.insets = new Insets(0, 0, 5, 5);
		gbc_label_5.gridx = 1;
		gbc_label_5.gridy = 4;
		panel.add(getLabel_5(), gbc_label_5);
		GridBagConstraints gbc_txtIter = new GridBagConstraints();
		gbc_txtIter.insets = new Insets(0, 0, 5, 5);
		gbc_txtIter.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIter.gridx = 2;
		gbc_txtIter.gridy = 4;
		panel.add(getTxtIter(), gbc_txtIter);
		return panel;
	}


	
	private JLabel getLblManageDatasets() {
		if (lblManageDatasets == null) {
			lblManageDatasets = new JLabel("CONFIGURE PARAMETERS:");
			lblManageDatasets.setOpaque(true);
			lblManageDatasets.setHorizontalAlignment(SwingConstants.CENTER);
			lblManageDatasets.setForeground(Color.WHITE);
			lblManageDatasets.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblManageDatasets.setBackground(Color.GRAY);
		}
		return lblManageDatasets;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Alpha:");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label;
	}
	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("Beta:");
			label_1.setHorizontalAlignment(SwingConstants.RIGHT);
			label_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_1;
	}
	private JTextField getTxtAlphaGen() {
		if (txtAlphaGen == null) {
			txtAlphaGen = new JTextField();
			txtAlphaGen.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtAlphaGen.setColumns(10);
		}
		return txtAlphaGen;
	}
	private JTextField getTxtBetaGen() {
		if (txtBetaGen == null) {
			txtBetaGen = new JTextField();
			txtBetaGen.setText("");
			txtBetaGen.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtBetaGen.setColumns(10);
		}
		return txtBetaGen;
	}
	private JLabel getLabel_2() {
		if (label_2 == null) {
			label_2 = new JLabel("Alpha:");
			label_2.setHorizontalAlignment(SwingConstants.RIGHT);
			label_2.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_2;
	}
	private JTextField getTxtAlpha() {
		if (txtAlpha == null) {
			txtAlpha = new JTextField();
			txtAlpha.setText("");
			txtAlpha.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtAlpha.setColumns(10);
		}
		return txtAlpha;
	}
	private JLabel getLabel_3() {
		if (label_3 == null) {
			label_3 = new JLabel("Beta:");
			label_3.setHorizontalAlignment(SwingConstants.RIGHT);
			label_3.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_3;
	}
	private JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel("Learning rate:");
			label_4.setHorizontalAlignment(SwingConstants.RIGHT);
			label_4.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_4;
	}
	private JLabel getLabel_5() {
		if (label_5 == null) {
			label_5 = new JLabel("Max. iterations:");
			label_5.setHorizontalAlignment(SwingConstants.RIGHT);
			label_5.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_5;
	}
	private JTextField getTxtBeta() {
		if (txtBeta == null) {
			txtBeta = new JTextField();
			txtBeta.setText("");
			txtBeta.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtBeta.setColumns(10);
		}
		return txtBeta;
	}
	private JTextField getTxtLR() {
		if (txtLR == null) {
			txtLR = new JTextField();
			txtLR.setText("");
			txtLR.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLR.setColumns(10);
		}
		return txtLR;
	}
	private JTextField getTxtIter() {
		if (txtIter == null) {
			txtIter = new JTextField();
			txtIter.setText("");
			txtIter.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtIter.setColumns(10);
		}
		return txtIter;
	}
	private JLabel getLabel_6() {
		if (label_6 == null) {
			label_6 = new JLabel("No. of hidden neurons:");
			label_6.setHorizontalAlignment(SwingConstants.RIGHT);
			label_6.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_6;
	}
	private JLabel getLabel_7() {
		if (label_7 == null) {
			label_7 = new JLabel("No. of iterations:");
			label_7.setHorizontalAlignment(SwingConstants.RIGHT);
			label_7.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_7;
	}
	private JTextField getTxtHidden() {
		if (txtHidden == null) {
			txtHidden = new JTextField();
			txtHidden.setText("");
			txtHidden.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtHidden.setColumns(10);
		}
		return txtHidden;
	}
	private JTextField getTxtIterNN() {
		if (txtIterNN == null) {
			txtIterNN = new JTextField();
			txtIterNN.setText("");
			txtIterNN.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtIterNN.setColumns(10);
		}
		return txtIterNN;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			GridBagLayout gbl_panel_1 = new GridBagLayout();
			gbl_panel_1.columnWidths = new int[]{111, 107, 115, 0};
			gbl_panel_1.rowHeights = new int[]{23, 0};
			gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel_1.setLayout(gbl_panel_1);
			GridBagConstraints gbc_btnSave = new GridBagConstraints();
			gbc_btnSave.fill = GridBagConstraints.BOTH;
			gbc_btnSave.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnSave.insets = new Insets(0, 0, 0, 5);
			gbc_btnSave.gridx = 1;
			gbc_btnSave.gridy = 0;
			panel_1.add(getBtnSave(), gbc_btnSave);
			GridBagConstraints gbc_btnResetToDefaults = new GridBagConstraints();
			gbc_btnResetToDefaults.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnResetToDefaults.gridx = 2;
			gbc_btnResetToDefaults.gridy = 0;
			panel_1.add(getBtnResetToDefaults(), gbc_btnResetToDefaults);
		}
		return panel_1;
	}
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton("SAVE");
			Style.buttonStyle(btnSave);
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String message = validateData();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message,
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						String[] text = prepareForFile();
						Writer.write(text, Reader.jarFile() + "/cfg.txt");
						MainFrame m = (MainFrame) mainFrame;
						m.enableMenu();
						panel.removeAll();
						panel.repaint();
						panel.revalidate();
					}
				}
			});
		}
		return btnSave;
	}
	
	public String[] prepareForFile() {
		String[] text = new String[19];
		alphaGen = Integer.parseInt(txtAlphaGen.getText());
		betaGen = Integer.parseInt(txtBetaGen.getText());
		alpha = Integer.parseInt(txtAlpha.getText());
		beta = Integer.parseInt(txtBeta.getText());
		lr = Double.parseDouble(txtLR.getText());
		iterations = Integer.parseInt(txtIter.getText());
		hidden = Integer.parseInt(txtHidden.getText());
		iterNN = Integer.parseInt(txtIterNN.getText());
		alphaReg = Integer.parseInt(txtAlphaReg.getText());
		betaReg = Integer.parseInt(txtBetaReg.getText());
		iterTemp = Integer.parseInt(txtIterMGCRF.getText());
		lambda = txtLambda.getText();
		rlsrHidden = Integer.parseInt(txtRlsrHidden.getText());
		rlsrIterNN = Integer.parseInt(txtRlsrIterNN.getText());
		sseIter = Integer.parseInt(txtSseIter.getText());
		lsIter = Integer.parseInt(txtLsIter.getText());
		useMatlab = chckMatlab.isSelected();
		if (useMatlab) {
			proxy = Integer.parseInt(txtProxy.getText())*1000L;
		}
		matlabPath = txtPath.getText();
		text[0] = "AlphaGen=" + alphaGen;
		text[1] = "BetaGen=" + betaGen;
		text[2] = "Alpha=" + alpha;
		text[3] = "Beta=" + beta;
		text[4] = "LR=" + lr;
		text[5] = "Iterations=" + iterations;
		text[6] = "NN hidden=" + hidden;
		text[7] = "Iterations NN=" + iterNN;
		text[8] = "Iterations temporal=" + iterTemp;
		text[9] = "AlphaReg=" + alphaReg;
		text[10] = "BetaReg=" + betaReg;
		text[11] = "Lambda=" + lambda;
		text[12] = "RLSR hidden NN=" + rlsrHidden;
		text[13] = "RLSR iterations NN=" + rlsrIterNN;
		text[14] = "RLSR SSE iterations=" + sseIter;
		text[15] = "RLSR SSE LS iterations=" + lsIter;
		text[16] = "Use MATLAB=" + useMatlab;
		text[17] = "Path=" + matlabPath;
		text[18] = "Proxy=" + proxy;
		return text;
	}

	
	public String validateData() {
		try {
			int a = Integer.parseInt(txtAlphaGen.getText());
			if (a <= 0) {
				return "Alpha for data generation should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Alpha for data generation should be integer.";
		}
		try {
			int b = Integer.parseInt(txtBetaGen.getText());
			if (b <= 0) {
				return "Beta for data generation should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Beta for data generation should be integer.";
		}

		try {
			int a = Integer.parseInt(txtAlpha.getText());
			if (a <= 0) {
				return "Alpha should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Alpha should be integer.";
		}
		try {
			int b = Integer.parseInt(txtBeta.getText());
			if (b <= 0) {
				return "Beta should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Beta should be integer.";
		}
		try {
			double l = Double.parseDouble(txtLR.getText());
			if (l <= 0) {
				return "Learning rate should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Learning rate should be number.";
		}

		try {
			int i = Integer.parseInt(txtIter.getText());
			if (i <= 0) {
				return "Max. iterations should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Max. iterations should be integer.";
		}
		try {
			int b = Integer.parseInt(txtHidden.getText());
			if (b <= 0) {
				return "No. of hidden neurons for neural network should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of hidden neurons for neural network should be integer.";
		}

		try {
			int b = Integer.parseInt(txtIterNN.getText());
			if (b <= 0) {
				return "No. of iteration for neural network training should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of iteration for neural network training should be integer.";
		}

		try {
			int a = Integer.parseInt(txtAlphaReg.getText());
			if (a <= 0) {
				return "Alpha for regularization should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Alpha for regularization should be integer.";
		}
		try {
			int b = Integer.parseInt(txtBetaReg.getText());
			if (b <= 0) {
				return "Beta for regularization should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "Beta for regularization should be integer.";
		}

		try {
			int b = Integer.parseInt(txtIterMGCRF.getText());
			if (b <= 0) {
				return "No. of iteration for temporal methods should be greater than 0.";
			}
		} catch (NumberFormatException e) {
			return "No. of iteration for temporal methods should be integer.";
		}

		if (chckMatlab.isSelected() && txtPath.getText().equals("")) {
			return "Choose path to MATLAB.exe or deselect 'Use methods implemented in MATLAB' check box.";
		}
		return null;
	}
	
	private JButton getBtnResetToDefaults() {
		if (btnResetToDefaults == null) {
			btnResetToDefaults = new JButton("Reset to defaults");
			Style.buttonStyle(btnResetToDefaults);
			btnResetToDefaults.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setUpDefaultValues();
					setTxtValues();
				}
			});
		}
		return btnResetToDefaults;
	}
	private JLabel getLabel_8() {
		if (label_8 == null) {
			label_8 = new JLabel("Max. iterations:");
			label_8.setHorizontalAlignment(SwingConstants.RIGHT);
			label_8.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_8;
	}
	private JTextField getTxtIterMGCRF() {
		if (txtIterMGCRF == null) {
			txtIterMGCRF = new JTextField();
			txtIterMGCRF.setText("0");
			txtIterMGCRF.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtIterMGCRF.setColumns(10);
		}
		return txtIterMGCRF;
	}
	private JLabel getLabel_9() {
		if (label_9 == null) {
			label_9 = new JLabel("Parameters for training m-GCRF:");
			label_9.setOpaque(true);
			label_9.setHorizontalAlignment(SwingConstants.CENTER);
			label_9.setForeground(Color.WHITE);
			label_9.setFont(new Font("Segoe UI", Font.BOLD, 15));
			label_9.setBackground(Color.GRAY);
		}
		return label_9;
	}
	private JLabel getLabel_10() {
		if (label_10 == null) {
			label_10 = new JLabel("Regularization alpha:");
			label_10.setHorizontalAlignment(SwingConstants.RIGHT);
			label_10.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_10;
	}
	private JLabel getLabel_11() {
		if (label_11 == null) {
			label_11 = new JLabel("Regularization beta:");
			label_11.setHorizontalAlignment(SwingConstants.RIGHT);
			label_11.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_11;
	}
	private JTextField getTxtAlphaReg() {
		if (txtAlphaReg == null) {
			txtAlphaReg = new JTextField();
			txtAlphaReg.setText("0");
			txtAlphaReg.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtAlphaReg.setColumns(10);
		}
		return txtAlphaReg;
	}
	private JTextField getTxtBetaReg() {
		if (txtBetaReg == null) {
			txtBetaReg = new JTextField();
			txtBetaReg.setText("0");
			txtBetaReg.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtBetaReg.setColumns(10);
		}
		return txtBetaReg;
	}
	private JLabel getLabel_12() {
		if (label_12 == null) {
			label_12 = new JLabel("Parameters for training RLSR:");
			label_12.setOpaque(true);
			label_12.setHorizontalAlignment(SwingConstants.CENTER);
			label_12.setForeground(Color.WHITE);
			label_12.setFont(new Font("Segoe UI", Font.BOLD, 15));
			label_12.setBackground(Color.GRAY);
		}
		return label_12;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("New label");
			lblNewLabel.setForeground(SystemColor.control);
		}
		return lblNewLabel;
	}
	private JLabel getLabel_13() {
		if (label_13 == null) {
			label_13 = new JLabel("New label");
			label_13.setForeground(SystemColor.menu);
		}
		return label_13;
	}
	private JLabel getLabel_14() {
		if (label_14 == null) {
			label_14 = new JLabel("New label");
			label_14.setForeground(SystemColor.menu);
		}
		return label_14;
	}
	private JLabel getLabel_15() {
		if (label_15 == null) {
			label_15 = new JLabel("New label");
			label_15.setForeground(SystemColor.menu);
		}
		return label_15;
	}
	private JLabel getLabel_16() {
		if (label_16 == null) {
			label_16 = new JLabel("New label");
			label_16.setForeground(SystemColor.menu);
		}
		return label_16;
	}
	private JLabel getLabel_17() {
		if (label_17 == null) {
			label_17 = new JLabel("New label");
			label_17.setForeground(SystemColor.menu);
		}
		return label_17;
	}
	private JLabel getLabel_18() {
		if (label_18 == null) {
			label_18 = new JLabel("Lambda set:");
			label_18.setHorizontalAlignment(SwingConstants.RIGHT);
			label_18.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_18;
	}
	private JTextField getTxtLambda() {
		if (txtLambda == null) {
			txtLambda = new JTextField();
			txtLambda.setText((String) null);
			txtLambda.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLambda.setColumns(10);
		}
		return txtLambda;
	}
	private JButton getButton_2() {
		if (button_2 == null) {
			button_2 = new JButton("");
			Style.questionButtonStyle(button_2);
			button_2.addActionListener(new ActionListener() {
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
		return button_2;
	}
	private JLabel getLabel_19() {
		if (label_19 == null) {
			label_19 = new JLabel("No. of hidden neurons for NN:");
			label_19.setHorizontalAlignment(SwingConstants.RIGHT);
			label_19.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_19;
	}
	private JLabel getLabel_20() {
		if (label_20 == null) {
			label_20 = new JLabel("No. of iterations for NN:");
			label_20.setHorizontalAlignment(SwingConstants.RIGHT);
			label_20.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_20;
	}
	private JLabel getLabel_21() {
		if (label_21 == null) {
			label_21 = new JLabel("SSE max. iterations:");
			label_21.setHorizontalAlignment(SwingConstants.RIGHT);
			label_21.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_21;
	}
	private JLabel getLabel_22() {
		if (label_22 == null) {
			label_22 = new JLabel("SSE LS max. iterations:");
			label_22.setHorizontalAlignment(SwingConstants.RIGHT);
			label_22.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_22;
	}
	private JTextField getTxtRlsrHidden() {
		if (txtRlsrHidden == null) {
			txtRlsrHidden = new JTextField();
			txtRlsrHidden.setText("0");
			txtRlsrHidden.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtRlsrHidden.setColumns(10);
		}
		return txtRlsrHidden;
	}
	private JTextField getTxtRlsrIterNN() {
		if (txtRlsrIterNN == null) {
			txtRlsrIterNN = new JTextField();
			txtRlsrIterNN.setText("0");
			txtRlsrIterNN.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtRlsrIterNN.setColumns(10);
		}
		return txtRlsrIterNN;
	}
	private JTextField getTxtSseIter() {
		if (txtSseIter == null) {
			txtSseIter = new JTextField();
			txtSseIter.setText("");
			txtSseIter.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtSseIter.setColumns(10);
		}
		return txtSseIter;
	}
	private JTextField getTxtLsIter() {
		if (txtLsIter == null) {
			txtLsIter = new JTextField();
			txtLsIter.setText("");
			txtLsIter.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtLsIter.setColumns(10);
		}
		return txtLsIter;
	}
	private JLabel getLabel_23() {
		if (label_23 == null) {
			label_23 = new JLabel("New label");
			label_23.setForeground(SystemColor.menu);
		}
		return label_23;
	}
	private JLabel getLabel_24() {
		if (label_24 == null) {
			label_24 = new JLabel("New label");
			label_24.setForeground(SystemColor.menu);
		}
		return label_24;
	}
	private JLabel getLblPathToMatlabexe() {
		if (lblPathToMatlabexe == null) {
			lblPathToMatlabexe = new JLabel("Path to MATLAB.exe:");
			lblPathToMatlabexe.setHorizontalAlignment(SwingConstants.LEFT);
			lblPathToMatlabexe.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblPathToMatlabexe.setEnabled(false);
		}
		return lblPathToMatlabexe;
	}
	private JLabel getLblProxytimeout() {
		if (lblProxytimeout == null) {
			lblProxytimeout = new JLabel("Proxy timeout (seconds):");
			lblProxytimeout.setHorizontalAlignment(SwingConstants.LEFT);
			lblProxytimeout.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblProxytimeout.setEnabled(false);
		}
		return lblProxytimeout;
	}
	private JTextField getTxtProxy() {
		if (txtProxy == null) {
			txtProxy = new JTextField();
			txtProxy.setText("");
			txtProxy.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtProxy.setEnabled(false);
			txtProxy.setColumns(10);
		}
		return txtProxy;
	}
	private JTextField getTxtPath() {
		if (txtPath == null) {
			txtPath = new JTextField();
			txtPath.setText((String) null);
			txtPath.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtPath.setEnabled(false);
			txtPath.setColumns(10);
		}
		return txtPath;
	}
	private JButton getBtnBrowse() {
		if (btnBrowse == null) {
			btnBrowse = new JButton("Browse");
			btnBrowse.setEnabled(false);
			btnBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chooseFile(txtPath);
				}
			});
			Style.buttonStyle(btnBrowse);
		}
		return btnBrowse;
	}
	
	public void chooseFile(JTextField txt) {
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			txt.setText(fc.getSelectedFile().getPath());
		}
	}
	private JLabel getLblHelp() {
		if (lblHelp == null) {
			lblHelp = new JLabel("Example for Windows: C:/Program Files/MATLAB/[Version]/bin/matlab.exe");
			lblHelp.setEnabled(false);
		}
		return lblHelp;
	}
	private JCheckBox getChckMatlab() {
		if (chckMatlab == null) {
			chckMatlab = new JCheckBox("Use methods implemented in MATLAB");
			chckMatlab.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if(chckMatlab.isSelected()){
						txtPath.setEnabled(true);
						btnBrowse.setEnabled(true);
						txtProxy.setEnabled(true);
						lblPathToMatlabexe.setEnabled(true);
						lblHelp.setEnabled(true);
						lblProxytimeout.setEnabled(true);
						lblHelpMac.setEnabled(true);
					}else{
						txtPath.setEnabled(false);
						btnBrowse.setEnabled(false);
						txtProxy.setEnabled(false);
						lblPathToMatlabexe.setEnabled(false);
						lblHelp.setEnabled(false);
						lblProxytimeout.setEnabled(false);
						lblHelpMac.setEnabled(false);
					}
				}
			});
		}
		return chckMatlab;
	}
	private JLabel getLblHelpMac() {
		if (lblHelpMac == null) {
			lblHelpMac = new JLabel("For MAC OSx type folowing:  /Applications/MATLAB_[Version].app/bin/matlab");
			lblHelpMac.setEnabled(false);
		}
		return lblHelpMac;
	}
}
