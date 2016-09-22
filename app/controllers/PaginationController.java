package controllers;

import static dto.RestError.INVALID_JSON;

import dto.PaginationDTO;
import model.BaseModel;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.PaginationService;

public abstract class PaginationController<T extends PaginationService<M>, M extends BaseModel> extends BaseController<T, M> {
	public Result findAllInPage() {
		RequestBody body = request().body();
		PaginationDTO settings = jsonHelper.extractModel(body, PaginationDTO.class);

		if (settings == null) {
			return jsonHelper.getInvalidJsonMessage(service().errorModel(INVALID_JSON));
		}
		return jsonHelper.getResponses(service().findAllInPage(settings));
	}
}
