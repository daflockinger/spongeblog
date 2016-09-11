package services;

import dao.PostDAO;
import model.Post;

public class PostService extends BaseService<Post, PostDAO>{

	@Override
	protected boolean isNotUnique(Post model) {
		return false;
	}
}
