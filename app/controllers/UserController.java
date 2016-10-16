package controllers;

import com.google.inject.Inject;

import dto.PostDTO;
import dto.UserDTO;
import exceptions.GeneralServiceException;
import helpers.JsonHelper;
import model.User;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.UserService;

public class UserController extends Controller{

	@Inject
	private SimpleController<UserDTO, User, UserService> simple;

	@Inject
	protected JsonHelper jsonHelper;

	public Result create() {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.create(jsonHelper.extractModel(body, UserDTO.class)), UserDTO.class);
	}

	public Result findById(String id) {
		return jsonHelper.getResponse(simple.findById(id), UserDTO.class);
	}

	public Result findAll() throws GeneralServiceException {
		return jsonHelper.getResponses(simple.findAll(), UserDTO.class);
	}

	public Result update(String id) {
		RequestBody body = request().body();
		return jsonHelper.getResponse(simple.update(jsonHelper.extractModel(body, UserDTO.class), id), UserDTO.class);
	}

	public Result delete(String id) {
		return jsonHelper.getResponse(simple.delete(id), UserDTO.class);
	}

	public void setSimple(SimpleController<UserDTO, User, UserService> simple) {
		this.simple = simple;
	}
}
