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

import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.style.Style;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class AddDatasetPanel extends JPanel {

	private static final long serialVersionUID = 7542338256284881226L;
	private JTextField txtSFile;
	private JLabel lblFile;
	private JButton btnBrowseS;
	private JButton btnQuestionS;
	private JLabel lblModelName;
	private JTextField txtName;
	private JLabel lblRArrayFile;
	private JTextField txtXFile;
	private JButton btnBrowseX;
	private JButton btnQuestionX;
	private JLabel lblYArrayFile;
	private JTextField txtYFile;
	private JButton btnBrowseY;
	private JButton btnQuestionY;
	private JButton btnTrain;
	private JFileChooser fc;
	private JPanel panel;
	private JFrame mainFrame;
	private JLabel label;
	private JTextField txtNodes;

	private JLabel lblData;
	private JLabel lblTestData;
	private JTextField txtSTestFile;
	private JLabel label_1;
	private JButton btnBrowseSTest;
	private JButton btnQuestionSTest;
	private JLabel label_3;
	private JTextField txtXTestFile;
	private JButton btnBrowseXTest;
	private JButton btnQuestionXTest;
	private JLabel label_4;
	private JTextField txtYTestFile;
	private JButton btnBrowseYTest;
	private JButton btnQuestionYTest;
	private JLabel label_5;
	private JTextField txtNodesTest;
	private JCheckBox chkTogether;
	private JLabel lblAttributes;
	private JTextField txtAttributes;
	private JLabel lblTimePoints;
	private JTextField txtTimePoints;
	private JCheckBox chkLearn;

	public AddDatasetPanel(JFrame mainFrame) {
		setBounds(new Rectangle(0, 0, 900, 650));
		setMinimumSize(new Dimension(500, 500));

		setBackground(UIManager.getColor("Button.background"));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{198, 91, 212, 100, 30, 190, 0};
		gridBagLayout.rowHeights = new int[]{30, 32, 32, 30, 30, 31, 33, 30, 30, 30, 30, 31, 23, 30, 30, 45, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblData = new GridBagConstraints();
		gbc_lblData.gridwidth = 10;
		gbc_lblData.fill = GridBagConstraints.BOTH;
		gbc_lblData.insets = new Insets(0, 0, 5, 0);
		gbc_lblData.gridx = 0;
		gbc_lblData.gridy = 0;
		add(getLblData(), gbc_lblData);
		GridBagConstraints gbc_lblModelName = new GridBagConstraints();
		gbc_lblModelName.fill = GridBagConstraints.BOTH;
		gbc_lblModelName.insets = new Insets(0, 0, 5, 5);
		gbc_lblModelName.gridx = 0;
		gbc_lblModelName.gridy = 1;
		add(getLblModelName(), gbc_lblModelName);
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.fill = GridBagConstraints.BOTH;
		gbc_txtName.insets = new Insets(0, 0, 5, 5);
		gbc_txtName.gridwidth = 2;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 1;
		add(getTxtName(), gbc_txtName);
		GridBagConstraints gbc_lblFile = new GridBagConstraints();
		gbc_lblFile.anchor = GridBagConstraints.EAST;
		gbc_lblFile.fill = GridBagConstraints.VERTICAL;
		gbc_lblFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblFile.gridx = 0;
		gbc_lblFile.gridy = 2;
		add(getLblFile(), gbc_lblFile);
		GridBagConstraints gbc_txtSFile = new GridBagConstraints();
		gbc_txtSFile.fill = GridBagConstraints.BOTH;
		gbc_txtSFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtSFile.gridwidth = 2;
		gbc_txtSFile.gridx = 1;
		gbc_txtSFile.gridy = 2;
		add(getTxtSFile(), gbc_txtSFile);
		GridBagConstraints gbc_btnBrowseS = new GridBagConstraints();
		gbc_btnBrowseS.fill = GridBagConstraints.BOTH;
		gbc_btnBrowseS.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseS.gridx = 3;
		gbc_btnBrowseS.gridy = 2;
		add(getBtnBrowseS(), gbc_btnBrowseS);
		GridBagConstraints gbc_btnQuestionS = new GridBagConstraints();
		gbc_btnQuestionS.fill = GridBagConstraints.BOTH;
		gbc_btnQuestionS.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuestionS.gridx = 4;
		gbc_btnQuestionS.gridy = 2;
		add(getBtnQuestionS(), gbc_btnQuestionS);
		GridBagConstraints gbc_chkLearn = new GridBagConstraints();
		gbc_chkLearn.anchor = GridBagConstraints.SOUTHWEST;
		gbc_chkLearn.insets = new Insets(0, 0, 5, 0);
		gbc_chkLearn.gridx = 5;
		gbc_chkLearn.gridy = 2;
		add(getChkLearn(), gbc_chkLearn);
		GridBagConstraints gbc_lblRArrayFile = new GridBagConstraints();
		gbc_lblRArrayFile.anchor = GridBagConstraints.EAST;
		gbc_lblRArrayFile.fill = GridBagConstraints.VERTICAL;
		gbc_lblRArrayFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblRArrayFile.gridx = 0;
		gbc_lblRArrayFile.gridy = 3;
		add(getLblRArrayFile(), gbc_lblRArrayFile);
		GridBagConstraints gbc_txtXFile = new GridBagConstraints();
		gbc_txtXFile.fill = GridBagConstraints.BOTH;
		gbc_txtXFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtXFile.gridwidth = 2;
		gbc_txtXFile.gridx = 1;
		gbc_txtXFile.gridy = 3;
		add(getTxtXFile(), gbc_txtXFile);
		GridBagConstraints gbc_btnBrowseX = new GridBagConstraints();
		gbc_btnBrowseX.fill = GridBagConstraints.BOTH;
		gbc_btnBrowseX.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseX.gridx = 3;
		gbc_btnBrowseX.gridy = 3;
		add(getBtnBrowseX(), gbc_btnBrowseX);
		GridBagConstraints gbc_btnQuestionX = new GridBagConstraints();
		gbc_btnQuestionX.fill = GridBagConstraints.BOTH;
		gbc_btnQuestionX.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuestionX.gridx = 4;
		gbc_btnQuestionX.gridy = 3;
		add(getBtnQuestionX(), gbc_btnQuestionX);
		GridBagConstraints gbc_lblYArrayFile = new GridBagConstraints();
		gbc_lblYArrayFile.anchor = GridBagConstraints.EAST;
		gbc_lblYArrayFile.fill = GridBagConstraints.VERTICAL;
		gbc_lblYArrayFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblYArrayFile.gridx = 0;
		gbc_lblYArrayFile.gridy = 4;
		add(getLblYArrayFile(), gbc_lblYArrayFile);
		GridBagConstraints gbc_txtYFile = new GridBagConstraints();
		gbc_txtYFile.fill = GridBagConstraints.BOTH;
		gbc_txtYFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtYFile.gridwidth = 2;
		gbc_txtYFile.gridx = 1;
		gbc_txtYFile.gridy = 4;
		add(getTxtYFile(), gbc_txtYFile);
		GridBagConstraints gbc_btnBrowseY = new GridBagConstraints();
		gbc_btnBrowseY.fill = GridBagConstraints.BOTH;
		gbc_btnBrowseY.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseY.gridx = 3;
		gbc_btnBrowseY.gridy = 4;
		add(getBtnBrowseY(), gbc_btnBrowseY);
		GridBagConstraints gbc_btnQuestionY = new GridBagConstraints();
		gbc_btnQuestionY.fill = GridBagConstraints.BOTH;
		gbc_btnQuestionY.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuestionY.gridx = 4;
		gbc_btnQuestionY.gridy = 4;
		add(getBtnQuestionY(), gbc_btnQuestionY);
		// add(getLblTime());
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 5;
		add(getLabel(), gbc_label);
		GridBagConstraints gbc_txtNodes = new GridBagConstraints();
		gbc_txtNodes.fill = GridBagConstraints.BOTH;
		gbc_txtNodes.insets = new Insets(0, 0, 5, 5);
		gbc_txtNodes.gridx = 1;
		gbc_txtNodes.gridy = 5;
		add(getTxtNodes(), gbc_txtNodes);
		GridBagConstraints gbc_lblTestData = new GridBagConstraints();
		gbc_lblTestData.fill = GridBagConstraints.BOTH;
		gbc_lblTestData.insets = new Insets(0, 0, 5, 0);
		gbc_lblTestData.gridwidth = 10;
		gbc_lblTestData.gridx = 0;
		gbc_lblTestData.gridy = 7;
		add(getLblTestData(), gbc_lblTestData);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.fill = GridBagConstraints.VERTICAL;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 8;
		add(getLabel_1(), gbc_label_1);
		GridBagConstraints gbc_txtSTestFile = new GridBagConstraints();
		gbc_txtSTestFile.fill = GridBagConstraints.BOTH;
		gbc_txtSTestFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtSTestFile.gridwidth = 2;
		gbc_txtSTestFile.gridx = 1;
		gbc_txtSTestFile.gridy = 8;
		add(getTxtSTestFile(), gbc_txtSTestFile);
		GridBagConstraints gbc_btnBrowseSTest = new GridBagConstraints();
		gbc_btnBrowseSTest.fill = GridBagConstraints.BOTH;
		gbc_btnBrowseSTest.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseSTest.gridx = 3;
		gbc_btnBrowseSTest.gridy = 8;
		add(getBtnBrowseSTest(), gbc_btnBrowseSTest);
		GridBagConstraints gbc_btnQuestionSTest = new GridBagConstraints();
		gbc_btnQuestionSTest.fill = GridBagConstraints.BOTH;
		gbc_btnQuestionSTest.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuestionSTest.gridx = 4;
		gbc_btnQuestionSTest.gridy = 8;
		add(getBtnQuestionSTest(), gbc_btnQuestionSTest);
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.fill = GridBagConstraints.VERTICAL;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 9;
		add(getLabel_3(), gbc_label_3);
		GridBagConstraints gbc_txtXTestFile = new GridBagConstraints();
		gbc_txtXTestFile.fill = GridBagConstraints.BOTH;
		gbc_txtXTestFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtXTestFile.gridwidth = 2;
		gbc_txtXTestFile.gridx = 1;
		gbc_txtXTestFile.gridy = 9;
		add(getTxtXTestFile(), gbc_txtXTestFile);
		GridBagConstraints gbc_btnBrowseXTest = new GridBagConstraints();
		gbc_btnBrowseXTest.fill = GridBagConstraints.BOTH;
		gbc_btnBrowseXTest.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseXTest.gridx = 3;
		gbc_btnBrowseXTest.gridy = 9;
		add(getBtnBrowseXTest(), gbc_btnBrowseXTest);
		GridBagConstraints gbc_btnQuestionXTest = new GridBagConstraints();
		gbc_btnQuestionXTest.fill = GridBagConstraints.BOTH;
		gbc_btnQuestionXTest.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuestionXTest.gridx = 4;
		gbc_btnQuestionXTest.gridy = 9;
		add(getBtnQuestionXTest(), gbc_btnQuestionXTest);
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.fill = GridBagConstraints.VERTICAL;
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 10;
		add(getLabel_4(), gbc_label_4);
		GridBagConstraints gbc_txtYTestFile = new GridBagConstraints();
		gbc_txtYTestFile.fill = GridBagConstraints.BOTH;
		gbc_txtYTestFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtYTestFile.gridwidth = 2;
		gbc_txtYTestFile.gridx = 1;
		gbc_txtYTestFile.gridy = 10;
		add(getTxtYTestFile(), gbc_txtYTestFile);
		GridBagConstraints gbc_btnBrowseYTest = new GridBagConstraints();
		gbc_btnBrowseYTest.fill = GridBagConstraints.BOTH;
		gbc_btnBrowseYTest.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseYTest.gridx = 3;
		gbc_btnBrowseYTest.gridy = 10;
		add(getBtnBrowseYTest(), gbc_btnBrowseYTest);
		GridBagConstraints gbc_btnQuestionYTest = new GridBagConstraints();
		gbc_btnQuestionYTest.fill = GridBagConstraints.BOTH;
		gbc_btnQuestionYTest.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuestionYTest.gridx = 4;
		gbc_btnQuestionYTest.gridy = 10;
		add(getBtnQuestionYTest(), gbc_btnQuestionYTest);
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.EAST;
		gbc_label_5.fill = GridBagConstraints.VERTICAL;
		gbc_label_5.insets = new Insets(0, 0, 5, 5);
		gbc_label_5.gridx = 0;
		gbc_label_5.gridy = 11;
		add(getLabel_5(), gbc_label_5);
		GridBagConstraints gbc_txtNodesTest = new GridBagConstraints();
		gbc_txtNodesTest.fill = GridBagConstraints.BOTH;
		gbc_txtNodesTest.insets = new Insets(0, 0, 5, 5);
		gbc_txtNodesTest.gridx = 1;
		gbc_txtNodesTest.gridy = 11;
		add(getTxtNodesTest(), gbc_txtNodesTest);
		GridBagConstraints gbc_chkTogether = new GridBagConstraints();
		gbc_chkTogether.anchor = GridBagConstraints.NORTHWEST;
		gbc_chkTogether.insets = new Insets(0, 0, 5, 5);
		gbc_chkTogether.gridwidth = 2;
		gbc_chkTogether.gridx = 1;
		gbc_chkTogether.gridy = 12;
		add(getChkTogether(), gbc_chkTogether);
		GridBagConstraints gbc_lblTimePoints = new GridBagConstraints();
		gbc_lblTimePoints.anchor = GridBagConstraints.EAST;
		gbc_lblTimePoints.fill = GridBagConstraints.VERTICAL;
		gbc_lblTimePoints.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimePoints.gridx = 0;
		gbc_lblTimePoints.gridy = 13;
		add(getLblTimePoints(), gbc_lblTimePoints);
		GridBagConstraints gbc_txtTimePoints = new GridBagConstraints();
		gbc_txtTimePoints.fill = GridBagConstraints.BOTH;
		gbc_txtTimePoints.insets = new Insets(0, 0, 5, 5);
		gbc_txtTimePoints.gridx = 1;
		gbc_txtTimePoints.gridy = 13;
		add(getTxtTimePoints(), gbc_txtTimePoints);
		GridBagConstraints gbc_lblAttributes = new GridBagConstraints();
		gbc_lblAttributes.fill = GridBagConstraints.BOTH;
		gbc_lblAttributes.insets = new Insets(0, 0, 5, 5);
		gbc_lblAttributes.gridx = 0;
		gbc_lblAttributes.gridy = 14;
		add(getLblAttributes(), gbc_lblAttributes);
		GridBagConstraints gbc_txtAttributes = new GridBagConstraints();
		gbc_txtAttributes.fill = GridBagConstraints.BOTH;
		gbc_txtAttributes.insets = new Insets(0, 0, 5, 5);
		gbc_txtAttributes.gridx = 1;
		gbc_txtAttributes.gridy = 14;
		add(getTxtAttributes(), gbc_txtAttributes);
		GridBagConstraints gbc_btnTrain = new GridBagConstraints();
		gbc_btnTrain.anchor = GridBagConstraints.WEST;
		gbc_btnTrain.fill = GridBagConstraints.BOTH;
		gbc_btnTrain.insets = new Insets(0, 0, 0, 5);
		gbc_btnTrain.gridx = 2;
		gbc_btnTrain.gridy = 15;
		add(getBtnTrain(), gbc_btnTrain);
		fc = new JFileChooser();
		panel = this;
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"TEXT FILES", "txt", "text");
		fc.setFileFilter(filter);
		fc.setCurrentDirectory(Reader.jarFile());
		this.mainFrame = mainFrame;

	}

	private JTextField getTxtSFile() {
		if (txtSFile == null) {
			txtSFile = new JTextField();
			txtSFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtSFile.setColumns(10);
		}
		return txtSFile;
	}

	private JLabel getLblFile() {
		if (lblFile == null) {
			lblFile = new JLabel("File with edges:");
			lblFile.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFile.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblFile;
	}

	private JButton getBtnBrowseS() {
		if (btnBrowseS == null) {
			btnBrowseS = new JButton("Browse");
			btnBrowseS.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					chooseFile(txtSFile);
				}
			});
			Style.buttonStyle(btnBrowseS);

		}
		return btnBrowseS;
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
									"Text file (.txt) that contains data about connections between nodes for train data."
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
		}
		return btnQuestionS;
	}

	private JLabel getLblModelName() {
		if (lblModelName == null) {
			lblModelName = new JLabel("Dataset name:");
			lblModelName.setHorizontalAlignment(SwingConstants.RIGHT);
			lblModelName.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblModelName;
	}

	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
			txtName.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtName.setColumns(10);
		}
		return txtName;
	}

	private JLabel getLblRArrayFile() {
		if (lblRArrayFile == null) {
			lblRArrayFile = new JLabel("File with attributes:");
			lblRArrayFile.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRArrayFile.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblRArrayFile;
	}

	private JTextField getTxtXFile() {
		if (txtXFile == null) {
			txtXFile = new JTextField();
			txtXFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtXFile.setColumns(10);
		}
		return txtXFile;
	}

	private JButton getBtnBrowseX() {
		if (btnBrowseX == null) {
			btnBrowseX = new JButton("Browse");
			btnBrowseX.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chooseFile(txtXFile);
				}
			});
			Style.buttonStyle(btnBrowseX);
		}
		return btnBrowseX;
	}

	private JButton getBtnQuestionX() {
		if (btnQuestionX == null) {
			btnQuestionX = new JButton("");
			Style.questionButtonStyle(btnQuestionX);
			btnQuestionX.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// "Text file (.txt) that contains output predicted by unstructured predictor for each node."
					// + "\nEach output should be in a separate line. "
					// +
					// "\nOrder of outputs should be consistent with ordinal numbers of nodes in the file with edges (S)."
					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Text file (.txt) that contains value of each atribute for each node for train data."
											+ "\nAtributes for each node should be in a separate line. "
											+ "\nAtributes should be comma separated. "
											+ "\nAll atributes should be numbers. "
											+ "\nOrder should be consistent with ordinal numbers of nodes in the file with edges.",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionX;
	}

	private JLabel getLblYArrayFile() {
		if (lblYArrayFile == null) {
			lblYArrayFile = new JLabel("File with outputs:");
			lblYArrayFile.setHorizontalAlignment(SwingConstants.RIGHT);
			lblYArrayFile.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblYArrayFile;
	}

	private JTextField getTxtYFile() {
		if (txtYFile == null) {
			txtYFile = new JTextField();
			txtYFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtYFile.setColumns(10);
		}
		return txtYFile;
	}

	private JButton getBtnBrowseY() {
		if (btnBrowseY == null) {
			btnBrowseY = new JButton("Browse");
			btnBrowseY.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chooseFile(txtYFile);
				}
			});
			Style.buttonStyle(btnBrowseY);
		}
		return btnBrowseY;
	}

	private JButton getBtnQuestionY() {
		if (btnQuestionY == null) {
			btnQuestionY = new JButton("");
			Style.questionButtonStyle(btnQuestionY);
			btnQuestionY.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Text file (.txt) that contains actual output for each node for train data."
											+ "\nEach output should be in a separate line. "
											+ "\nOrder of outputs should be consistent with ordinal numbers of nodes in the file with edges (S).",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionY;
	}

	private JButton getBtnTrain() {
		if (btnTrain == null) {
			btnTrain = new JButton("SAVE");
			btnTrain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String message = validateData();
					if (message != null) {
						JOptionPane.showMessageDialog(mainFrame, message,
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {

						String mainPath = Reader.jarFile() 
								+ "/Datasets";
						String name = txtName.getText();
						String dataPath = "";
						if (!chkTogether.isSelected()) {
							dataPath = mainPath + "/Networks/" + name;
						} else {
							dataPath = mainPath + "/TemporalNetworks/" + name;
						}
						if (Writer.checkFolder(dataPath)) {

							int selectedOption = JOptionPane
									.showConfirmDialog(
											mainFrame,

											"Dataset with name "
													+ name
													+ " already exists. Do you want to replace it?",
											"Question",
											JOptionPane.YES_NO_OPTION);
							if (selectedOption == JOptionPane.YES_OPTION) {
								Reader.deleteFiles(dataPath);
							}
							if (selectedOption == JOptionPane.NO_OPTION) {
								return;
							}
						}
						Writer.createFolder(dataPath);

						String xTrain = txtXFile.getText();
						String yTrain = txtYFile.getText();
						String sTrain = txtSFile.getText();

						int noOfNodes = Integer.parseInt(txtNodes.getText());

						if (!chkTogether.isSelected()) {
							String[] x = Reader.read(xTrain);
							double[] y = Reader.readArray(yTrain, noOfNodes);
							double[][] s = Reader.readGraph(sTrain, noOfNodes);

							String message1 = checkFilesTrain(noOfNodes, x, y,
									s,"train");

							if (message1 != null) {
								JOptionPane.showMessageDialog(mainFrame,
										message1, "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}

							String xTest = txtXTestFile.getText();
							String yTest = txtYTestFile.getText();
							String sTest = txtSTestFile.getText();

							int noOfNodesTest = Integer.parseInt(txtNodesTest.getText());
							
							String[] x1 = Reader.read(xTest);
							double[] y1 = Reader.readArray(yTest, noOfNodesTest);
							double[][] s1 = Reader.readGraph(sTest, noOfNodesTest);

							String message2 = checkFilesTrain(noOfNodesTest, x1,
									y1, s1, "test");

							if (message2 != null) {
								JOptionPane.showMessageDialog(mainFrame,
										message2, "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}

							Writer.copyFile(new File(xTrain), dataPath
									+ "/xTrain.txt");
							Writer.copyFile(new File(yTrain), dataPath
									+ "/yTrain.txt");
							Writer.copyFile(new File(sTrain), dataPath
									+ "/sTrain.txt");

							Writer.copyFile(new File(xTest), dataPath
									+ "/xTest.txt");
							Writer.copyFile(new File(yTest), dataPath
									+ "/yTest.txt");
							Writer.copyFile(new File(sTest), dataPath
									+ "/sTest.txt");

							String[] text = new String[2];
							text[0] = "Train nodes: " + noOfNodes;
							text[1] = "Test nodes: " + noOfNodesTest;

							Writer.write(text, dataPath + "/readme.txt");
						} else {

							int noOfTime = Integer.parseInt(txtTimePoints
									.getText());
							int noOfX = Integer.parseInt(txtAttributes
									.getText());

							String[] x = Reader.read(xTrain);
							String[] y = Reader.read(yTrain);

							double[][] s = null;
							if (!chkLearn.isSelected()) {
								s = Reader.readGraph(sTrain, noOfNodes);
							}
							String message1 = checkAllFiles(noOfNodes,
									noOfTime, noOfX, x, y, s);

							if (message1 != null) {
								JOptionPane.showMessageDialog(mainFrame,
										message1, "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							Writer.copyFile(new File(xTrain), dataPath
									+ "/x.txt");
							Writer.copyFile(new File(yTrain), dataPath
									+ "/y.txt");
							if (!chkLearn.isSelected()) {
								Writer.copyFile(new File(sTrain), dataPath
										+ "/s.txt");
							}

							String[] text = new String[3];
							text[0] = "Nodes: " + noOfNodes;
							text[1] = "Time points: " + noOfTime;
							text[2] = "Attributes per node: " + noOfX;
							Writer.write(text, dataPath + "/readme.txt");
						}

						JOptionPane.showMessageDialog(mainFrame,
								"Dataset is created successfully.", "Error",
								JOptionPane.INFORMATION_MESSAGE);
						resetValues();
					}
				}

			});
			Style.buttonStyle(btnTrain);
		}
		return btnTrain;
	}

	private void resetValues() {
		txtName.setText("");
		txtXTestFile.setText("");
		txtYTestFile.setText("");
		txtSTestFile.setText("");
		txtXFile.setText("");
		txtYFile.setText("");
		txtSFile.setText("");
		txtNodes.setText("");
		txtNodesTest.setText("");
		chkTogether.setSelected(false);
		txtTimePoints.setText("");
		txtAttributes.setText("");
	}

	public String validateData() {
		if (txtName.getText().equals("")) {
			return "Insert dataset name.";
		}
		if (txtSFile.getText().equals("") && !chkLearn.isSelected()) {
			return "Choose matrix file for training.";
		}
		if (txtXFile.getText().equals("")) {
			return "Choose file with attributes values for training.";
		}
		if (txtYFile.getText().equals("")) {
			return "Choose file with output values for training.";
		}
		try {
			Integer.parseInt(txtNodes.getText());
		} catch (NumberFormatException e) {
			return "No. of nodes for training should be integer.";
		}
		if (!chkTogether.isSelected()) {
			if (txtSTestFile.getText().equals("")) {
				return "Choose matrix file for test.";
			}
			if (txtXTestFile.getText().equals("")) {
				return "Choose file with attributes values for test.";
			}
			if (txtYTestFile.getText().equals("")) {
				return "Choose file with output values for test.";
			}
			try {
				Integer.parseInt(txtNodesTest.getText());
			} catch (NumberFormatException e) {
				return "No. of nodes for test should be integer.";
			}
		}

		return null;
	}

	public String validateDataForTestPredictor() {

		if (txtSFile.getText().equals("")) {
			return "Choose file with edges.";
		}
		if (txtXFile.getText().equals("")) {
			return "Choose file with attributes values.";
		}
		if (txtYFile.getText().equals("")) {
			return "Choose file with output values.";
		}
		try {
			Integer.parseInt(txtNodes.getText());
		} catch (NumberFormatException e) {
			return "No. of nodes should be integer.";
		}

		return null;
	}

	public void chooseFile(JTextField txt) {
		int returnVal = fc.showOpenDialog(panel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			txt.setText(fc.getSelectedFile().getPath());
		}
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("No. of nodes:");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label;
	}

	private JTextField getTxtNodes() {
		if (txtNodes == null) {
			txtNodes = new JTextField();
			txtNodes.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtNodes.setColumns(10);
		}
		return txtNodes;
	}

	private JLabel getLblData() {
		if (lblData == null) {
			lblData = new JLabel("TRAIN DATA:");
			lblData.setForeground(Color.WHITE);
			lblData.setBackground(Color.GRAY);
			lblData.setHorizontalAlignment(SwingConstants.CENTER);
			lblData.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblData.setOpaque(true);
		}
		return lblData;
	}

	private JLabel getLblTestData() {
		if (lblTestData == null) {
			lblTestData = new JLabel("TEST DATA:");
			lblTestData.setOpaque(true);
			lblTestData.setHorizontalAlignment(SwingConstants.CENTER);
			lblTestData.setForeground(Color.WHITE);
			lblTestData.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblTestData.setBackground(Color.GRAY);
		}
		return lblTestData;
	}

	private JTextField getTxtSTestFile() {
		if (txtSTestFile == null) {
			txtSTestFile = new JTextField();
			txtSTestFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtSTestFile.setColumns(10);
		}
		return txtSTestFile;
	}

	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("File with edges:");
			label_1.setHorizontalAlignment(SwingConstants.RIGHT);
			label_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_1;
	}

	private JButton getBtnBrowseSTest() {
		if (btnBrowseSTest == null) {
			btnBrowseSTest = new JButton("Browse");
			btnBrowseSTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					chooseFile(txtSTestFile);
				}
			});
			Style.buttonStyle(btnBrowseSTest);
		}
		return btnBrowseSTest;
	}

	private JButton getBtnQuestionSTest() {
		if (btnQuestionSTest == null) {
			btnQuestionSTest = new JButton("");
			Style.questionButtonStyle(btnQuestionSTest);
			btnQuestionSTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Text file (.txt) that contains data about connections between nodes for test data."
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
		}
		return btnQuestionSTest;
	}

	private JLabel getLabel_3() {
		if (label_3 == null) {
			label_3 = new JLabel("File with attributes:");
			label_3.setHorizontalAlignment(SwingConstants.RIGHT);
			label_3.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_3;
	}

	private JTextField getTxtXTestFile() {
		if (txtXTestFile == null) {
			txtXTestFile = new JTextField();
			txtXTestFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtXTestFile.setColumns(10);
		}
		return txtXTestFile;
	}

	private JButton getBtnBrowseXTest() {
		if (btnBrowseXTest == null) {
			btnBrowseXTest = new JButton("Browse");
			btnBrowseXTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					chooseFile(txtXTestFile);
				}
			});
			Style.buttonStyle(btnBrowseXTest);
		}
		return btnBrowseXTest;
	}

	private JButton getBtnQuestionXTest() {
		if (btnQuestionXTest == null) {
			btnQuestionXTest = new JButton("");
			Style.questionButtonStyle(btnQuestionXTest);
			btnQuestionXTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// "Text file (.txt) that contains output predicted by unstructured predictor for each node."
					// + "\nEach output should be in a separate line. "
					// +
					// "\nOrder of outputs should be consistent with ordinal numbers of nodes in the file with edges (S)."
					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Text file (.txt) that contains value of each atribute for each node for test data."
											+ "\nAtributes for each node should be in a separate line. "
											+ "\nAtributes should be comma separated. "
											+ "\nAll atributes should be numbers. "
											+ "\nOrder should be consistent with ordinal numbers of nodes in the file with edges.",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionXTest;
	}

	private JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel("File with outputs:");
			label_4.setHorizontalAlignment(SwingConstants.RIGHT);
			label_4.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_4;
	}

	private JTextField getTxtYTestFile() {
		if (txtYTestFile == null) {
			txtYTestFile = new JTextField();
			txtYTestFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtYTestFile.setColumns(10);
		}
		return txtYTestFile;
	}

	private JButton getBtnBrowseYTest() {
		if (btnBrowseYTest == null) {
			btnBrowseYTest = new JButton("Browse");
			btnBrowseYTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					chooseFile(txtYTestFile);
				}
			});
			Style.buttonStyle(btnBrowseYTest);

		}
		return btnBrowseYTest;
	}

	private JButton getBtnQuestionYTest() {
		if (btnQuestionYTest == null) {
			btnQuestionYTest = new JButton("");
			Style.questionButtonStyle(btnQuestionYTest);
			btnQuestionYTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane
							.showMessageDialog(
									mainFrame,
									"Text file (.txt) that contains actual output for each node for test data."
											+ "\nEach output should be in a separate line. "
											+ "\nOrder of outputs should be consistent with ordinal numbers of nodes in the file with edges (S).",
									"Help", JOptionPane.QUESTION_MESSAGE,
									Style.questionIcon());
				}
			});
		}
		return btnQuestionYTest;
	}

	private JLabel getLabel_5() {
		if (label_5 == null) {
			label_5 = new JLabel("No. of nodes:");
			label_5.setHorizontalAlignment(SwingConstants.RIGHT);
			label_5.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return label_5;
	}

	private JTextField getTxtNodesTest() {
		if (txtNodesTest == null) {
			txtNodesTest = new JTextField();
			txtNodesTest.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtNodesTest.setColumns(10);
		}
		return txtNodesTest;
	}

	private JCheckBox getChkTogether() {
		if (chkTogether == null) {
			chkTogether = new JCheckBox(
					"train and test data are provided together");
			chkTogether.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (chkTogether.isSelected()) {
						txtSTestFile.setEnabled(false);
						txtXTestFile.setEnabled(false);
						txtYTestFile.setEnabled(false);
						txtNodesTest.setEnabled(false);
						btnBrowseSTest.setEnabled(false);
						btnBrowseXTest.setEnabled(false);
						btnBrowseYTest.setEnabled(false);
						lblAttributes.setVisible(true);
						lblTimePoints.setVisible(true);
						txtAttributes.setVisible(true);
						txtTimePoints.setVisible(true);
					} else {
						txtSTestFile.setEnabled(true);
						txtXTestFile.setEnabled(true);
						txtYTestFile.setEnabled(true);
						txtNodesTest.setEnabled(true);
						btnBrowseSTest.setEnabled(true);
						btnBrowseXTest.setEnabled(true);
						btnBrowseYTest.setEnabled(true);
						lblAttributes.setVisible(false);
						lblTimePoints.setVisible(false);
						txtAttributes.setVisible(false);
						txtTimePoints.setVisible(false);
					}
				}
			});
		}
		return chkTogether;
	}

	private JLabel getLblAttributes() {
		if (lblAttributes == null) {
			lblAttributes = new JLabel("No. of attributes per node:");
			lblAttributes.setVisible(false);
			lblAttributes.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAttributes.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblAttributes;
	}

	private JTextField getTxtAttributes() {
		if (txtAttributes == null) {
			txtAttributes = new JTextField();
			txtAttributes.setVisible(false);
			txtAttributes.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtAttributes.setColumns(10);
		}
		return txtAttributes;
	}

	private JLabel getLblTimePoints() {
		if (lblTimePoints == null) {
			lblTimePoints = new JLabel("No. of time points:");
			lblTimePoints.setVisible(false);
			lblTimePoints.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTimePoints.setFont(new Font("Segoe UI", Font.BOLD, 15));
		}
		return lblTimePoints;
	}

	private JTextField getTxtTimePoints() {
		if (txtTimePoints == null) {
			txtTimePoints = new JTextField();
			txtTimePoints.setVisible(false);
			txtTimePoints.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtTimePoints.setColumns(10);
		}
		return txtTimePoints;
	}

	private JCheckBox getChkLearn() {
		if (chkLearn == null) {
			chkLearn = new JCheckBox("Learn similarity");
		}
		return chkLearn;
	}

	private String checkAllFiles(int noOfNodes, int noOfTime, int noOfX,
			String[] x, String[] y, double[][] s) {

		String xMsg = checkX(noOfNodes, x, noOfX, noOfTime);
		if (xMsg != null) {
			return xMsg;
		}

		String yMsg = checkY(noOfNodes, y, noOfX, noOfTime);
		if (yMsg != null) {
			return yMsg;
		}
		if (s != null) {
			String sMsg = checkS(noOfNodes, s);
			if (sMsg != null) {
				return sMsg;
			}
		}
		return null;
	}

	private String checkFilesTrain(int noOfNodes, String[] x, double[] y,
			double[][] s, String trainTest) {
		if (x == null) {
			return "Error while reading file with attributes for " + trainTest
					+ ".";
		}

		if (x.length != noOfNodes) {
			return "Error while reading file with attributes for " + trainTest
					+ ": Number of lines should be " + noOfNodes + ".";
		}

		if (y == null) {
			return "Error while reading file with outputs for " + trainTest
					+ ": Number of lines should be " + noOfNodes + ".";
		}

		if (s == null) {
			return "Error while reading file with edges for " + trainTest
					+ ": Ordinal number of node can be between 1 and "
					+ noOfNodes + ".";
		}
		return null;
	}

	private String checkS(int noOfNodes, double[][] s) {
		if (s == null) {
			return "Ordinal number of node can be between 1 and " + noOfNodes
					+ ".";
		}
		return null;
	}

	private String checkX(int noOfNodes, String[] x, int noOfX, int noOfTime) {
		int totalX = noOfTime * noOfX;
		if (x == null) {
			return "Error while reading file with attributes.";
		}

		if (x.length != noOfNodes) {
			return "Number of lines in the file with attributes should be "
					+ noOfNodes + ".";
		}
		for (int i = 0; i < x.length; i++) {
			if (x[i].split(",").length != totalX) {
				return "Number of values in each line in the file with attributes should be equal to no. of attributes * no. of time points: "
						+ totalX;
			}
		}
		return null;
	}

	private String checkY(int noOfNodes, String[] y, int noOfX, int noOfTime) {
		if (y == null) {
			return "Error while reading file with attributes.";
		}
		if (y.length != noOfNodes) {
			return "Number of lines in the file with outputs should be "
					+ noOfNodes + ".";
		}
		for (int i = 0; i < y.length; i++) {
			if (y[i].split(",").length != noOfTime) {
				return "Number of values in each line in the file with outputs should be equal to no. of time points: "
						+ noOfTime;
			}
		}
		return null;
	}
}
