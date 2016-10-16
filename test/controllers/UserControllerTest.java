package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import dao.UserDAO;
import dto.UserDTO;
import model.User;
import model.UserStatus;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.test.WithApplication;
import services.UserService;
import utils.BlogMapperFactory;

public class UserControllerTest extends WithApplication {
	protected UserDAO dao;
	
	protected JsonNode insertNode;
	protected JsonNode updateNode;
	protected String params;
	
	protected String routePath;
	protected String testId;
	private User testUser1;
	private User insertUser;

	@Before
	public void setup() {
		routePath = "/api/v1/users";

		dao = new UserDAO();

		testUser1 = new User();
		testUser1.setLogin("Test User");
		testUser1.setPassword("origpassword");
		testUser1.setUserStatus(UserStatus.ADMIN);

		dao.save(testUser1);
		testId = testUser1.getId().toHexString();

		testUser1.setPassword("updatedpass");
		testUser1.setUserStatus(UserStatus.AUTHOR);

		insertUser = new User();
		insertUser.setLogin("New User");
		insertUser.setUserStatus(UserStatus.SUSPENDED);
		insertUser.setPassword("specialpass");

		insertNode = Json.toJson(insertUser);
		updateNode = Json.toJson(testUser1);

		testUser1.setUserStatus(UserStatus.ADMIN);
	}

	@Test
	public void testCreate_withNotValid() {
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyText("invalid")
	            .uri(routePath);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void testCreate_withValid() {
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(insertNode)
	            .uri(routePath);
	    Result result = route(request);	  
	    
	    assertTrue(result.status() == HttpStatus.SC_CREATED);
		User newUser = dao.find(dao.createQuery().filter("login", "New User")).asList().get(0);

		assertNotNull(newUser);
		assertEquals("New User", newUser.getLogin());
		assertEquals(UserStatus.SUSPENDED.toString(), newUser.getUserStatus().toString());
		assertEquals(newUser.getPassword(), "specialpass");
		dao.deleteByQuery(dao.createQuery().filter("login", "New User"));
	}

	@Test
	public void testCreate_withAlreadyExisting() {
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(updateNode)
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_CONFLICT);
	}

	@Test
	public void testUpdate_withNotValid() {
		RequestBuilder request = new RequestBuilder().method("PUT")
				.bodyText("invalid")
	            .uri(routePath + "/12345678");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}
	
	protected Result testUpdate_withValidationError(JsonNode validationFail){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(validationFail)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    return result;
	}
	
	@Test
	public void testUpdate_withValidationGood_shouldWork(){
		User invalidUser = new User();
		invalidUser.setLogin("abc");
		invalidUser.setPassword("ckk");
		invalidUser.setEmail("a@b.cc");
		invalidUser.setUserStatus(UserStatus.ADMIN);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidUser));

		assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	@Test
	public void testUpdate_withLoginValidationFail_shouldThrowException(){
		User invalidUser = new User();
		invalidUser.setLogin("ab");
		invalidUser.setPassword("ckkkd");
		invalidUser.setEmail("a@b.cc");
		invalidUser.setUserStatus(UserStatus.ADMIN);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidUser));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("login"));
	}
	
	@Test
	public void testUpdate_withPasswordValidationFail_shouldThrowException(){
		User invalidUser = new User();
		invalidUser.setLogin("akkb");
		invalidUser.setPassword("cd");
		invalidUser.setEmail("a@b.cc");
		invalidUser.setUserStatus(UserStatus.ADMIN);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidUser));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("password"));
	}
	
	@Test
	public void testUpdate_withEmailValidationFail_shouldThrowException(){
		User invalidUser = new User();
		invalidUser.setLogin("akkb");
		invalidUser.setPassword("cjd");
		invalidUser.setEmail("a(at)b");
		invalidUser.setUserStatus(UserStatus.ADMIN);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidUser));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("email"));
	}
	
	@Test
	public void testUpdate_withStatusValidationFail_shouldThrowException(){
		User invalidUser = new User();
		invalidUser.setLogin("akkb");
		invalidUser.setPassword("cjd");
		invalidUser.setEmail("a@b.cc");
		invalidUser.setUserStatus(null);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidUser));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("userStatus"));
	}

	@Test
	public void testUpdate_withValid() {
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(updateNode)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);

		User updated = dao.get(testUser1.getId());
		assertNotNull(updated);
		assertEquals("Test User", updated.getLogin());
		assertEquals(UserStatus.AUTHOR.toString(), updated.getUserStatus().toString());
		assertEquals(updated.getPassword(), "updatedpass");
	}

	@Test
	public void testUpdate_withNotExisting() {
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(insertNode)
	            .uri(routePath + "/1234567890123");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void testFindById_withNotValid() {
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath  + "/invalid");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void testFindById_withValid_() {
		String response = testFindById_withValid();
		assertTrue(response.contains("Test User"));
		assertTrue(response.contains(UserStatus.ADMIN.toString()));
		assertTrue(response.contains("origpassword"));
	}

	protected String testFindById_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}
	
	@Test
	public void testFindAll_shouldReturnOne(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	}

	@Test
	public void testDelete_withNotValid() {
		RequestBuilder request = new RequestBuilder().method("DELETE")
	            .uri(routePath + "/invalid");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void testDelete_withValid() {
		RequestBuilder request = new RequestBuilder().method("DELETE")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
		assertNull(dao.get(testUser1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteById(testUser1.getId());
	}

}
