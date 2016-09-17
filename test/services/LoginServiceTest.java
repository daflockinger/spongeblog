package services;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.UserDAO;
import dto.LoginCredentials;
import dto.LoginResultDTO;
import dto.OperationResult;
import model.User;
import model.UserStatus;
import play.test.WithApplication;

public class LoginServiceTest extends WithApplication{
	
	private LoginService service;
	private UserDAO dao;
	
	@Before
	public void setup() {
		dao = new UserDAO();
		service = new LoginService();
		service.setDao(dao);
		
		User testUser = new User();
		testUser.setLogin("test");
		testUser.setEmail("test@testinger.cc");
		testUser.setNickname("te");
		testUser.setPassword("1234");
		testUser.setStatus(UserStatus.ADMIN);
		dao.save(testUser);
	}
	
	@Test
	public void testLogin_withInvalidCredentials_shouldReturnForbidden() {
		LoginCredentials testCreds = new LoginCredentials();
		testCreds.setUser("not existing");
		testCreds.setPassword("blub");
		
		OperationResult<LoginResultDTO> result  = service.login(testCreds);
		
		assertEquals(HttpStatus.SC_FORBIDDEN, result.getStatus());
	}
	
	@Test
	public void testLogin_withGoodCredentials_shouldReturnOk() {
		LoginCredentials testCreds = new LoginCredentials();
		testCreds.setUser("test");
		testCreds.setPassword("1234");
		
		OperationResult<LoginResultDTO> result  = service.login(testCreds);
		
		assertEquals(HttpStatus.SC_OK, result.getStatus());
		assertEquals(UserStatus.ADMIN,result.getEntity().getStatus());
	}

	@After
	public void teardown() {
		dao.deleteByQuery(dao.createQuery().filter("login", "test"));
	}
}
