package services;

import dao.UserDAO;
import model.User;

public class UserService extends BaseService<User, UserDAO> {

	@Override
	protected boolean isNotUnique(User model) {
		return dao().exists(dao().createQuery().filter("login", model.getLogin()));
	}
}
