package gui.renderers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

public class MatrixIntegerCellRenderer extends MatrixCellRenderer {

	private static final long serialVersionUID = 1L;

	public MatrixIntegerCellRenderer(JTextField txtEdit) {
		super(txtEdit);
	}

	public Object[][] getResultMatrix(int length) {
		return new Integer[length][];
	}

	public Object[] getResultArray(int length) {
		return new Integer[length];
	}

	public Object valueOf(String data) {
		return Integer.valueOf(data);
	}
	
	@Override
	protected boolean cellValidate() {
		String matrixRegex = "( )*\\[( )*\\[(( )*\\d+( )*(,( )*\\d+( )*)*)?\\]( )*(,( )*\\[( )*\\d+( )*(,( )*\\d+( )*)*\\]( )*)*\\]( )*";
		Pattern pattern = Pattern.compile(matrixRegex);
		Matcher matcher = pattern.matcher(getText());
		return matcher.matches();
	}

}