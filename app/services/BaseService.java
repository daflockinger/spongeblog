package services;

import org.bson.types.ObjectId;

import dto.OperationResult;

public interface BaseService<M> {
	public OperationResult<M> create(M newUser);

	public OperationResult<M> findById(ObjectId id);

	public OperationResult<M> update(M model);
	
	public OperationResult<M> delete(ObjectId id);
}
