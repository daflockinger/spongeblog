package services;

import java.util.List;

import org.bson.types.ObjectId;

import dto.OperationResult;
import dto.PaginationDTO;

public interface BaseService<M> {
	public OperationResult<M> create(M model);

	public OperationResult<M> findById(ObjectId id);

	public OperationResult<M> update(M model);
	
	public OperationResult<M> delete(ObjectId id);
	
	public Class<M> getModelClass();
	
	public OperationResult<List<?>> findAllInPage(PaginationDTO settings);
}
