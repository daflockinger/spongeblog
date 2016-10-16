package exceptions;

public class ModelAlreadyExistsException extends GeneralServiceException{
	public ModelAlreadyExistsException(String message){
		super(message + " already exists");
	}
}
