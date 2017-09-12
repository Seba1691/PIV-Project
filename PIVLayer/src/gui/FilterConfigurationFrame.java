package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import pivLayer.FilterException;
import pivLayer.Filtro;

public class FilterConfigurationFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable table;
	private Filtro filtro;
	private DefaultTableModel tableModel;

	/**
	 * Create the frame.
	 */
	public FilterConfigurationFrame(Filtro filtro) {
		setTitle("Configuracion");
		this.filtro = filtro;
		InitFrame();
	}

	private void InitFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(120, 100, 225, 259);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		tableModel = parametersToTableModel(filtro.getParametros());

		table = new FilterConfiguratonTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(575);
		table.getColumnModel().getColumn(0).sizeWidthToFit();
		resizeColumnWidth(table);

		JScrollPane scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		JButton btnGuardarConfig = new JButton("Guardar");
		btnGuardarConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.isEditing())
					table.getCellEditor().stopCellEditing();
				try {
					filtro.setParametros(tableModelToParameters(tableModel));
					dispose();
				} catch (FilterException e) {
					new GUIException(e).inform();
				}
			}
		});
		panel.add(btnGuardarConfig);

	}

	private void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 75; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width, width) + 1;
			}
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}

	private DefaultTableModel parametersToTableModel(HashMap<String, Object> parameters) {
		Object[][] data = new Object[parameters.size()][2];
		int count = 0;
		for (Entry<String, Object> entry : parameters.entrySet()) {
			data[count][0] = entry.getKey();
			data[count][1] = entry.getValue();
			count++;
		}

		return new DefaultTableModel(data, new String[] { "Parametro", "Valor" }) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
	}

	private HashMap<String, Object> tableModelToParameters(DefaultTableModel tableModel) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		int nRow = tableModel.getRowCount();
		for (int i = 0; i < nRow; i++) {
			result.put((String) tableModel.getValueAt(i, 0), tableModel.getValueAt(i, 1));
		}

		return result;
	}

}
