package pivLayer;

public class FilterException extends Exception {

	private static final long serialVersionUID = 1L;

	public FilterException(Exception e) {
		super(e);
	}

	public FilterException(String cause, Exception e) {
		super(cause, e);
	}
	
	public FilterException(String cause) {
		super(cause);
	}

}
