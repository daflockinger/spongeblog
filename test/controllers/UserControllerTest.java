package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.UserDAO;
import model.User;
import model.UserStatus;
import play.libs.Json;
import play.mvc.Result;
import services.UserService;

public class UserControllerTest extends BaseControllerTest<UserController, UserService, UserDAO, User> {

	private User testUser1;
	private User insertUser;

	@Before
	public void setup() {
		routePath = "/api/v1/users";

		dao = new UserDAO();
		service = new UserService();
		service.setDao(dao);
		controller = new UserController();
		controller.setService(service);

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
		super.testCreate_withNotValid();
	}

	@Test
	public void testCreate_withValid() {
		super.testCreate_withValid();
		User newUser = dao.find(dao.createQuery().filter("login", "New User")).asList().get(0);

		assertNotNull(newUser);
		assertEquals("New User", newUser.getLogin());
		assertEquals(UserStatus.SUSPENDED.toString(), newUser.getUserStatus().toString());
		assertEquals(newUser.getPassword(), "specialpass");
		dao.deleteByQuery(dao.createQuery().filter("login", "New User"));
	}

	@Test
	public void testCreate_withAlreadyExisting() {
		super.testCreate_withAlreadyExisting();
	}

	@Test
	public void testUpdate_withNotValid() {
		super.testUpdate_withNotValid();
	}
	
	@Test
	public void testUpdate_withValidationGood_shouldWork(){
		User invalidUser = new User();
		invalidUser.setLogin("abc");
		invalidUser.setPassword("ckk");
		invalidUser.setEmail("a@b.cc");
		invalidUser.setUserStatus(UserStatus.ADMIN);
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidUser));

		assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	@Test
	public void testUpdate_withLoginValidationFail_shouldThrowException(){
		User invalidUser = new User();
		invalidUser.setLogin("ab");
		invalidUser.setPassword("ckkkd");
		invalidUser.setEmail("a@b.cc");
		invalidUser.setUserStatus(UserStatus.ADMIN);
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidUser));

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
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidUser));

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
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidUser));

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
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidUser));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("userStatus"));
	}

	@Test
	public void testUpdate_withValid() {
		super.testUpdate_withValid();

		User updated = dao.get(testUser1.getId());
		assertNotNull(updated);
		assertEquals("Test User", updated.getLogin());
		assertEquals(UserStatus.AUTHOR.toString(), updated.getUserStatus().toString());
		assertEquals(updated.getPassword(), "updatedpass");
	}

	@Test
	public void testUpdate_withNotExisting() {
		super.testUpdate_withNotExisting();
	}

	@Test
	public void testFindById_withNotValid() {
		super.testFindById_withNotValid();
	}

	@Test
	public void testFindById_withValid_() {
		String response = super.testFindById_withValid();
		assertTrue(response.contains("Test User"));
		assertTrue(response.contains(UserStatus.ADMIN.toString()));
		assertTrue(response.contains("origpassword"));
	}

	@Test
	public void testFindAll_shouldReturnOne(){
		super.testFindAll_ShouldReturnOne();
	}

	@Test
	public void testDelete_withNotValid() {
		super.testDelete_withNotValid();
	}

	@Test
	public void testDelete_withValid() {
		super.testDelete_withValid();
		assertNull(dao.get(testUser1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteById(testUser1.getId());
	}

}
