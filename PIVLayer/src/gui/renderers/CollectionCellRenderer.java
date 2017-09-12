package gui.renderers;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellRenderer;

public abstract class CollectionCellRenderer extends DefaultCellEditor implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	protected JTextField txtEdit;
	private Border errorBorder;
	private Border correctBorder;

	protected ChangeEvent changeEvent = new ChangeEvent(this);

	protected EventListenerList listenerList = new EventListenerList();
	
	public CollectionCellRenderer(JTextField txtEdit) {
		super(txtEdit);
		errorBorder = new LineBorder(Color.red);
		correctBorder = txtEdit.getBorder();
		this.txtEdit = txtEdit;		
	}

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		return new JLabel(arrayToString(arg1));
	}

	@Override
	public Object getCellEditorValue() {
		String value = txtEdit.getText();
		return stringToArray(value);
	}

	@Override
	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
		txtEdit.setBorder(correctBorder);
		txtEdit.setText(arrayToString(arg1));
		return txtEdit;
	}

	public void addCellEditorListener(CellEditorListener listener) {
		listenerList.add(CellEditorListener.class, listener);
	}

	public void removeCellEditorListener(CellEditorListener listener) {
		listenerList.remove(CellEditorListener.class, listener);
	}

	protected void fireEditingStopped() {
		CellEditorListener listener;
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == CellEditorListener.class) {
				listener = (CellEditorListener) listeners[i + 1];
				listener.editingStopped(changeEvent);
			}
		}
	}

	protected void fireEditingCanceled() {
		CellEditorListener listener;
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == CellEditorListener.class) {
				listener = (CellEditorListener) listeners[i + 1];
				listener.editingCanceled(changeEvent);
			}
		}
	}

	protected String getText() {
		return txtEdit.getText();
	}

	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	public boolean stopCellEditing() {
		if (cellValidate()) {
			fireEditingStopped();
			return true;
		} else {
			txtEdit.setBorder(errorBorder);
			return false;
		}
	}	

	@Override
	public boolean isCellEditable(EventObject arg0) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		return true;
	}

	protected abstract Object stringToArray(String stringValue);

	protected abstract String arrayToString(Object array);

	protected abstract boolean cellValidate();
}