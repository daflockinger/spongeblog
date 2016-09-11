package services;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.google.inject.Inject;

import dao.ExtendedDAO;
import dto.OperationResult;
import model.BaseModel;

public abstract class BaseService<M extends BaseModel,T extends ExtendedDAO<M, String>> {
	@Inject
	private T dao;

	public OperationResult<M> create(M newUser) {

		if (isNotUnique(newUser)) {
			return new OperationResult<M>(HttpStatus.SC_CONFLICT);
		}
		dao.save(newUser);

		return new OperationResult<M>(newUser, HttpStatus.SC_CREATED);
	}

	protected abstract boolean isNotUnique(M model);

	public OperationResult<M> findById(String id) {
		M model = dao.get(id);

		if (model == null) {
			return new OperationResult<M>(HttpStatus.SC_NOT_FOUND);
		}
		return new OperationResult<M>(model, HttpStatus.SC_OK);
	}

	public OperationResult<M> update(M model) {
		if (isNotValid(model)) {
			return new OperationResult<M>(HttpStatus.SC_NOT_FOUND);
		}
		dao.save(model);

		return new OperationResult<M>(HttpStatus.SC_NO_CONTENT);
	}

	private boolean isNotValid(M model) {
		return StringUtils.isEmpty(model.getId()) || (StringUtils.isNotEmpty(model.getId())
				&& existsWithId(model.getId()) );
	}
	
	private boolean existsWithId(String id){
		return findById(id).getStatus() != HttpStatus.SC_NOT_FOUND;
	}
	
	public OperationResult<M> delete(String id){
		if(!existsWithId(id)){
			return new OperationResult<M>(HttpStatus.SC_NOT_FOUND);
		}
		dao.deleteById(id);
		
		return new OperationResult<M>(HttpStatus.SC_NO_CONTENT);
	}
	
	protected T dao(){
		return dao;
	}
}
