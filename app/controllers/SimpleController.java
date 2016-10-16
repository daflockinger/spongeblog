package controllers;

import static dto.RestError.INVALID_JSON;
import static dto.RestError.VALIDATION_FAILED;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.VerboseJSR303ConstraintViolationException;

import com.google.inject.Inject;

import dto.BaseDTO;
import dto.RestError;
import exceptions.GeneralServiceException;
import model.BaseModel;
import services.BaseService;

public class SimpleController<M extends BaseDTO, N extends BaseModel, T extends BaseService<M, N>> {
	
	@Inject
	private T service;
	
	public M create(M model){
		
		M resultModel;

		if (model == null) {
			return service.errorModel(INVALID_JSON);
		}

		try {
			resultModel = service.create(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = service.errorModel(VALIDATION_FAILED,validationException.getMessage());
		} catch (GeneralServiceException e) {
			resultModel = service.errorModel(RestError.ALREADY_EXISTS, service.getModelClass().getSimpleName());
		}
		
		return resultModel;
	}
	
	public M findById(String id) {
		ObjectId mongoId = null;
		M model = null;

		if (ObjectId.isValid(id)) {
			mongoId = new ObjectId(id);
		}
		
		try {
			model = service.findById(mongoId);
		} catch (GeneralServiceException e) {
			model = service.errorModel(RestError.NOT_FOUND, service.getModelClass().getSimpleName());
		}
		
		return model;
	}
	
	public List<M> findAll() throws GeneralServiceException{
		return service.findAll();
	}

	public M update(M model, String id) {
		M resultModel = null;

		if (model == null) {
			return service.errorModel(INVALID_JSON);
		} else if (StringUtils.isNotEmpty(id) && ObjectId.isValid(id)) {
			model.setId(new ObjectId(id));
		}
		
		try {
			resultModel = service.update(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = service.errorModel(VALIDATION_FAILED,validationException.getMessage());
		} catch (GeneralServiceException e) {
			resultModel = service.errorModel(RestError.NOT_FOUND, service.getModelClass().getSimpleName());
		}
		
		return resultModel;
	}

	public M delete(String id) {
		ObjectId mongoId = null;
		M model = null;
		if (ObjectId.isValid(id)) {
			mongoId = new ObjectId(id);
		}
		
		try {
			model = service.delete(mongoId);
		} catch (GeneralServiceException e) {
			model = service.errorModel(RestError.NOT_FOUND, service.getModelClass().getSimpleName());
		}
		return model;
	}

	public void setService(T service) {
		this.service = service;
	}
}
