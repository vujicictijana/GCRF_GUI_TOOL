package app.gui.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;

import javax.swing.JLabel;

import app.file.io.Reader;
import app.gui.style.SwingLink;

import java.awt.GridBagLayout;

public class HelpPanel extends JPanel {

	private static final long serialVersionUID = 7542338256284881226L;
	private JPanel panel;
	private JFrame mainFrame;
	private JLabel lblText;

	public HelpPanel(JFrame mainFrame, String fileName) {
		setBounds(new Rectangle(0, 0, 900, 650));
		setMinimumSize(new Dimension(500, 500));

		setBackground(UIManager.getColor("Button.background"));

		this.mainFrame = mainFrame;

		panel = this;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { Double.MIN_VALUE };
		setLayout(gridBagLayout);
		if (fileName != null) {
			String initialText = getHtml(fileName);
			lblText = new JLabel(initialText);
			GridBagConstraints label = new GridBagConstraints();
			label.anchor = GridBagConstraints.NORTHWEST;
			label.gridx = 0;
			label.gridy = 0;
			panel.add(lblText, label);
		} else {
			setLinks();
		}
	}

	private String getHtml(String fileName) {
		String initialText = "";
		String[] read = Reader.read(fileName);
		for (int i = 0; i < read.length; i++) {
			initialText += read[i];
		}
		return initialText;
	}

	public String[] generateLinks() {
		String[] methods = new String[6];
		methods[0] = "GCRF - http://www.ist.temple.edu/~zoran/papers/ECAI125.pdf";
		methods[1] = "DirGCRF – unpublished";
		methods[2] = "UmGCRF – http://www.ist.temple.edu/~zoran/papers/jesseAAAI2016.pdf";
		methods[3] = "RLSR - http://www.ist.temple.edu/~zoran/papers/chaoSDM2016.pdf";
		methods[4] = "up-GCRF - http://www.ist.temple.edu/~zoran/papers/djoleAAAI2016.pdf";
		methods[5] = "m-GCRF - http://www.dabi.temple.edu/~zoran/papers/StojanovicSDM15.pdf";
		return methods;
	}

	public void setLinks() {
		String initialText = getHtml(Reader.jarFile() + "/html/methods.html");
		lblText = new JLabel(initialText);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(lblText, gbc);

		JLabel label = new JLabel("Links:");
		int y = 1;
		gbc.gridy = y;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(label, gbc);

		String[] links = generateLinks();

		for (int j = 0; j < links.length; j++) {
			if (!links[j].contains("unpublished")) {
				String name = links[j].substring(0,
						links[j].lastIndexOf(" ") - 2);
				String link = links[j].substring(links[j].lastIndexOf(" ") + 1);
				SwingLink linkLabel = new SwingLink(name, link);
				y++;
				gbc.gridy = y;
				panel.add(linkLabel, gbc);
			} else {
				JLabel label1 = new JLabel(links[j]);
				y++;
				gbc.gridy = y;
				panel.add(label1, gbc);
			}
		}

	}

}
