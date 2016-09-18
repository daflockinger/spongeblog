package controllers;

import com.google.inject.Inject;

import model.Category;
import services.CategoryService;

public class CategoryController extends BaseController<CategoryService,Category> {

	@Inject
	private CategoryService service;
	
	@Override
	protected CategoryService service() {
		return service;
	}

	public void setService(CategoryService service) {
		this.service = service;
	}
}
