package controllers;

import static dto.RestError.INVALID_JSON;
import static dto.RestError.VALIDATION_FAILED;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.VerboseJSR303ConstraintViolationException;

import com.google.inject.Inject;

import dto.BaseDTO;
import dto.RestError;
import exceptions.GeneralServiceException;
import exceptions.ModelAlreadyExistsException;
import helpers.JsonHelper;
import model.BaseModel;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.BaseService;
import utils.BlogMapperFactory;

public abstract class BaseController<T extends BaseService<M,N>, M extends BaseDTO, N extends BaseModel> extends Controller {

	@Inject
	protected JsonHelper jsonHelper;

	protected abstract T service();

	public Result create() {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getDTOClass());
		M resultModel;

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		}

		try {
			resultModel = service().create(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = service().errorModel(VALIDATION_FAILED,validationException.getMessage());
		} catch (GeneralServiceException e) {
			resultModel = service().errorModel(RestError.ALREADY_EXISTS, service().getModelClass().getSimpleName());
		}

		return jsonHelper.getResponse(resultModel);
	}

	public Result findById(String id) {
		ObjectId mongoId = null;
		M model = null;

		if (ObjectId.isValid(id)) {
			mongoId = new ObjectId(id);
		}
		
		try {
			model = service().findById(mongoId);
		} catch (GeneralServiceException e) {
			model = service().errorModel(RestError.NOT_FOUND, service().getModelClass().getSimpleName());
		}
		
		return jsonHelper.getResponse(model);
	}

	public Result findAll() throws GeneralServiceException{
		return jsonHelper.getResponses(service().findAll(), service().getDTOClass());
	}

	public Result update(String id) {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getDTOClass());
		M resultModel = null;

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		} else if (StringUtils.isNotEmpty(id) && ObjectId.isValid(id)) {
			model.setId(new ObjectId(id));
		}
		
		try {
			resultModel = service().update(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = service().errorModel(VALIDATION_FAILED,validationException.getMessage());
		} catch (GeneralServiceException e) {
			resultModel = service().errorModel(RestError.NOT_FOUND, service().getModelClass().getSimpleName());
		}
		
		return jsonHelper.getResponse(resultModel);
	}

	public Result delete(String id) {
		ObjectId mongoId = null;
		M model = null;
		if (ObjectId.isValid(id)) {
			mongoId = new ObjectId(id);
		}
		
		try {
			model = service().delete(mongoId);
		} catch (GeneralServiceException e) {
			model = service().errorModel(RestError.NOT_FOUND, service().getModelClass().getSimpleName());
		}
		return jsonHelper.getResponse(model);
	}

	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}
}
