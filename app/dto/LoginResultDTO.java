package dto;

import model.UserStatus;

public class LoginResultDTO {
	private UserStatus status;
	
	public LoginResultDTO(UserStatus status){
		this.status = status;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
