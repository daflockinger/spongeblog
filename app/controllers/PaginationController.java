package controllers;

import com.google.inject.Inject;

import dto.PaginationDTO;
import helpers.PaginationMapper;
import model.BaseModel;
import play.mvc.Result;
import services.PaginationService;

public abstract class PaginationController<T extends PaginationService<M>,M extends BaseModel>
		extends BaseController<T, M> {

	@Inject
	private PaginationMapper mapper;

	private Result findAllInPage(PaginationDTO settings) {
		return jsonHelper.getResponses(service().findAllInPage(settings), service().getModelClass());
	}

	@Override
	public Result findAll() {
		PaginationDTO pagination = mapper.mapParamsToPagination(request().queryString());

		if (pagination != null) {
			return findAllInPage(pagination);
		} else {
			return super.findAll();
		}
	}
}
