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
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.BlogService;

public class BlogController extends Controller {

	@Inject
	SimpleController<BlogDTO, Blog, BlogService> simple;

	@Inject 
	BlogService blogService;
	
	@Inject
	protected JsonHelper jsonHelper;

	public Result create() {
		RequestBody body = request().body();
		BlogDTO model = jsonHelper.extractModel(body, BlogDTO.class);
		BlogDTO resultModel = new BlogDTO();

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(blogService.errorModel(INVALID_JSON));
		}

		try {
			resultModel = blogService.create(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = blogService.errorModel(VALIDATION_FAILED,validationException.getMessage());
		} catch (BlogOnlyOneInstanceAllowedException e) {
			resultModel = blogService.errorModel(ACTION_NOT_ALLOWED,e.getMessage());
		} catch (ModelAlreadyExistsException e) {
			resultModel = blogService.errorModel(ALREADY_EXISTS);
		}

		return jsonHelper.getResponse(resultModel,BlogDTO.class);
	}

	public Result findById(String id) {
		return jsonHelper.getResponse(simple.findById(id),BlogDTO.class);
	}

	public Result findAll() {
		List<BlogDTO> blogs = new ArrayList<>();
		
		try {
			blogs = blogService.findAll();
		} catch (BlogOnlyOneInstanceAllowedException e) {
			blogs = ImmutableList.of(blogService.errorModel(ACTION_NOT_ALLOWED,e.getMessage()));
		}
		
		return jsonHelper.getResponses(blogs, BlogDTO.class);
	}

	public Result update(String id) {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.update(jsonHelper.extractModel(body,BlogDTO.class),id),BlogDTO.class);
	}

	public Result delete(String id) {
		return jsonHelper.getResponse(simple.delete(id),BlogDTO.class);
	}

	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}
	
	public void setSimple(SimpleController<BlogDTO, Blog, BlogService> simple) {
		this.simple = simple;
	}
}
