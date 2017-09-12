package gui;

import gui.renderers.MatrixIntegerCellRenderer;
import gui.renderers.MatrixStringCellRenderer;
import gui.renderers.VectorDoubleCellRenderer;
import gui.renderers.VectorIntegerCellRenderer;
import gui.renderers.VectorStringCellRenderer;

import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class FilterConfiguratonTable extends JTable {

	private static final long serialVersionUID = 1L;

	private HashMap<Class<?>, TableCellRenderer> renderers;

	private HashMap<Class<?>, DefaultCellEditor> editors;

	private Class<?> editingClass;

	public FilterConfiguratonTable() {
		super();
		InitCellRenderers();
		InitCellEditors();
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (modelColumn == 1) {
			Class<?> cellClass = getModel().getValueAt(row, modelColumn).getClass();
			return getRenderer(cellClass);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (modelColumn == 1) {
			editingClass = getModel().getValueAt(row, modelColumn).getClass();
			return getEditor(editingClass);
		} else {
			return super.getCellEditor(row, column);
		}
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return editingClass != null ? editingClass : super.getColumnClass(column);
	}

	private void InitCellEditors() {
		editors = new HashMap<Class<?>, DefaultCellEditor>();
		editors.put(Integer[].class, new VectorIntegerCellRenderer(new JTextField()));
		editors.put(String[].class, new VectorStringCellRenderer(new JTextField()));
		editors.put(Double[].class, new VectorDoubleCellRenderer(new JTextField()));
		editors.put(Integer[][].class, new MatrixIntegerCellRenderer(new JTextField()));
		editors.put(String[][].class, new MatrixStringCellRenderer(new JTextField()));

	}

	private void InitCellRenderers() {
		renderers = new HashMap<Class<?>, TableCellRenderer>();
		renderers.put(Integer[].class, new VectorIntegerCellRenderer(new JTextField()));
		renderers.put(String[].class, new VectorStringCellRenderer(new JTextField()));
		renderers.put(Double[].class, new VectorDoubleCellRenderer(new JTextField()));
		renderers.put(Integer[][].class, new MatrixIntegerCellRenderer(new JTextField()));
		renderers.put(String[][].class, new MatrixStringCellRenderer(new JTextField()));
	}

	private TableCellRenderer getRenderer(Class<?> cellClass) {
		TableCellRenderer result = renderers.get(cellClass);
		return result != null ? result : new JTable().getDefaultRenderer(cellClass);
	}

	private TableCellEditor getEditor(Class<?> cellClass) {
		TableCellEditor result = editors.get(cellClass);
		return result != null ? result : new JTable().getDefaultEditor(cellClass);
	}

}
