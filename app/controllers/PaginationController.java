package controllers;

import com.google.inject.Inject;

import dto.BaseDTO;
import dto.PaginationDTO;
import exceptions.GeneralServiceException;
import helpers.PaginationMapper;
import model.BaseModel;
import play.mvc.Result;
import services.PaginationService;

public abstract class PaginationController<T extends PaginationService<M,N>,M extends BaseDTO, N extends BaseModel>
		extends BaseController<T,M,N> {

	@Inject
	private PaginationMapper mapper;

	private Result findAllInPage(PaginationDTO settings) {
		return jsonHelper.getResponses(service().findAllInPage(settings), service().getDTOClass());
	}

	@Override
	public Result findAll() throws GeneralServiceException{
		PaginationDTO pagination = mapper.mapParamsToPagination(request().queryString());

		if (pagination != null) {
			return findAllInPage(pagination);
		} else {
			return super.findAll();
		}
	}
}
