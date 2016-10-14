package controllers;

import static dto.RestError.INVALID_JSON;
import static dto.RestError.VALIDATION_FAILED;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.VerboseJSR303ConstraintViolationException;

import com.google.inject.Inject;

import helpers.JsonHelper;
import model.BaseModel;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.BaseService;

public abstract class BaseController<T extends BaseService<M>, M extends BaseModel> extends Controller {

	@Inject
	protected JsonHelper jsonHelper;

	protected abstract T service();

	public Result create() {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getModelClass());
		M resultModel;

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		}

		try {
			resultModel = service().create(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = service().errorModel(VALIDATION_FAILED,validationException.getMessage());
		}

		return jsonHelper.getResponse(resultModel);
	}

	public Result findById(String id) {
		ObjectId mongoId = null;

		if (ObjectId.isValid(id)) {
			mongoId = new ObjectId(id);
		}
		return jsonHelper.getResponse(service().findById(mongoId));
	}

	public Result findAll() {
		return jsonHelper.getResponses(service().findAll(), service().getModelClass());
	}

	public Result update(String id) {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getModelClass());
		M resultModel;

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		} else if (StringUtils.isNotEmpty(id) && ObjectId.isValid(id)) {
			model.setId(new ObjectId(id));
		}
		
		try {
			resultModel = service().update(model);
		} catch (VerboseJSR303ConstraintViolationException validationException) {
			resultModel = service().errorModel(VALIDATION_FAILED,validationException.getMessage());
		}
		
		return jsonHelper.getResponse(resultModel);
	}

	public Result delete(String id) {
		ObjectId mongoId = null;
		if (ObjectId.isValid(id)) {
			mongoId = new ObjectId(id);
		}
		return jsonHelper.getResponse(service().delete(mongoId));
	}

	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}
}
