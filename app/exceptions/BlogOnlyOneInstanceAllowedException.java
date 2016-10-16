package exceptions;

public class BlogOnlyOneInstanceAllowedException extends GeneralServiceException{

	public BlogOnlyOneInstanceAllowedException(String message){
		super(message);
	}
}
