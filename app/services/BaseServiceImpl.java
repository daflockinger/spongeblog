package services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.google.common.collect.ImmutableList;
import static dto.RestError.*;
import dao.ExtendedDAO;
import dto.PaginationDTO;
import dto.PaginationQueryDTO;
import dto.RestError;
import model.BaseModel;
import play.Logger;

public abstract class BaseServiceImpl<M extends BaseModel, T extends ExtendedDAO<M, ObjectId>>
		implements BaseService<M> {

	protected abstract T dao();

	public M create(M model) {

		if (model == null || (model != null && isNotUnique(model))) {
			return errorModel(ALREADY_EXISTS);
		}
		dao().save(model);
		model.setStatus(HttpStatus.SC_CREATED);

		return  model;
	}

	protected abstract boolean isNotUnique(M model);

	public M findById(ObjectId id) {
		M model = dao().get(id);

		if (model == null) {
			return errorModel(NOT_FOUND);
		}
		return model;
	}

	public List<M> findAllInPage(PaginationDTO settings) {
		List<M> models = new ArrayList<>();
		models = dao().findAllInPage(new PaginationQueryDTO<M>(settings, getModelClass()));

		if (models == null) {
			return ImmutableList.of(errorModel(NOT_FOUND));
		}
		return models;
	}

	public M errorModel(RestError message) {
		try {
			M errorModel = getModelClass().newInstance();
			errorModel.setErrorMessage(message.toString());
			errorModel.setStatus(message.status());
			return errorModel;
		} catch (InstantiationException e) {
			Logger.error("Create model Instance failed", e);
		} catch (IllegalAccessException e) {
			Logger.error("Create model Instance failed", e);
		}
		return null;
	}

	public abstract Class<M> getModelClass();

	public M update(M model) {
		if (model == null || isNotValid(model)) {
			return errorModel(NOT_FOUND);
		}
		dao().save(model);

		return model;
	}

	private boolean isNotValid(M model) {
		return model.getId() == null;
	}

	protected boolean existsWithId(ObjectId id) {
		return findById(id).getStatus() != HttpStatus.SC_NOT_FOUND;
	}

	public M delete(ObjectId id) {
		if (!existsWithId(id)) {
			return errorModel(NOT_FOUND);
		}
		M toDelete = findById(id);
		dao().deleteById(id);

		return toDelete;
	}
}
