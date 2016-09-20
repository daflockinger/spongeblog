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
import model.Blog;
import model.BlogStatus;
import play.test.WithApplication;

public class BlogServiceTest extends WithApplication {

	private BlogService service;
	private BlogDAO dao;

	private Blog testBlog1;

	@Before
	public void setup() {
		dao = new BlogDAO();
		service = new BlogService();
		service.setDao(dao);

		testBlog1 = new Blog();
		testBlog1.setName("Test Blog");
		testBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog1.setBlogStatus(BlogStatus.ACTIVE);
		testBlog1.setUsers(ImmutableList.of("test"));

		dao.save(testBlog1);
	}

	@Test
	public void testCreate_withNullBlog_shouldReturnFail() {
		Blog result = service.create(null);
		assertTrue(HttpStatus.SC_CONFLICT == result.getStatus());
	}

	@Test
	public void testCreate_withValidBlog_shouldInsert() {
		Blog testBlog = new Blog();
		testBlog.setName("Test Blog2");
		testBlog.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog.setBlogStatus(BlogStatus.DISABLED);
		testBlog.setUsers(ImmutableList.of("test2"));

		Blog result = service.create(testBlog);
		assertTrue(HttpStatus.SC_CREATED == result.getStatus());

		Blog foundBlog = dao.findOne(dao.createQuery().filter("name", "Test Blog2"));
		assertNotNull(foundBlog);
		assertEquals("Test Blog2", foundBlog.getName());
		assertEquals(BlogStatus.DISABLED, foundBlog.getBlogStatus());
		assertEquals("test2", foundBlog.getUsers().get(0));

		assertTrue(foundBlog.getSettings().size() == 3);
	}

	@Test
	public void testFindById_withNullId_shouldReturnNotFound() {
		Blog result = service.findById(null);
		assertTrue(HttpStatus.SC_NOT_FOUND == result.getStatus());
	}

	@Test
	public void testFindById_withNotExistingId_shouldReturnNotFound() {
		Blog result = service.findById(new ObjectId());
		assertTrue(HttpStatus.SC_NOT_FOUND == result.getStatus());
	}

	@Test
	public void testFindById_withExistingId_shouldReturnUser() {
		Blog result = service.findById(testBlog1.getId());
		assertTrue(HttpStatus.SC_OK == result.getStatus());
		assertEquals("Test Blog", result.getName());
	}

	@Test
	public void testUpdate_withNullBlog_shouldReturnNotFound() {
		Blog result = service.update(null);
		assertTrue(HttpStatus.SC_NOT_FOUND == result.getStatus());
	}
	
	@Test
	public void testUpdate_withExistingBlog_shouldReturnOk() {
		testBlog1.setName("New Test Blog");
		testBlog1.setBlogStatus(BlogStatus.MAINTENANCE);
		
		Blog result = service.update(testBlog1);
		assertTrue(HttpStatus.SC_OK == result.getStatus());
		
		Blog updatedBlog = dao.get(testBlog1.getId());
		
		assertEquals("New Test Blog", updatedBlog.getName());
		assertEquals(BlogStatus.MAINTENANCE, updatedBlog.getBlogStatus());
		assertTrue(updatedBlog.getSettings().size() == 3);
	}
	
	@Test
	public void testDelete_withNullBlog_shouldReturnNotFound() {
		Blog result = service.delete(null);
		assertTrue(HttpStatus.SC_NOT_FOUND == result.getStatus());
	}
	
	@Test
	public void testDelete_withValidBlog_shouldReturnOk() {
		Blog result = service.delete(testBlog1.getId());
		assertTrue(HttpStatus.SC_OK ==  result.getStatus());
		
		assertNull(dao.get(testBlog1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteByQuery(dao.createQuery().filter("name", "Test Blog"));
		dao.deleteByQuery(dao.createQuery().filter("name", "Test Blog2"));
	}

}
