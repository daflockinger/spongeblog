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

public abstract class BaseController<T extends BaseService<M>, M extends BaseModel> extends Controller {

	@Inject
	private JsonHelper jsonHelper;

	protected abstract T service();

	public Result create() {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getModelClass());

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(body);
		}
		return jsonHelper.getStatus(service().create(model));
	}

	public Result findById(String id) {
		return jsonHelper.getStatus(service().findById(new ObjectId(id)));
	}

	public Result update() {
		RequestBody body = request().body();
		M model = jsonHelper.extractModel(body, service().getModelClass());

		if (model == null) {
			return jsonHelper.getInvalidJsonMessage(body);
		}
		return jsonHelper.getStatus(service().update(model));
	}

	public Result delete(String id) {
		return jsonHelper.getStatus(service().delete(new ObjectId(id)));
	}

	public Result findAllInPage() {
		RequestBody body = request().body();
		PaginationDTO settings = jsonHelper.extractModel(body, PaginationDTO.class);

		if (settings == null) {
			return jsonHelper.getInvalidJsonMessage(body);
		}
		return jsonHelper.getStatus(service().findAllInPage(settings));
	}

	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}
}
