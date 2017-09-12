package gui;

import javax.swing.JOptionPane;

public class GUIException extends Exception {

	private static final long serialVersionUID = 1L;

	public GUIException(Exception e) {
		super(e.getMessage(),e);
	}

	public GUIException(String cause, Exception e) {
		super(cause, e);
	}

	public GUIException(String cause) {
		super(cause);
	}

	public void inform() {
		JOptionPane.showMessageDialog(null, getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		printStackTrace();
	}

}
