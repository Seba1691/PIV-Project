package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import manager.Settings;

public class SettingsFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;

	public SettingsFrame() {
		setTitle("Propiedades");
		initComponent();
		loadSettings();
	}

	private void initComponent() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(120, 100, 386, 144);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblPathFiltros = new JLabel("Path Filtros");
		lblPathFiltros.setBounds(10, 9, 79, 14);
		panel.add(lblPathFiltros);

		textField = new JTextField();
		textField.setBounds(99, 6, 174, 20);
		panel.add(textField);
		textField.setColumns(10);

		JButton buttonGuardar = new JButton("Guardar");
		buttonGuardar.setBounds(275, 83, 73, 23);
		panel.add(buttonGuardar);
		buttonGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.filtersPath = textField.getText();
				PIVGui.restatApplication();
			}
		});

	}

	private void loadSettings() {
		textField.setText(Settings.filtersPath.replace("/", "\\"));
	}
}
