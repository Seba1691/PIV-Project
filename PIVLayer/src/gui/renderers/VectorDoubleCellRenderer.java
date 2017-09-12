package gui.renderers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

public class VectorDoubleCellRenderer extends VectorCellRenderer {

	private static final long serialVersionUID = 1L;

	public VectorDoubleCellRenderer(JTextField txtEdit) {
		super(txtEdit);
	}

	public Object[] getResultArray(int length) {
		return new Double[length];
	}

	public Object valueOf(String data) {
		return Double.valueOf(data);
	}
	
	@Override
	protected boolean cellValidate() {
		String matrixRegex = "( )*\\[(( )*\\d+(\\.\\d+)?( )*(,( )*\\d+(\\.\\d+)?( )*)*)?\\]( )*";
		Pattern pattern = Pattern.compile(matrixRegex);
		Matcher matcher = pattern.matcher(getText());
		return matcher.matches();
	}

}