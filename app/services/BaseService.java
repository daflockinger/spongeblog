package services;

import java.util.List;

import org.bson.types.ObjectId;
import dto.PaginationDTO;
import dto.RestError;
import exceptions.GeneralServiceException;
import exceptions.ModelNotFoundException;

public interface BaseService<M,N> {
	public M create(M model) throws GeneralServiceException;

	public M findById(ObjectId id) throws GeneralServiceException;

	public M update(M model) throws GeneralServiceException;
	
	public M delete(ObjectId id) throws GeneralServiceException;
	
	public abstract Class<N> getModelClass();
	public abstract Class<M> getDTOClass();
	
	public List<M> findAll() throws GeneralServiceException;
	
	public M errorModel(RestError message);
	
	public M errorModel(RestError message,String addonMessage);
}
