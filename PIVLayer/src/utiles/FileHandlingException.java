package utiles;

public class FileHandlingException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileHandlingException(Exception e) {
		super(e);
	}

	public FileHandlingException(String cause, Exception e) {
		super(cause, e);
	}

	public FileHandlingException(String cause) {
		super(cause);
	}

}
