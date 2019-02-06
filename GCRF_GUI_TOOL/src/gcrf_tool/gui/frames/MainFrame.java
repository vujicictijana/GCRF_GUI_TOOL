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

package gcrf_tool.gui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JMenuBar;

import gcrf_tool.file.Reader;
import gcrf_tool.gui.panels.AddDatasetPanel;
import gcrf_tool.gui.panels.ConfigurePanel;
import gcrf_tool.gui.panels.HelpPanel;
import gcrf_tool.gui.panels.ManageDatasetPanel;
import gcrf_tool.gui.panels.TestPanel;
import gcrf_tool.gui.panels.TestRandomPanel;
import gcrf_tool.gui.panels.TrainPanel;
import gcrf_tool.gui.panels.TrainRandomPanel;
import gcrf_tool.gui.panels.TrainTemporalPanel;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -4251515464104659000L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnTrain;
	private JMenuItem menuTrain;
	private JMenuItem menuTrainRandom;
	private JMenu mnTest;
	private JMenuItem menuTest;
	private JMenuItem menuTestRandom;
	private JMenu mnSettings;
	private JMenuItem menuParameters;
	private JFrame frame;
	private JPanel mainPanel;
	private JMenuItem mntmNewMenuItem;
	private JMenu mnDatasets;
	private JMenuItem menuAddDataset;
	private JMenuItem menuManageDatasets;
	private JMenu mnHelp;
	private JMenuItem menuAbout;
	private JMenuItem menuDatasets;
	private JMenuItem menuMethods;
	private JScrollPane scrollPane;
	private JPanel mainPanel1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		

	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setJMenuBar(getMenuBar_1());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		setMinimumSize(new Dimension(900, 500));
		frame = this;
		if (Reader.checkFile(Reader.jarFile() + "/cfg.txt")) {

		} else {
			ConfigurePanel t = new ConfigurePanel(frame);
			if (mainPanel1 != null) {
				contentPane.removeAll();
				contentPane.repaint();
				contentPane.revalidate();
			}
			mainPanel1 = t;
			scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			scrollPane.setViewportView(mainPanel1);
			contentPane.add(getScrollPane(), gbc_scrollPane);
			contentPane.repaint();
			contentPane.revalidate();

			menuBar.setEnabled(false);
			for (int i = 0; i < menuBar.getComponentCount(); i++) {
				menuBar.getComponent(i).setEnabled(false);
			}
		}
	}
	
	public void enableMenu() {
		menuBar.setEnabled(true);
		for (int i = 0; i < menuBar.getComponentCount(); i++) {
			menuBar.getComponent(i).setEnabled(true);
		}
	}

	
	private JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.setBackground(Color.WHITE);
			menuBar.setBounds(0, 0, 900, 59);
			menuBar.add(getMnTrain());
			menuBar.add(getMnTest());
