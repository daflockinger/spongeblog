package services;

import org.apache.http.HttpStatus;
import org.mongodb.morphia.query.Query;

import com.google.inject.Inject;

import dao.UserDAO;
import dto.LoginCredentials;
import dto.LoginResultDTO;
import dto.OperationResult;
import model.BaseModel;
import model.User;
import play.Logger;

public class LoginService {

	@Inject
	private UserDAO dao;

	public OperationResult<LoginResultDTO> login(LoginCredentials credentials) {

		User foundUser = dao.find(createLoginQuery(credentials)).get();

		if (foundUser != null) {
			return new OperationResult<LoginResultDTO>(new LoginResultDTO(foundUser.getStatus()), HttpStatus.SC_OK);
		}

		return new OperationResult<LoginResultDTO>(errorModel(BaseModel.UNAUTHORIZED),HttpStatus.SC_FORBIDDEN);
	}

	private Query<User> createLoginQuery(LoginCredentials credentials) {
		return dao.createQuery().filter("login", credentials.getUser()).filter("password", credentials.getPassword());
	}

	public void setDao(UserDAO dao) {
		this.dao = dao;
	}

	public LoginResultDTO errorModel(String message) {
		LoginResultDTO errorModel = new LoginResultDTO(null);
		errorModel.setErrorMessage(message);
		return errorModel;
	}
}
