package services;

import static dto.RestError.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.google.inject.Inject;

import dao.PostDAO;
import dto.PaginationDTO;
import model.Post;
import model.PostStatus;

public class PostService extends PaginationServiceImpl<Post, PostDAO> {

	@Inject
	private PostDAO dao;

	@Override
	public Post delete(ObjectId id) {
		if (!existsWithId(id)) {
			return errorModel(NOT_FOUND);
		}
		Post postToDelete = dao().get(id);
		postToDelete.setPostStatus(PostStatus.DELETED);
		dao.save(postToDelete);

		return postToDelete;
	}
	
	public Post findByTitle(String title){
		Post model = dao().findOne(dao().createQuery().filter("title", title.replaceAll("_", " ")));

		if (model == null) {
			return errorModel(NOT_FOUND);
		}
		return model;
	}

	@Override
	public List<Post> findAllInPage(PaginationDTO settings) {
		List<Post> foundOnes = super.findAllInPage(settings);
		boolean hasPreviousPage = hasPreviousPage(settings);

		return foundOnes.stream().map(post -> {
			post.setHasPreviousPage(hasPreviousPage);
			return post;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Post> findAll() {
		return new ArrayList<>();
	}

	@Override
	protected boolean isNotUnique(Post model) {
		return false;
	}

	@Override
	public Class<Post> getModelClass() {
		return Post.class;
	}

	@Override
	protected PostDAO dao() {
		return dao;
	}

	public void setDao(PostDAO dao) {
		this.dao = dao;
	}
}
