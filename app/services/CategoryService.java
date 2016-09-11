package services;

import dao.CategoryDAO;
import model.Category;

public class CategoryService extends BaseService<Category, CategoryDAO> {

	@Override
	protected boolean isNotUnique(Category model) {
		return dao().exists(dao().createQuery().filter("name", model.getName()));
	}
}
