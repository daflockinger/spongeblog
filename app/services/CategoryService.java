package services;

import com.google.inject.Inject;

import dao.CategoryDAO;
import model.Category;

public class CategoryService extends BaseServiceImpl<Category, CategoryDAO> {

	@Inject
	private CategoryDAO dao;
	
	@Override
	protected boolean isNotUnique(Category model) {
		return dao().exists(dao().createQuery().filter("name", model.getName()));
	}

	@Override
	public Class<Category> getModelClass() {
		return Category.class;
	}

	@Override
	protected CategoryDAO dao() {
		return dao;
	}
	
	public void setDao(CategoryDAO dao){
		this.dao = dao;
	}
}
