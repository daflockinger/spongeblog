package services;

import static dto.RestError.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.google.inject.Inject;

import dao.PostDAO;
import dto.PaginationDTO;
import dto.PostDTO;
import exceptions.ModelNotFoundException;
import model.Post;
import model.PostStatus;

public class PostService extends PaginationServiceImpl<PostDTO,Post, PostDAO> {

	@Inject
	private PostDAO dao;

	@Override
	public PostDTO delete(ObjectId id) throws ModelNotFoundException{

		if (!existsWithId(id)) {
			throw new ModelNotFoundException("Post");
		}
		PostDTO postToDelete = mapper().map(dao().get(id),PostDTO.class);
		postToDelete.setPostStatus(PostStatus.DELETED.toString());
		dao.save(mapper().map(postToDelete,Post.class));

		return postToDelete;
	}
	
	public PostDTO findByTitle(String title){
		PostDTO model = mapper().map(
				dao().findOne(dao().createQuery().filter("title", title.replaceAll("_", " "))),PostDTO.class);

		if (model == null) {
			return errorModel(NOT_FOUND);
		}
		return model;
	}

	@Override
	public List<PostDTO> findAllInPage(PaginationDTO settings) {
		List<PostDTO> foundOnes = super.findAllInPage(settings);
		boolean hasPreviousPage = hasPreviousPage(settings);

		return foundOnes.stream().map(post -> {
			post.setHasPreviousPage(hasPreviousPage);
			return post;
		}).collect(Collectors.toList());
	}

	@Override
	public List<PostDTO> findAll() {
		return new ArrayList<>();
	}

	@Override
	protected boolean isNotUnique(PostDTO model) {
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

	@Override
	public Class<PostDTO> getDTOClass() {
		return PostDTO.class;
	}
}
