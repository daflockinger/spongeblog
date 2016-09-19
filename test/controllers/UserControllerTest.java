package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import dao.UserDAO;
import dto.PaginationDTO;
import model.User;
import model.UserStatus;
import play.libs.Json;
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
		testUser1.setStatus(UserStatus.ADMIN);

		dao.save(testUser1);
		testId = testUser1.getId().toHexString();

		testUser1.setPassword("updatedpass");
		testUser1.setStatus(UserStatus.AUTHOR);

		insertUser = new User();
		insertUser.setLogin("New User");
		insertUser.setStatus(UserStatus.SUSPENDED);
		insertUser.setPassword("specialpass");

		PaginationDTO settings = new PaginationDTO();
		settings.setPage(0);
		settings.setLimit(1);
		settings.setFilters(ImmutableMap.of("status", UserStatus.ADMIN.toString()));
		settings.setSortBy("login");

		insertNode = Json.toJson(insertUser);
		updateNode = Json.toJson(testUser1);
		pageNode = Json.toJson(settings);

		testUser1.setStatus(UserStatus.ADMIN);
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
		assertEquals(UserStatus.SUSPENDED.toString(), newUser.getStatus().toString());
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
	public void testUpdate_withValid() {
		super.testUpdate_withValid();

		User updated = dao.get(testUser1.getId());
		assertNotNull(updated);
		assertEquals("Test User", updated.getLogin());
		assertEquals(UserStatus.AUTHOR.toString(), updated.getStatus().toString());
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
	public void testFindAllInPage_withNotValid() {
		super.testFindAllInPage_withNotValid();
	}

	@Test
	public void testFindAllInPage_withValid_() {
		String response = super.testFindAllInPage_withValid();
		assertTrue(response.contains("Test User"));
		assertTrue(response.contains(UserStatus.ADMIN.toString()));
		assertTrue(response.contains("origpassword"));
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
