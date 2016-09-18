package services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.google.inject.Inject;
import com.mongodb.QueryBuilder;

import dao.ExtendedDAO;
import dto.OperationResult;
import dto.PaginationDTO;
import dto.PaginationQueryDTO;
import model.BaseModel;

public abstract class BaseServiceImpl<M extends BaseModel, T extends ExtendedDAO<M, ObjectId>> implements BaseService<M> {
	
	protected abstract T dao();
	
	public OperationResult<M> create(M model) {

		if (model == null || (model != null && isNotUnique(model)) ) {
			return new OperationResult<M>(HttpStatus.SC_CONFLICT);
		}
		dao().save(model);

		return new OperationResult<M>(model, HttpStatus.SC_CREATED);
	}

	protected abstract boolean isNotUnique(M model);

	public OperationResult<M> findById(ObjectId id) {
		M model = dao().get(id);

		if (model == null) {
			return new OperationResult<M>(HttpStatus.SC_NOT_FOUND);
		}
		return new OperationResult<M>(model, HttpStatus.SC_OK);
	}
	
	public OperationResult<List<?>> findAllInPage(PaginationDTO settings){
		List<?> models = new ArrayList<>();
		models = dao().findAllInPage(new PaginationQueryDTO(settings, getModelClass()));
		
		if (models == null) {
			return new OperationResult<List<?>>(HttpStatus.SC_NOT_FOUND);
		}
		return new OperationResult<List<?>>(models, HttpStatus.SC_OK);
	}
	
	public abstract Class<M> getModelClass();
	

	public OperationResult<M> update(M model) {
		if (model == null || isNotValid(model)) {
			return new OperationResult<M>(HttpStatus.SC_NOT_FOUND);
		}
		dao().save(model);

		return new OperationResult<M>(HttpStatus.SC_NO_CONTENT);
	}

	private boolean isNotValid(M model) {
		return model.getId()==null;
	}

	protected boolean existsWithId(ObjectId id) {
		return findById(id).getStatus() != HttpStatus.SC_NOT_FOUND;
	}

	public OperationResult<M> delete(ObjectId id) {
		if (!existsWithId(id)) {
			return new OperationResult<M>(HttpStatus.SC_NOT_FOUND);
		}
		dao().deleteById(id);

		return new OperationResult<M>(HttpStatus.SC_NO_CONTENT);
	}
}
