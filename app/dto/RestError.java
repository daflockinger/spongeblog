package dto;

import org.apache.http.HttpStatus;

public enum RestError {
	INVALID_JSON("Invalid/Malformed Json",HttpStatus.SC_BAD_REQUEST),
	ALREADY_EXISTS("Entity already exists",HttpStatus.SC_CONFLICT),
	NOT_FOUND("Entity not found",HttpStatus.SC_NOT_FOUND),
	UNAUTHORIZED("Username or password is not valid",HttpStatus.SC_FORBIDDEN);
	
	private int status;
	private String name;
	
	private RestError(String name, int status){
		this.name = name;
		this.status = status;
	}
	
	@Override
    public String toString() {
        return name;
    }
		
	public int status(){
		return status;
	}
}
