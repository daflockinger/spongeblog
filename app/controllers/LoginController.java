package controllers;

import com.google.inject.Inject;

import dto.LoginCredentials;
import helpers.JsonHelper;
import play.mvc.Result;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import services.LoginService;

public class LoginController extends Controller{
	
	@Inject
	private JsonHelper jsonHelper;
	@Inject
	private LoginService service;

	
	public Result login(){
		RequestBody body = request().body();
		LoginCredentials credentials = jsonHelper.extractModel(body, LoginCredentials.class);

		if (credentials == null) {
			return jsonHelper.getInvalidJsonMessage(body);
		}
		
		return jsonHelper.getStatus(service.login(credentials));
	}

	
	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}
	
	public void setService(LoginService service) {
		this.service = service;
	}
}
