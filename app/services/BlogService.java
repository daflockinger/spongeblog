package services;

import dao.BlogDAO;
import model.Blog;

public class BlogService extends BaseService<Blog, BlogDAO>{

	@Override
	protected boolean isNotUnique(Blog model) {
		return false;//TODO implement
	}
}
