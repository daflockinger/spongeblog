package controllers;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.route;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.UserDAO;
import dto.LoginCredentials;
import model.User;
import model.UserStatus;
import play.libs.Json;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

public class LoginControllerTest extends WithApplication{
	private UserDAO dao;
	
	private User testUser1;
	
	@Before
	public void setup(){
		dao = new UserDAO();
		
		testUser1 = new User();
		testUser1.setLogin("flo");
		testUser1.setPassword("flo123!");
		testUser1.setStatus(UserStatus.ADMIN);
		dao.save(testUser1);
	}
	
	@Test
	public void testLogin_withValidCredentials_shouldReturnGood(){
		LoginCredentials invalidcreds = new LoginCredentials();
		invalidcreds.setUser("flo");
		invalidcreds.setPassword("flo123!");
		
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(Json.toJson(invalidcreds))
	            .uri("/api/v1/login");
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    assertTrue(Helpers.contentAsString(result).contains(UserStatus.ADMIN.toString()));
	}
	
	@Test
	public void testLogin_withInValidCredentials_ShouldReturnNotAuthorized(){
		LoginCredentials invalidcreds = new LoginCredentials();
		invalidcreds.setUser("hack");
		invalidcreds.setPassword("meeee");
		
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(Json.toJson(invalidcreds))
	            .uri("/api/v1/login");
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_FORBIDDEN);
	}
	
	
	@After
	public void teardown(){
		dao.deleteById(testUser1.getId());
	}
	
}
