package wrapper;

public class WrapperException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public WrapperException(Exception e) {
		super(e);
	}
	
	public WrapperException(String cause, Exception e) {
		super(cause, e);
	}
	
	public WrapperException(String cause) {
		super(cause);
	}

}
