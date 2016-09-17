package services;

import com.google.inject.Inject;

import dao.PostDAO;
import model.Post;

public class PostService extends BaseServiceImpl<Post, PostDAO>{

	@Inject
	private PostDAO dao;
	
	@Override
	protected boolean isNotUnique(Post model) {
		return false;
	}

	@Override
	protected Class<Post> getModelClass() {
		return Post.class;
	}

	@Override
	protected PostDAO dao() {
		return dao;
	}
	
	public void setDao(PostDAO dao){
		this.dao = dao;
	}
}
