package gcrf_tool.gui.frames;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JFrame {

	private static final long serialVersionUID = 1747260278082326270L;
	private JProgressBar current;
	private JFrame frame;

	/**
	 * @wbp.parser.constructor
	 */
	public ProgressBar(int max) {
		setResizable(false);
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout());
		current = new JProgressBar(0, max);
		current.setValue(0);
		current.setStringPainted(true);
		Dimension prefSize = current.getPreferredSize();
		prefSize.width = 350;
		current.setPreferredSize(prefSize);
		pane.add(current);
		pane.setPreferredSize(new Dimension(400, 70));
		setContentPane(pane);
		frame = this;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(frame,
						"To cancel training process press Cancel button.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	public ProgressBar(String process) {
		setResizable(false);
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout());
		int min = 0;
		int max = 100;
		current = new JProgressBar(min, max);
		current.setIndeterminate(true);
		Dimension prefSize = current.getPreferredSize();
		prefSize.width = 350;
		current.setPreferredSize(prefSize);
		pane.add(current);
		pane.setPreferredSize(new Dimension(400, 70));
		setContentPane(pane);
		frame = this;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(frame,
						"To cancel " + process + " process press Cancel button.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	public JProgressBar getCurrent() {
		return current;
	}

	public void setCurrent(JProgressBar current) {
		this.current = current;
	}

}