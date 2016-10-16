package controllers;

import com.google.inject.Inject;

import dto.UserDTO;
import model.User;
import services.UserService;

public class UserController extends BaseController<UserService, UserDTO, User>{

	@Inject
	private UserService service;
	
	@Override
	protected UserService service() {
		return service;
	}

	public void setService(UserService service) {
		this.service = service;
	}
}
