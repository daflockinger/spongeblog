package controllers;

import com.google.inject.Inject;

import model.Post;
import services.PostService;

public class PostController extends BaseController<PostService, Post>{

	@Inject
	private PostService service;
	
	@Override
	protected PostService service() {
		return service;
	}

	public void setService(PostService service) {
		this.service = service;
	}
}
