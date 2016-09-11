package services;

import com.google.inject.Inject;

import dao.UserDAO;
import dto.LoginCredentials;
import dto.LoginResultDTO;

public class LoginService {
	
	@Inject
	private UserService userService;
	
	public LoginResultDTO login(LoginCredentials credentials){
		
		return null;//TODO implement
	}
	
}
