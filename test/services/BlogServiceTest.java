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
import dto.BlogDTO;
import exceptions.BlogOnlyOneInstanceAllowedException;
import exceptions.GeneralServiceException;
import exceptions.ModelAlreadyExistsException;
import exceptions.ModelNotFoundException;
import model.Blog;
import model.BlogStatus;
import play.test.WithApplication;
import utils.BlogMapperFactory;

public class BlogServiceTest extends WithApplication {

	private BlogService service;
	private BlogDAO dao;

	private BlogDTO testBlog1;

	@Before
	public void setup() {
		dao = new BlogDAO();
		service = new BlogService();
		service.setDao(dao);
		service.setMapperFactory(new BlogMapperFactory());

		testBlog1 = new BlogDTO();
		testBlog1.setName("Test Blog");
		testBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog1.setBlogStatus(BlogStatus.ACTIVE.toString());
		testBlog1.setUsers(ImmutableList.of("test"));
		
		Blog savetestBlog1 = new Blog();
		savetestBlog1.setName("Test Blog");
		savetestBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		savetestBlog1.setBlogStatus(BlogStatus.ACTIVE);
		savetestBlog1.setUsers(ImmutableList.of("test"));

		dao.save(savetestBlog1);
		testBlog1.setId(savetestBlog1.getId());
	}

	@Test(expected=ModelAlreadyExistsException.class)
	public void testCreate_withNullBlog_shouldReturnFail() throws GeneralServiceException{
		service.create(null);
	}

	@Test
	public void testCreate_withValidBlog_shouldInsert() throws BlogOnlyOneInstanceAllowedException, ModelAlreadyExistsException {
		dao.deleteByQuery(dao.createQuery().filter("name", "Test Blog"));
		
		BlogDTO testBlog = new BlogDTO();
		testBlog.setName("Test Blog2");
		testBlog.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog.setBlogStatus(BlogStatus.DISABLED.toString());
		testBlog.setUsers(ImmutableList.of("test2"));

		BlogDTO result = service.create(testBlog);
		assertTrue(HttpStatus.SC_CREATED == result.getStatus());

		Blog foundBlog = dao.findOne(dao.createQuery().filter("name", "Test Blog2"));
		assertNotNull(foundBlog);
		assertEquals("Test Blog2", foundBlog.getName());
		assertEquals(BlogStatus.DISABLED, foundBlog.getBlogStatus());
		assertEquals("test2", foundBlog.getUsers().get(0));

		assertTrue(foundBlog.getSettings().size() == 3);
	}
	
	@Test(expected=BlogOnlyOneInstanceAllowedException.class)
	public void testCreate_withValidBlogButOneExistsAlready_shouldThrowOnlyOneAllowedException() throws BlogOnlyOneInstanceAllowedException, ModelAlreadyExistsException {
		BlogDTO testBlog = new BlogDTO();
		testBlog.setName("Test Blog2");
		testBlog.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog.setBlogStatus(BlogStatus.DISABLED.toString());
		testBlog.setUsers(ImmutableList.of("test2"));

		BlogDTO result = service.create(testBlog);
		assertTrue(HttpStatus.SC_CREATED == result.getStatus());

		Blog foundBlog = dao.findOne(dao.createQuery().filter("name", "Test Blog2"));
		assertNotNull(foundBlog);
		assertEquals("Test Blog2", foundBlog.getName());
		assertEquals(BlogStatus.DISABLED, foundBlog.getBlogStatus());
		assertEquals("test2", foundBlog.getUsers().get(0));

		assertTrue(foundBlog.getSettings().size() == 3);
	}

	@Test(expected=ModelNotFoundException.class)
	public void testFindById_withNullId_shouldReturnNotFound() throws ModelNotFoundException {
		service.findById(null);
	}

	@Test(expected=ModelNotFoundException.class)
	public void testFindById_withNotExistingId_shouldReturnNotFound() throws ModelNotFoundException {
		service.findById(new ObjectId());
	}

	@Test
	public void testFindById_withExistingId_shouldReturnUser() throws ModelNotFoundException {
		BlogDTO result = service.findById(testBlog1.getId());
		assertTrue(HttpStatus.SC_OK == result.getStatus());
		assertEquals("Test Blog", result.getName());
	}

	@Test(expected=ModelNotFoundException.class)
	public void testUpdate_withNullBlog_shouldReturnNotFound() throws ModelNotFoundException {
		service.update(null);
	}
	
	@Test
	public void testUpdate_withExistingBlog_shouldReturnOk() throws ModelNotFoundException {
		testBlog1.setName("New Test Blog");
		testBlog1.setBlogStatus(BlogStatus.MAINTENANCE.toString());
		
		BlogDTO result = service.update(testBlog1);
		assertTrue(HttpStatus.SC_OK == result.getStatus());
		
		Blog updatedBlog = dao.get(testBlog1.getId());
		
		assertEquals("New Test Blog", updatedBlog.getName());
		assertEquals(BlogStatus.MAINTENANCE, updatedBlog.getBlogStatus());
		assertTrue(updatedBlog.getSettings().size() == 3);
	}
	
	@Test(expected=ModelNotFoundException.class)
	public void testDelete_withNullBlog_shouldReturnNotFound() throws ModelNotFoundException {
		service.delete(null);
	}
	
	@Test
	public void testDelete_withValidBlog_shouldReturnOk() throws ModelNotFoundException {
		BlogDTO result = service.delete(testBlog1.getId());
		assertTrue(HttpStatus.SC_OK ==  result.getStatus());
		
		assertNull(dao.get(testBlog1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteByQuery(dao.createQuery().filter("name", "Test Blog"));
		dao.deleteByQuery(dao.createQuery().filter("name", "Test Blog2"));
		dao.deleteByQuery(dao.createQuery().filter("name", "New Test Blog"));
	}

}
