package services;

import static model.BaseModel.ALREADY_EXISTS;
import static model.BaseModel.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.google.common.collect.ImmutableList;

import dao.ExtendedDAO;
import dto.OperationResult;
import dto.PaginationDTO;
import dto.PaginationQueryDTO;
import model.BaseModel;
import play.Logger;

public abstract class BaseServiceImpl<M extends BaseModel, T extends ExtendedDAO<M, ObjectId>>
		implements BaseService<M> {

	protected abstract T dao();

	public OperationResult<M> create(M model) {

		if (model == null || (model != null && isNotUnique(model))) {
			return new OperationResult<M>(errorModel(ALREADY_EXISTS), HttpStatus.SC_CONFLICT);
		}
		dao().save(model);

		return new OperationResult<M>(model, HttpStatus.SC_CREATED);
	}

	protected abstract boolean isNotUnique(M model);

	public OperationResult<M> findById(ObjectId id) {
		M model = dao().get(id);

		if (model == null) {
			return new OperationResult<M>(errorModel(NOT_FOUND), HttpStatus.SC_NOT_FOUND);
		}
		return new OperationResult<M>(model, HttpStatus.SC_OK);
	}

	public OperationResult<List<?>> findAllInPage(PaginationDTO settings) {
		List<?> models = new ArrayList<>();
		models = dao().findAllInPage(new PaginationQueryDTO<M>(settings, getModelClass()));

		if (models == null) {
			return new OperationResult<List<?>>(ImmutableList.of(errorModel(NOT_FOUND)), HttpStatus.SC_NOT_FOUND);
		}
		return new OperationResult<List<?>>(models, HttpStatus.SC_OK);
	}

	public M errorModel(String message) {
		try {
			M errorModel = getModelClass().newInstance();
			errorModel.setErrorMessage(message);
			return errorModel;
		} catch (InstantiationException e) {
			Logger.error("Create model Instance failed", e);
		} catch (IllegalAccessException e) {
			Logger.error("Create model Instance failed", e);
		}
		return null;
	}

	public abstract Class<M> getModelClass();

	public OperationResult<M> update(M model) {
		if (model == null || isNotValid(model)) {
			return new OperationResult<M>(errorModel(NOT_FOUND), HttpStatus.SC_NOT_FOUND);
		}
		dao().save(model);

		return new OperationResult<M>(model, HttpStatus.SC_OK);
	}

	private boolean isNotValid(M model) {
		return model.getId() == null;
	}

	protected boolean existsWithId(ObjectId id) {
		return findById(id).getStatus() != HttpStatus.SC_NOT_FOUND;
	}

	public OperationResult<M> delete(ObjectId id) {
		if (!existsWithId(id)) {
			return new OperationResult<M>(errorModel(NOT_FOUND), HttpStatus.SC_NOT_FOUND);
		}
		dao().deleteById(id);

		return new OperationResult<M>(findById(id).getEntity(), HttpStatus.SC_OK);
	}
}
