package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import dao.BlogDAO;
import dao.UserDAO;
import dto.OperationResult;
import model.Blog;
import model.BlogStatus;
import model.User;
import model.UserStatus;
import play.test.WithApplication;

public class BlogServiceTest extends WithApplication {

	private BlogService service;
	private BlogDAO dao;
	private UserDAO userDao;

	private Blog testBlog1;

	@Before
	public void setup() {
		dao = new BlogDAO();
		userDao = new UserDAO();
		service = new BlogService();
		service.setDao(dao);

		testBlog1 = new Blog();
		testBlog1.setName("Test Blog");
		testBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog1.setStatus(BlogStatus.ACTIVE);

		User testUser = new User();
		testUser.setLogin("test");
		testUser.setEmail("test@testinger.cc");
		testUser.setNickname("te");
		testUser.setPassword("1234");
		testUser.setStatus(UserStatus.ADMIN);
		userDao.save(testUser);

		testBlog1.setUsers(ImmutableList.of(testUser));

		dao.save(testBlog1);
	}

	@Test
	public void testCreate_withNullBlog_shouldReturnFail() {
		OperationResult<Blog> result = service.create(null);
		assertEquals(HttpStatus.SC_CONFLICT, result.getStatus());
	}

	@Test
	public void testCreate_withValidBlog_shouldInsert() {
		Blog testBlog = new Blog();
		testBlog.setName("Test Blog2");
		testBlog.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog.setStatus(BlogStatus.DISABLED);

		User testUser = new User();
		testUser.setLogin("test2");
		testUser.setEmail("test2@testinger.cc");
		testUser.setNickname("te2");
		testUser.setPassword("4321");
		testUser.setStatus(UserStatus.AUTHOR);
		userDao.save(testUser);

		testBlog.setUsers(ImmutableList.of(testUser));

		OperationResult<Blog> result = service.create(testBlog);
		assertEquals(HttpStatus.SC_CREATED, result.getStatus());

		Blog foundBlog = dao.findOne(dao.createQuery().filter("name", "Test Blog2"));
		assertNotNull(foundBlog);
		assertEquals("Test Blog2", foundBlog.getName());
		assertEquals(BlogStatus.DISABLED, foundBlog.getStatus());
		assertEquals(testUser.getLogin(), foundBlog.getUsers().get(0).getLogin());

		assertTrue(foundBlog.getSettings().size() == 3);
	}

	@Test
	public void testFindById_withNullId_shouldReturnNotFound() {
		OperationResult<Blog> result = service.findById(null);
		assertEquals(HttpStatus.SC_NOT_FOUND, result.getStatus());
	}

	@Test
	public void testFindById_withNotExistingId_shouldReturnNotFound() {
		OperationResult<Blog> result = service.findById(new ObjectId());
		assertEquals(HttpStatus.SC_NOT_FOUND, result.getStatus());
	}

	@Test
	public void testFindById_withExistingId_shouldReturnUser() {
		OperationResult<Blog> result = service.findById(testBlog1.getId());
		assertEquals(HttpStatus.SC_OK, result.getStatus());
		assertEquals("Test Blog", result.getEntity().getName());
	}

	@Test
	public void testUpdate_withNullBlog_shouldReturnNotFound() {
		OperationResult<Blog> result = service.update(null);
		assertEquals(HttpStatus.SC_NOT_FOUND, result.getStatus());
	}
	
	@Test
	public void testUpdate_withExistingBlog_shouldReturnOk() {
		testBlog1.setName("New Test Blog");
		testBlog1.setStatus(BlogStatus.MAINTENANCE);
		
		OperationResult<Blog> result = service.update(testBlog1);
		assertEquals(HttpStatus.SC_NO_CONTENT, result.getStatus());
		
		Blog updatedBlog = dao.get(testBlog1.getId());
		
		assertEquals("New Test Blog", updatedBlog.getName());
		assertEquals(BlogStatus.MAINTENANCE, updatedBlog.getStatus());
		assertTrue(updatedBlog.getSettings().size() == 3);
	}
	
	@Test
	public void testDelete_withNullBlog_shouldReturnNotFound() {
		OperationResult<Blog> result = service.delete(null);
		assertEquals(HttpStatus.SC_NOT_FOUND, result.getStatus());
	}
	
	@Test
	public void testDelete_withValidBlog_shouldReturnOk() {
		OperationResult<Blog> result = service.delete(testBlog1.getId());
		assertEquals(HttpStatus.SC_NO_CONTENT, result.getStatus());
		
		assertNull(dao.get(testBlog1.getId()));
	}

	@After
	public void teardown() {
		userDao.deleteByQuery(userDao.createQuery().filter("login", "test"));
		userDao.deleteByQuery(userDao.createQuery().filter("login", "test2"));
		dao.deleteByQuery(dao.createQuery().filter("name", "Test Blog"));
		dao.deleteByQuery(dao.createQuery().filter("name", "Test Blog2"));
	}

}
