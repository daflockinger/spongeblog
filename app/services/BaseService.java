package services;

import java.util.List;

import org.bson.types.ObjectId;
import dto.PaginationDTO;
import dto.RestError;

public interface BaseService<M> {
	public M create(M model);

	public M findById(ObjectId id);

	public M update(M model);
	
	public M delete(ObjectId id);
	
	public Class<M> getModelClass();
	
	public List<M> findAll();
	
	public M errorModel(RestError message);
}
