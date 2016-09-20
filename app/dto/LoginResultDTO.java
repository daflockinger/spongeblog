package dto;

import model.BaseModel;
import model.UserStatus;

public class LoginResultDTO  extends BaseModel{
	private UserStatus userStatus;
	private String errorMessage;
	
	public LoginResultDTO(UserStatus userStatus){
		this.userStatus = userStatus;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
