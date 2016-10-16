package controllers;

import com.google.inject.Inject;

import dto.KeywordDTO;
import exceptions.GeneralServiceException;
import helpers.JsonHelper;
import model.Keyword;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.KeywordService;

public class KeywordController extends Controller{

	@Inject
	private SimpleController<KeywordDTO, Keyword, KeywordService> simple;

	@Inject
	protected JsonHelper jsonHelper;

	public Result create() {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.create(jsonHelper.extractModel(body, KeywordDTO.class)));
	}

	public Result findById(String id) {
		return jsonHelper.getResponse(simple.findById(id));
	}

	public Result findAll() throws GeneralServiceException {
		return jsonHelper.getResponses(simple.findAll(), KeywordDTO.class);
	}

	public Result update(String id) {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.update(jsonHelper.extractModel(body, KeywordDTO.class), id));
	}

	public Result delete(String id) {
		return jsonHelper.getResponse(simple.delete(id));
	}

	public void setSimple(SimpleController<KeywordDTO, Keyword, KeywordService> simple) {
		this.simple = simple;
	}
}
