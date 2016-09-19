package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import dao.BlogDAO;
import dto.PaginationDTO;
import model.Blog;
import model.BlogStatus;
import play.libs.Json;
import services.BlogService;

public class BlogControllerTest extends BaseControllerTest<BlogController, BlogService, BlogDAO, Blog> {

	private Blog testBlog1;
	private Blog insertBlog;

	@Before
	public void setup() {
		routePath = "/api/v1/blogs";

		dao = new BlogDAO();
		service = new BlogService();
		service.setDao(dao);
		controller = new BlogController();
		controller.setBlogService(service);

		testBlog1 = new Blog();
		testBlog1.setName("Test Blog");
		testBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog1.setStatus(BlogStatus.ACTIVE);
		testBlog1.setUsers(ImmutableList.of("test"));

		dao.save(testBlog1);
		testId = testBlog1.getId().toHexString();

		testBlog1.setSettings(ImmutableMap.of("setting1", "value1"));
		testBlog1.setStatus(BlogStatus.MAINTENANCE);

		insertBlog = new Blog();
		insertBlog.setName("New BLog");
		insertBlog.setSettings(ImmutableMap.of("setting2", "value2", "setting3", "value3"));
		insertBlog.setStatus(BlogStatus.DISABLED);
		insertBlog.setUsers(ImmutableList.of("test"));

		PaginationDTO settings = new PaginationDTO();
		settings.setPage(0);
		settings.setLimit(1);
		settings.setFilters(ImmutableMap.of("status", BlogStatus.ACTIVE.toString()));
		settings.setSortBy("name");

		insertNode = Json.toJson(insertBlog);
		updateNode = Json.toJson(testBlog1);
		pageNode = Json.toJson(settings);

		testBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog1.setStatus(BlogStatus.ACTIVE);
	}

	@Test
	public void testCreate_withNotValid() {
		super.testCreate_withNotValid();
	}

	@Test
	public void testCreate_withValid() {
		super.testCreate_withValid();
		Blog newBlog = dao.find(dao.createQuery().filter("name", "New BLog")).asList().get(0);

		assertNotNull(newBlog);
		assertEquals("New BLog", newBlog.getName());
		assertEquals(BlogStatus.DISABLED.toString(), newBlog.getStatus().toString());
		assertTrue(newBlog.getSettings().size() == 2);
		dao.deleteByQuery(dao.createQuery().filter("name", "New BLog"));
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

		Blog updated = dao.get(testBlog1.getId());
		assertNotNull(updated);
		assertEquals("Test Blog", updated.getName());
		assertEquals(BlogStatus.MAINTENANCE.toString(), updated.getStatus().toString());
		assertTrue(updated.getSettings().size() == 1);
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
		assertTrue(response.contains("Test Blog"));
		assertTrue(response.contains(BlogStatus.ACTIVE.toString()));
	}

	@Test
	public void testFindAllInPage_withNotValid() {
		super.testFindAllInPage_withNotValid();
	}

	@Test
	public void testFindAllInPage_withValid_() {
		String response = super.testFindAllInPage_withValid();
		assertTrue(response.contains("Test Blog"));
		assertTrue(response.contains(BlogStatus.ACTIVE.toString()));
	}

	@Test
	public void testDelete_withNotValid() {
		super.testDelete_withNotValid();
	}

	@Test
	public void testDelete_withValid() {
		super.testDelete_withValid();
		assertNull(dao.get(testBlog1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteById(testBlog1.getId());
	}
}
