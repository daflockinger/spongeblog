package controllers;

import org.bson.types.ObjectId;

import com.google.inject.Inject;

import dto.PaginationDTO;
import helpers.JsonHelper;
import model.BaseModel;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.BaseService;
import static dto.RestError.*;

public abstract class BaseController<T extends BaseService<M>, M extends BaseModel> extends Controller {

	@Inject
	protected JsonHelper jsonHelper;

	protected abstract T service();

	public Result create() {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getModelClass());

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		}
		return jsonHelper.getResponse(service().create(model));
	}

	public Result findById(String id) {
		ObjectId mongoId = null;
		
		if(ObjectId.isValid(id)){
			mongoId = new ObjectId(id);
		}
		return jsonHelper.getResponse(service().findById(mongoId));
	}
	
	public Result findAll(){
		return jsonHelper.getResponses(service().findAll(),service().getModelClass());
	}

	public Result update(String id) {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getModelClass());

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		}
		return jsonHelper.getResponse(service().update(model));
	}

	public Result delete(String id) {
		ObjectId mongoId = null;
		if(ObjectId.isValid(id)){
			mongoId = new ObjectId(id);
		}
		return jsonHelper.getResponse(service().delete(mongoId));
	}

	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}
}
