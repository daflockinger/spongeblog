package services;

import org.mongodb.morphia.query.Query;

import com.google.inject.Inject;

import dao.UserDAO;
import dto.LoginCredentials;
import dto.LoginResultDTO;
import dto.RestError;
import model.BaseModel;
import model.User;

public class LoginService {

	@Inject
	private UserDAO dao;

	public LoginResultDTO login(LoginCredentials credentials) {

		User foundUser = dao.find(createLoginQuery(credentials)).get();

		if (foundUser != null) {
			return new LoginResultDTO(foundUser.getUserStatus());
		}

		return errorModel(RestError.UNAUTHORIZED);
	}

	private Query<User> createLoginQuery(LoginCredentials credentials) {
		return dao.createQuery().filter("login", credentials.getUser()).filter("password", credentials.getPassword());
	}

	public void setDao(UserDAO dao) {
		this.dao = dao;
	}

	public LoginResultDTO errorModel(RestError message) {
		LoginResultDTO errorModel = new LoginResultDTO(null);
		errorModel.setErrorMessage(message.toString());
		errorModel.setStatus(message.status());
		return errorModel;
	}
}
