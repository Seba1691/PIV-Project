package gui.renderers;

import javax.swing.JTextField;

public abstract class MatrixCellRenderer extends CollectionCellRenderer {

	private static final long serialVersionUID = 1L;

	public MatrixCellRenderer(JTextField txtEdit) {
		super(txtEdit);
	}

	@Override
	protected Object stringToArray(String stringValue) {
		String[] vecArray = stringValue.split("]( )*,( )*\\[");
		Object[][] matrix = getResultMatrix(vecArray.length);
		for (int i = 0; i < vecArray.length; i++) {
			vecArray[i] = vecArray[i].replace("[", "");
			vecArray[i] = vecArray[i].replace("]", "");
			String[] str2 = vecArray[i].split(",");
			matrix[i] = getResultArray(str2.length);
			for (int j = 0; j < str2.length; j++) {
				matrix[i][j] = valueOf(str2[j].trim());
			}
		}
		return matrix;
	}

	@Override
	protected String arrayToString(Object array) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < ((Object[][]) array).length; i++) {
			sb.append("[");
			for (int j = 0; j < ((Object[][]) array)[i].length; j++)
				sb.append(j == ((Object[][]) array)[i].length - 1 ? ((Object[][]) array)[i][j] : ((Object[][]) array)[i][j] + ", ");
			sb.append(i == ((Object[][]) array).length - 1 ? "]" : "], ");
		}
		sb.append("]");
		return sb.toString();
	}

	protected abstract Object[][] getResultMatrix(int length);

	protected abstract Object[] getResultArray(int length);

	protected abstract Object valueOf(String data);

}
