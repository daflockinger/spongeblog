package controllers;

import com.google.inject.Inject;

import model.Blog;
import services.BlogService;

public class BlogController extends BaseController<BlogService, Blog>{

	@Inject 
	BlogService blogService;

	@Override
	protected BlogService service() {
		return blogService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}
}
