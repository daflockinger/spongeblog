package exceptions;

public class ModelNotFoundException extends GeneralServiceException{
	public ModelNotFoundException(String message){
		super(message + " not found");
	}
}
