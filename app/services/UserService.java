package services;

import com.google.inject.Inject;

import dao.UserDAO;
import model.User;

public class UserService extends BaseServiceImpl<User, UserDAO> {
	
	@Inject
	private UserDAO dao;

	@Override
	protected boolean isNotUnique(User model) {
		return dao().exists(dao().createQuery().filter("login", model.getLogin()));
	}

	@Override
	public Class<User> getModelClass() {
		return User.class;
	}

	@Override
	protected UserDAO dao() {
		return dao;
	}
	
	public void setDao(UserDAO dao){
		this.dao = dao;
	}
}
