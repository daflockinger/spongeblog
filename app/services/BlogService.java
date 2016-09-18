package services;

import com.google.inject.Inject;

import dao.BlogDAO;
import model.Blog;

public class BlogService extends BaseServiceImpl<Blog, BlogDAO>{
	
	@Inject
	private BlogDAO dao;
	
	@Override
	protected boolean isNotUnique(Blog model) {
		return dao.exists(dao.createQuery().filter("name", model.getName()));
	}

	@Override
	public Class<Blog> getModelClass() {
		return Blog.class;
	}

	@Override
	protected BlogDAO dao() {
		return dao;
	}
	
	public void setDao(BlogDAO dao) {
		this.dao = dao;
	}
}
