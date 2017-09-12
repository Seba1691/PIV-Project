package manager;

public class ManagerException extends Exception {

	private static final long serialVersionUID = 1L;

	public ManagerException(Exception e) {
		super(e);
	}

	public ManagerException(String cause, Exception e) {
		super(cause, e);
	}

	public ManagerException(String cause) {
		super(cause);
	}

}
