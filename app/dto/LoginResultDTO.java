package dto;

import model.UserStatus;

public class LoginResultDTO {
	private UserStatus status;
	private String errorMessage;
	
	public LoginResultDTO(UserStatus status){
		this.status = status;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
