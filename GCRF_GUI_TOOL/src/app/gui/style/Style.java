package app.gui.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import app.file.io.Reader;

public class Style {

	public static void buttonStyle(JButton button) {
		button.setBackground(UIManager.getColor("Button.background"));
//		button.setBackground(new Color(54, 120, 226));
		button.setBorder(new RoundedBorder(10));
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));

		button.setFocusPainted(false);
	}

	public static void questionButtonStyle(JButton button) {
		button.setBorder(null);
		button.setBackground(UIManager.getColor("Button.background"));
		button.setIcon(new ImageIcon(Reader.jarFile() +  "/images/question.png"));
		button.setRolloverIcon(new ImageIcon(Reader.jarFile() +  "/images/question-h.png"));
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
	}

	public static void resultTable(JTable table, int last) {
		JTableHeader header = table.getTableHeader();
		header.setBackground(new Color(74, 130, 226));
		header.setForeground(Color.WHITE);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setDefaultEditor(Object.class, null);
		header.setFont(new Font("Segoe UI", Font.BOLD, 15));
		table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		table.setRowHeight(20);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -4710832517168964346L;
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				final Component c = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				if (last != -1) {
					c.setBackground(row == last || row==last-1 ? Color.LIGHT_GRAY
							: Color.WHITE);
					c.setFont(row == last || row==last-1 ? new Font("Segoe UI", Font.BOLD, 15)
							: new Font("Segoe UI", Font.PLAIN, 15));
				}
				return c;
			}
		});
	}

	public static void greyLabel(JLabel label){
		label.setForeground(Color.WHITE);
		label.setBackground(Color.GRAY);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Segoe UI", Font.BOLD, 15));
		label.setOpaque(true);
	}
	
	public static ImageIcon questionIcon() {
		return new ImageIcon(Reader.jarFile() +  "/images/question-icon.png");
	}
}
