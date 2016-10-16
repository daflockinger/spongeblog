package controllers;

import com.google.inject.Inject;

import dto.PostDTO;
import model.Post;
import play.mvc.Result;
import services.PostService;

public class PostController extends PaginationController<PostService, PostDTO,Post>{

	@Inject
	private PostService service;
	
	@Override
	public Result findById(String id) {
		return jsonHelper.getResponse(service().findByTitle(id));
	}
	
	@Override
	protected PostService service() {
		return service;
	}

	public void setService(PostService service) {
		this.service = service;
	}
}