//			menuBar.add(getMnPredict());
			menuBar.add(getMnDatasets());
			menuBar.add(getMnSettings());
			menuBar.add(getMnHelp());
		}
		return menuBar;
	}

	private JMenu getMnTrain() {
		if (mnTrain == null) {
			mnTrain = new JMenu("Train");
			mnTrain.setIcon(new ImageIcon(Reader.jarFile() + "/images/train.png"));
			mnTrain.setFont(new Font("Segoe UI", Font.BOLD, 17));
			mnTrain.add(getMenuTrain());
			mnTrain.add(new JSeparator());
			mnTrain.add(getMntmNewMenuItem());
			mnTrain.add(new JSeparator());
			mnTrain.add(getMenuTrainRandom());
		}
		return mnTrain;
	}

	private JMenuItem getMenuTrain() {
		if (menuTrain == null) {
			menuTrain = new JMenuItem("Train on networks");
			menuTrain.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuTrain.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					TrainPanel t = new TrainPanel(frame);
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();

				}
			});
		}
		return menuTrain;
	}

	private JMenuItem getMenuTrainRandom() {
		if (menuTrainRandom == null) {
			menuTrainRandom = new JMenuItem("Train on random networks");
			menuTrainRandom.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuTrainRandom
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							TrainRandomPanel t = new TrainRandomPanel(frame);
							if (mainPanel1 != null) {
								contentPane.removeAll();
								contentPane.repaint();
								contentPane.revalidate();
							}
							mainPanel1 = t;
							scrollPane = new JScrollPane();
							GridBagConstraints gbc_scrollPane = new GridBagConstraints();
							gbc_scrollPane.fill = GridBagConstraints.BOTH;
							gbc_scrollPane.gridx = 0;
							gbc_scrollPane.gridy = 0;
							scrollPane.setViewportView(mainPanel1);
							contentPane.add(getScrollPane(), gbc_scrollPane);
							contentPane.repaint();
							contentPane.revalidate();
						}
					});
		}
		return menuTrainRandom;
	}

	private JMenu getMnTest() {
		if (mnTest == null) {
			mnTest = new JMenu("Test");
			mnTest.setFont(new Font("Segoe UI", Font.BOLD, 17));
			mnTest.setIcon(new ImageIcon(Reader.jarFile() +"/images/test.png"));
			mnTest.add(getMenuTest());
			mnTest.add(new JSeparator());
			mnTest.add(getMenuTestRandom());
		}
		return mnTest;
	}

	private JMenuItem getMenuTest() {
		if (menuTest == null) {
			menuTest = new JMenuItem("Test on networks");
			menuTest.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuTest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					TestPanel t = new TestPanel(frame);
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();
				}
			});
		}
		return menuTest;
	}

	private JMenuItem getMenuTestRandom() {
		if (menuTestRandom == null) {
			menuTestRandom = new JMenuItem("Test on random networks");
			menuTestRandom.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuTestRandom
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							TestRandomPanel t = new TestRandomPanel(frame);
							if (mainPanel1 != null) {
								contentPane.removeAll();
								contentPane.repaint();
								contentPane.revalidate();
							}
							mainPanel1 = t;
							scrollPane = new JScrollPane();
							GridBagConstraints gbc_scrollPane = new GridBagConstraints();
							gbc_scrollPane.fill = GridBagConstraints.BOTH;
							gbc_scrollPane.gridx = 0;
							gbc_scrollPane.gridy = 0;
							scrollPane.setViewportView(mainPanel1);
							contentPane.add(getScrollPane(), gbc_scrollPane);
							contentPane.repaint();
							contentPane.revalidate();
						}
					});
		}
		return menuTestRandom;
	}

	private JMenu getMnSettings() {
		if (mnSettings == null) {
			mnSettings = new JMenu("Settings");
			mnSettings.setFont(new Font("Segoe UI", Font.BOLD, 17));
			mnSettings.setIcon(new ImageIcon(Reader.jarFile() +"/images/settings.png"));
			mnSettings.add(getMenuParameters());
			// mnSettings.add(new JSeparator());
			// mnSettings.add(getMenuFile());
			// mnSettings.add(new JSeparator());

		}
		return mnSettings;
	}

	private JMenuItem getMenuParameters() {
		if (menuParameters == null) {
			menuParameters = new JMenuItem("Configuration");
			menuParameters.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuParameters
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							ConfigurePanel t = new ConfigurePanel(frame);
							if (mainPanel1 != null) {
								contentPane.removeAll();
								contentPane.repaint();
								contentPane.revalidate();
							}
							mainPanel1 = t;
							scrollPane = new JScrollPane();
							GridBagConstraints gbc_scrollPane = new GridBagConstraints();
							gbc_scrollPane.fill = GridBagConstraints.BOTH;
							gbc_scrollPane.gridx = 0;
							gbc_scrollPane.gridy = 0;
							scrollPane.setViewportView(mainPanel1);
							contentPane.add(getScrollPane(), gbc_scrollPane);
							contentPane.repaint();
							contentPane.revalidate();
						}
					});
		}
		return menuParameters;
	}


	// private JMenuItem getMnPredictYour() {
	// if (mnPredictYour == null) {
	// mnPredictYour = new JMenuItem("Predict using existing model");
	// mnPredictYour.setFont(new Font("Segoe UI", Font.PLAIN, 15));
	// mnPredictYour.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// PredictPanel t = new PredictPanel(frame);
	// t.setBounds(0, 61, 900, 500);
	// if (mainPanel != null) {
	// contentPane.remove(mainPanel);
	// contentPane.repaint();
	// contentPane.revalidate();
	// }
	// mainPanel = t;
	// contentPane.add(mainPanel);
	// contentPane.repaint();
	// contentPane.revalidate();
	// }
	// });
	// }
	// return mnPredictYour;
	// }

	private JMenuItem getMntmNewMenuItem() {
		if (mntmNewMenuItem == null) {
			mntmNewMenuItem = new JMenuItem("Train on temporal networks");
			mntmNewMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					TrainTemporalPanel t = new TrainTemporalPanel(frame);
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();
				}
			});
			mntmNewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		}
		return mntmNewMenuItem;
	}
	private JMenu getMnDatasets() {
		if (mnDatasets == null) {
			mnDatasets = new JMenu("Datasets");
			mnDatasets.setFont(new Font("Segoe UI", Font.BOLD, 17));
			mnDatasets.setIcon(new ImageIcon(Reader.jarFile() + "/images/data.png"));
			mnDatasets.add(getMenuAddDataset());
			mnDatasets.add(new JSeparator());
			mnDatasets.add(getMenuManageDatasets());
		}
		return mnDatasets;
	}
	private JMenuItem getMenuAddDataset() {
		if (menuAddDataset == null) {
			menuAddDataset = new JMenuItem("Add dataset");
			menuAddDataset.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuAddDataset.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					AddDatasetPanel t = new AddDatasetPanel(frame);
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();

				}
			});
		}
		return menuAddDataset;
	}
	private JMenuItem getMenuManageDatasets() {
		if (menuManageDatasets == null) {
			menuManageDatasets = new JMenuItem("Manage datasets");
			menuManageDatasets.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuManageDatasets.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					ManageDatasetPanel t = new ManageDatasetPanel(frame);
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();

				}
			});
		}
		return menuManageDatasets;
	}
	private JMenu getMnHelp() {
		if (mnHelp == null) {
			mnHelp = new JMenu("Help");
			mnHelp.setFont(new Font("Segoe UI", Font.BOLD, 17));
			mnHelp.setIcon(new ImageIcon(Reader.jarFile() +"/images/question-menu.png"));
			mnHelp.add(getMenuAbout());
			mnHelp.add(new JSeparator());
			mnHelp.add(getMenuDatasets());
			mnHelp.add(new JSeparator());
			mnHelp.add(getMenuMethods());
		}
		return mnHelp;
	}
	private JMenuItem getMenuAbout() {
		if (menuAbout == null) {
			menuAbout = new JMenuItem("About");
			menuAbout.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuAbout.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					HelpPanel t = new HelpPanel(frame,Reader.jarFile()  + "/html/about.html");
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();
				}
			});
		}
		return menuAbout;
	}
	private JMenuItem getMenuDatasets() {
		if (menuDatasets == null) {
			menuDatasets = new JMenuItem("Datasets");
			menuDatasets.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuDatasets.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {

					HelpPanel t = new HelpPanel(frame,Reader.jarFile() + "/html/dataset.html");
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();

				}
			});
		}
		return menuDatasets;
	}
	private JMenuItem getMenuMethods() {
		if (menuMethods == null) {
			menuMethods = new JMenuItem("Methods");
			menuMethods.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			menuMethods.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					HelpPanel t = new HelpPanel(frame,null);
					if (mainPanel1 != null) {
						contentPane.removeAll();
						contentPane.repaint();
						contentPane.revalidate();
					}
					mainPanel1 = t;
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 0;
					scrollPane.setViewportView(mainPanel1);
					contentPane.add(getScrollPane(), gbc_scrollPane);
					contentPane.repaint();
					contentPane.revalidate();

				}
			});
		}
		return menuMethods;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getPanel_2());
		}
		return scrollPane;
	}
	private JPanel getPanel_2() {
		if (mainPanel1 == null) {
			mainPanel1 = new JPanel(); 
		}
		return mainPanel1;
	}
}
