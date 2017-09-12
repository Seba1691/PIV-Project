package gui.renderers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

public class MatrixStringCellRenderer extends MatrixCellRenderer {

	private static final long serialVersionUID = 1L;

	public MatrixStringCellRenderer(JTextField txtEdit) {
		super(txtEdit);
	}

	protected Object[][] getResultMatrix(int length) {
		return new String[length][];
	}

	protected Object[] getResultArray(int length) {
		return new String[length];
	}

	protected Object valueOf(String data) {
		return data;
	}
	
	@Override
	protected boolean cellValidate() {
		String matrixRegex = "( )*\\[( )*\\[(( )*\\w+(( )*\\w*)*( )*(,( )*\\w+(( )*\\w*)*( )*)*)?\\]( )*(,( )*\\[( )*\\w+(( )*\\w*)*( )*(,( )*\\w+(( )*\\w*)*( )*)*\\]( )*)*\\]( )*";
		Pattern pattern = Pattern.compile(matrixRegex);
		Matcher matcher = pattern.matcher(getText());
		return matcher.matches();
	}

}
