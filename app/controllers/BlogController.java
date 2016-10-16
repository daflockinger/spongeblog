package controllers;

import static dto.RestError.ACTION_NOT_ALLOWED;
import static dto.RestError.ALREADY_EXISTS;
import static dto.RestError.INVALID_JSON;
import static dto.RestError.VALIDATION_FAILED;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.VerboseJSR303ConstraintViolationException;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import dto.BlogDTO;
import exceptions.BlogOnlyOneInstanceAllowedException;
import exceptions.ModelAlreadyExistsException;
import helpers.JsonHelper;
import model.Blog;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.BlogService;

public class BlogController extends BaseController<BlogService, BlogDTO, Blog>{

	@Inject 
	BlogService blogService;
	
	@Inject
	protected JsonHelper jsonHelper;


	public Result create() {
		RequestBody body = request().body();
		BlogDTO model = jsonHelper.extractModel(body, BlogDTO.class);
		BlogDTO resultModel = new BlogDTO();

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		}

		try {
			resultModel = service().create(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = service().errorModel(VALIDATION_FAILED,validationException.getMessage());
		} catch (BlogOnlyOneInstanceAllowedException e) {
			resultModel = service().errorModel(ACTION_NOT_ALLOWED,e.getMessage());
		} catch (ModelAlreadyExistsException e) {
			resultModel = service().errorModel(ALREADY_EXISTS);
		}

		return jsonHelper.getResponse(resultModel);
	}

	public Result findById(String id) {
		return super.findById(id);
	}

	public Result findAll() {
		List<BlogDTO> blogs = new ArrayList<>();
		
		try {
			blogs = service().findAll();
		} catch (BlogOnlyOneInstanceAllowedException e) {
			blogs = ImmutableList.of(service().errorModel(ACTION_NOT_ALLOWED,e.getMessage()));
		}
		
		return jsonHelper.getResponses(blogs, BlogDTO.class);
	}

	public Result update(String id) {
		//TODO transform dto to model
		return super.update(id);
	}

	public Result delete(String id) {
		return super.delete(id);
	}

	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}
	
	@Override
	protected BlogService service() {
		return blogService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}
}
