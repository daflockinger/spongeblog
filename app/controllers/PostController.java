package controllers;

import com.google.inject.Inject;

import dto.PaginationDTO;
import dto.PostDTO;
import exceptions.GeneralServiceException;
import helpers.JsonHelper;
import helpers.PaginationMapper;
import model.Post;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.PostService;

public class PostController extends Controller {

	@Inject
	private PostService service;

	@Inject
	private SimpleController<PostDTO, Post, PostService> simple;

	@Inject
	protected JsonHelper jsonHelper;

	@Inject
	private PaginationMapper mapper;

	public Result findById(String id) {
		return jsonHelper.getResponse(service.findByTitle(id), PostDTO.class);
	}

	public Result create() {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.create(jsonHelper.extractModel(body, PostDTO.class)), PostDTO.class);
	}

	private Result findAllInPage(PaginationDTO settings) {
		return jsonHelper.getResponses(service.findAllInPage(settings), PostDTO.class);
	}

	public Result findAll() throws GeneralServiceException {
		PaginationDTO pagination = mapper.mapParamsToPagination(request().queryString());

		if (pagination != null) {
			return findAllInPage(pagination);
		} else {
			return jsonHelper.getResponses(simple.findAll(), PostDTO.class);
		}
	}

	public Result update(String id) {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.update(jsonHelper.extractModel(body, PostDTO.class), id), PostDTO.class);
	}

	public Result delete(String id) {
		return jsonHelper.getResponse(simple.delete(id), PostDTO.class);
	}

	public void setSimple(SimpleController<PostDTO, Post, PostService> simple) {
		this.simple = simple;
	}
}
