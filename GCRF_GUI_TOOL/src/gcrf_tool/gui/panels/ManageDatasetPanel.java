package gcrf_tool.gui.panels;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.GridBagLayout;

import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.style.Style;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.BorderLayout;


import java.awt.Insets;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class ManageDatasetPanel extends JPanel {

	private static final long serialVersionUID = 8738206830360856377L;
	private JFrame mainFrame;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPaneTable2;
	private JScrollPane scrollPaneTable1;
	private JButton btnDelete1;
	private JTable table1;
	private DefaultTableModel model1;
	private JButton btnDelete2;
	private JTable table2;
	private DefaultTableModel model2;
	private JButton btnRename1;
	private JButton btnRename2;
	private JLabel lblManageDatasets;

	/**
	 * Create the panel.
	 */
	public ManageDatasetPanel(JFrame mainFrame) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblManageDatasets = new GridBagConstraints();
		gbc_lblManageDatasets.fill = GridBagConstraints.BOTH;
		gbc_lblManageDatasets.insets = new Insets(0, 0, 5, 0);
		gbc_lblManageDatasets.gridx = 0;
		gbc_lblManageDatasets.gridy = 0;
		add(getLblManageDatasets(), gbc_lblManageDatasets);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		add(getTabbedPane(), gbc_tabbedPane);
		this.mainFrame = mainFrame;


	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			JComponent panel1 = makePanelNetworks();
			tabbedPane.addTab("NETWORKS", null, panel1,
					"MANAGE NETWORKS  DATASETS");

			tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

			JComponent panel2 = makePanelTemporalNetworks();
			tabbedPane.addTab("TEMPORAL NETWORKS", null, panel2,
					"MANAGE TEMPORAL NETWORKS  DATASETS");
			tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		}
		return tabbedPane;
	}

	protected JComponent makePanelNetworks() {
		JPanel panel = new JPanel(false);
		model1 = createTable1();
		panel.setLayout(null);
		panel.setLayout(new BorderLayout(0, 0));
		table1 = new JTable(model1);
		table1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		scrollPaneTable1 = new JScrollPane(table1);
		table1.setFillsViewportHeight(true);
		panel.add(scrollPaneTable1);
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(panel_1, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{165, 115, 0};
		gbl_panel_1.rowHeights = new int[]{23, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		GridBagConstraints gbc_btnDelete1 = new GridBagConstraints();
		gbc_btnDelete1.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete1.gridx = 0;
		gbc_btnDelete1.gridy = 0;
		panel_1.add(getBtnDelete1(), gbc_btnDelete1);
		GridBagConstraints gbc_btnRename1 = new GridBagConstraints();
		gbc_btnRename1.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnRename1.gridx = 1;
		gbc_btnRename1.gridy = 0;
		panel_1.add(getBtnRename1(), gbc_btnRename1);
		return panel;
	}

	protected JComponent makePanelTemporalNetworks() {
		JPanel panel = new JPanel(false);
		model2 = createTable2();
		panel.setLayout(null);
		panel.setLayout(new BorderLayout(0, 0));
		table2 = new JTable(model2);
		scrollPaneTable2 = new JScrollPane(table2);
		table2.setFillsViewportHeight(true);
		panel.add(scrollPaneTable2);
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{165, 115, 0};
		gbl_panel_1.rowHeights = new int[]{23, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		GridBagConstraints gbc_btnDelete1 = new GridBagConstraints();
		gbc_btnDelete1.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete1.gridx = 0;
		gbc_btnDelete1.gridy = 0;
		panel_1.add(getBtnDelete2(), gbc_btnDelete1);
		GridBagConstraints gbc_btnRename1 = new GridBagConstraints();
		gbc_btnRename1.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnRename1.gridx = 1;
		gbc_btnRename1.gridy = 0;
		panel_1.add(getBtnRename2(), gbc_btnRename1);
		return panel;
	}
	private JButton getBtnRename1() {
		if (btnRename1 == null) {
			btnRename1 = new JButton("Rename selected");
		
			btnRename1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					if (table1.getSelectedRow() != -1) {
						String name = model1.getValueAt(
								table1.getSelectedRow(), 0).toString();
						String newName = (String) JOptionPane.showInputDialog(
								mainFrame, "Insert new name for dataset", name);
						String mainPath = Reader.jarFile()
								+ "/Datasets/Networks/" + name;
						if ((newName != null) && (newName.length() > 0)) {
							model1.setValueAt(newName, table1.getSelectedRow(),
									0);
							Writer.renameDir(mainPath, newName);
							return;
						}
					} else {
						JOptionPane.showMessageDialog(mainFrame,
								"Please select dataset.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			});
			Style.buttonStyle(btnRename1);
		}
		return btnRename1;
	}


	private DefaultTableModel createTable1() {
		String[] columnNames = { "Name", "No of nodes train",
				"No of nodes test" };
		String mainPath = Reader.jarFile() + "/Datasets/Networks";
		String[] files = Reader.getAllFolders(mainPath);

		Object[][] data = new Object[files.length][3];
		String readme = "";
		String[] lines = null;
		for (int i = 0; i < files.length; i++) {
			readme = mainPath + "/" + files[i] + "/readme.txt";
			lines = Reader.read(readme);
			data[i][0] = files[i];
			data[i][1] = Integer.parseInt(lines[0].substring(lines[0]
					.indexOf(":") + 2));
			data[i][2] = Integer.parseInt(lines[1].substring(lines[1]
					.indexOf(":") + 2));
		}

		return new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = -7347188371755670835L;

			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}

		};
	}
	
	private JButton getBtnDelete1() {
		if (btnDelete1 == null) {
			btnDelete1 = new JButton("Delete selected");
			Style.buttonStyle(btnDelete1);
			btnDelete1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (table1.getSelectedRow() != -1) {
						String name = model1.getValueAt(
								table1.getSelectedRow(), 0).toString();
						int selectedOption = JOptionPane.showConfirmDialog(
								mainFrame,

								"Are you sure that you want to permanently delete \""
										+ name + "\" dataset?", "Question",
								JOptionPane.YES_NO_OPTION);
						if (selectedOption == JOptionPane.YES_OPTION) {
							String mainPath = Reader.jarFile()
									+ "/Datasets/Networks/" + name;
							Reader.deleteDir(new File(mainPath));
							model1.removeRow(table1.getSelectedRow());
						}
						if (selectedOption == JOptionPane.NO_OPTION) {
							return;
						}
					} else {
						JOptionPane.showMessageDialog(mainFrame,
								"Please select dataset.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return btnDelete1;
	}

	private DefaultTableModel createTable2() {
		String[] columnNames = { "Name", "No of nodes", "No of time points",
				"No of attributes" };
		String mainPath = Reader.jarFile() + "/Datasets/TemporalNetworks";
		String[] files = Reader.getAllFolders(mainPath);

		Object[][] data = new Object[files.length][4];
		String readme = "";
		String[] lines = null;
		for (int i = 0; i < files.length; i++) {
			readme = mainPath + "/" + files[i] + "/readme.txt";
			lines = Reader.read(readme);
			data[i][0] = files[i];
			data[i][1] = Integer.parseInt(lines[0].substring(lines[0]
					.indexOf(":") + 2));
			data[i][2] = Integer.parseInt(lines[1].substring(lines[1]
					.indexOf(":") + 2));
			data[i][3] = Integer.parseInt(lines[2].substring(lines[2]
					.indexOf(":") + 2));
		}

		return new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = 7690123115074512018L;

			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}

		};
	}


	private JButton getBtnRename2() {
		if (btnRename2 == null) {
			btnRename2 = new JButton("Rename selected");
			Style.buttonStyle(btnRename2);
			btnRename2.setBounds(411, 456, 180, 42);
			btnRename2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					if (table2.getSelectedRow() != -1) {
						String name = model2.getValueAt(
								table2.getSelectedRow(), 0).toString();
						String newName = (String) JOptionPane.showInputDialog(
								mainFrame, "Insert new name for dataset", name);
						String mainPath = Reader.jarFile()
								+ "/Datasets/TemporalNetworks/" + name;
						if ((newName != null) && (newName.length() > 0)) {
							model2.setValueAt(newName, table2.getSelectedRow(),
									0);
							Writer.renameDir(mainPath, newName);
							return;
						}
					} else {
						JOptionPane.showMessageDialog(mainFrame,
								"Please select dataset.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			});
		}
		return btnRename2;
	}
	private JButton getBtnDelete2() {
		if (btnDelete2 == null) {
			btnDelete2 = new JButton("Delete selected");


			Style.buttonStyle(btnDelete2);
			btnDelete2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (table2.getSelectedRow() != -1) {
						String name = model2.getValueAt(
								table2.getSelectedRow(), 0).toString();
						int selectedOption = JOptionPane.showConfirmDialog(
								mainFrame,

								"Are you sure that you want to permanently delete \""
										+ name + "\" dataset?", "Question",
								JOptionPane.YES_NO_OPTION);
						if (selectedOption == JOptionPane.YES_OPTION) {
							String mainPath = Reader.jarFile()
									+ "/Datasets/TemporalNetworks/" + name;
							Reader.deleteDir(new File(mainPath));
							model2.removeRow(table2.getSelectedRow());
						}
						if (selectedOption == JOptionPane.NO_OPTION) {
							return;
						}
					} else {
						JOptionPane.showMessageDialog(mainFrame,
								"Please select dataset.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return btnDelete2;
	}
	private JLabel getLblManageDatasets() {
		if (lblManageDatasets == null) {
			lblManageDatasets = new JLabel("MANAGE DATASETS:");
			lblManageDatasets.setOpaque(true);
			lblManageDatasets.setHorizontalAlignment(SwingConstants.CENTER);
			lblManageDatasets.setForeground(Color.WHITE);
			lblManageDatasets.setFont(new Font("Segoe UI", Font.BOLD, 15));
			lblManageDatasets.setBackground(Color.GRAY);
		}
		return lblManageDatasets;
	}
}
