package gui.renderers;

import javax.swing.JTextField;

public abstract class VectorCellRenderer extends CollectionCellRenderer {

	private static final long serialVersionUID = 1L;

	public VectorCellRenderer(JTextField txtEdit) {
		super(txtEdit);
	}

	@Override
	protected Object stringToArray(String stringValue) {
		stringValue = stringValue.replace("[", "");
		stringValue = stringValue.replace("]", "");
		String[] stringArray = stringValue.split(",");
		Object[] result = getResultArray(stringArray.length);
		for (int i = 0; i < stringArray.length; i++)
			result[i] = valueOf(stringArray[i].trim());
		return result;
	}

	@Override
	protected String arrayToString(Object array) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < ((Object[]) array).length; i++)
			sb.append(i == ((Object[]) array).length - 1 ? ((Object[]) array)[i] : ((Object[]) array)[i] + ", ");
		sb.append("]");
		return sb.toString();
	}

	protected abstract Object[] getResultArray(int length);

	protected abstract Object valueOf(String data);

}
