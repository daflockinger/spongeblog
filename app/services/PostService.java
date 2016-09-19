package services;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.google.inject.Inject;

import dao.PostDAO;
import dto.OperationResult;
import model.BaseModel;
import model.Post;
import model.PostStatus;

public class PostService extends BaseServiceImpl<Post, PostDAO>{

	@Inject
	private PostDAO dao;
	
	@Override
	public OperationResult<Post> delete(ObjectId id) {
		if (!existsWithId(id)) {
			return new OperationResult<Post>(errorModel(BaseModel.NOT_FOUND),HttpStatus.SC_NOT_FOUND);
		}
		Post postToDelete = dao().get(id);
		postToDelete.setStatus(PostStatus.DELETED);
		dao.save(postToDelete);

		return new OperationResult<Post>(postToDelete,HttpStatus.SC_OK);
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
	
	public void setDao(PostDAO dao){
		this.dao = dao;
	}
}
