package controllers;

import com.google.inject.Inject;

import dto.CategoryDTO;
import exceptions.GeneralServiceException;
import helpers.JsonHelper;
import model.Category;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.CategoryService;

public class CategoryController extends Controller {

	@Inject
	private SimpleController<CategoryDTO, Category, CategoryService> simple;

	@Inject
	protected JsonHelper jsonHelper;

	public Result create() {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.create(jsonHelper.extractModel(body, CategoryDTO.class)));
	}

	public Result findById(String id) {
		return jsonHelper.getResponse(simple.findById(id));
	}

	public Result findAll() throws GeneralServiceException {
		return jsonHelper.getResponses(simple.findAll(), CategoryDTO.class);
	}

	public Result update(String id) {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.update(jsonHelper.extractModel(body, CategoryDTO.class), id));
	}

	public Result delete(String id) {
		return jsonHelper.getResponse(simple.delete(id));
	}

	public void setSimple(SimpleController<CategoryDTO, Category, CategoryService> simple) {
		this.simple = simple;
	}
}
