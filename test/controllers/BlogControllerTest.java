package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import dao.BlogDAO;
import dto.BlogDTO;
import model.Blog;
import model.BlogStatus;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.test.WithApplication;
import services.BlogService;
import utils.BlogMapperFactory;

public class BlogControllerTest extends WithApplication{//extends BaseControllerTest<BlogController, BlogService, BlogDAO, BlogDTO,Blog> {

	private Blog testBlog1;
	private Blog insertBlog;
	
	protected BlogController controller;
	protected BlogService service;
	protected BlogDAO dao;
	
	protected JsonNode insertNode;
	protected JsonNode updateNode;
	protected String params;
	
	protected String routePath;
	protected String testId;

	@Before
	public void setup() {
		routePath = "/api/v1/blogs";

		dao = new BlogDAO();
		service = new BlogService();
		service.setDao(dao);
		service.setMapperFactory(new BlogMapperFactory());
		controller = new BlogController();
		controller.setBlogService(service);
		SimpleController<BlogDTO,Blog,BlogService> simple = new SimpleController<>();
		simple.setService(service);
		controller.setSimple(simple);

		testBlog1 = new Blog();
		testBlog1.setName("Test Blog");
		testBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog1.setBlogStatus(BlogStatus.ACTIVE);
		testBlog1.setUsers(ImmutableList.of("test"));

		dao.save(testBlog1);
		testId = testBlog1.getId().toHexString();

		testBlog1.setSettings(ImmutableMap.of("setting1", "value1"));
		testBlog1.setBlogStatus(BlogStatus.MAINTENANCE);

		insertBlog = new Blog();
		insertBlog.setName("New BLog");
		insertBlog.setSettings(ImmutableMap.of("setting2", "value2", "setting3", "value3"));
		insertBlog.setBlogStatus(BlogStatus.DISABLED);
		insertBlog.setUsers(ImmutableList.of("test"));

		insertNode = Json.toJson(insertBlog);
		updateNode = Json.toJson(testBlog1);

		testBlog1.setSettings(ImmutableMap.of("setting1", "value1", "setting2", "value2", "setting3", "value3"));
		testBlog1.setBlogStatus(BlogStatus.ACTIVE);
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
		dao.deleteById(testBlog1.getId());
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(insertNode)
	            .uri(routePath);
	    Result result = route(request);	  
	    
	    assertTrue(result.status() == HttpStatus.SC_CREATED);
		Blog newBlog = dao.find(dao.createQuery().filter("name", "New BLog")).asList().get(0);

		assertNotNull(newBlog);
		assertEquals("New BLog", newBlog.getName());
		assertEquals(BlogStatus.DISABLED.toString(), newBlog.getBlogStatus().toString());
		assertTrue(newBlog.getSettings().size() == 2);
		dao.deleteByQuery(dao.createQuery().filter("name", "New BLog"));
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
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyText("invalid")
	            .uri(routePath);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void testUpdate_withValidationGood_shouldWork() {
		Blog invalidBlog = new Blog();
		invalidBlog.setName("abc");
		invalidBlog.setBlogStatus(BlogStatus.ACTIVE);
		invalidBlog.setUsers(new ArrayList<String>());

		Result result = testUpdate_withValidationError(Json.toJson(invalidBlog));
		assertTrue(result.status() == HttpStatus.SC_OK);
	}

	@Test
	public void testUpdate_withNameValidationFail_shouldThrowException() {
		Blog invalidBlog = new Blog();
		invalidBlog.setName("ac");
		invalidBlog.setBlogStatus(BlogStatus.ACTIVE);
		invalidBlog.setUsers(new ArrayList<String>());

		Result result = testUpdate_withValidationError(Json.toJson(invalidBlog));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
	}

	@Test
	public void testUpdate_withNullNameValidationFail_shouldThrowException() {
		Blog invalidBlog = new Blog();
		invalidBlog.setName(null);
		invalidBlog.setBlogStatus(BlogStatus.ACTIVE);
		invalidBlog.setUsers(new ArrayList<String>());

		Result result = testUpdate_withValidationError(Json.toJson(invalidBlog));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
	}

	@Test
	public void testUpdate_withStatusValidationFail_shouldThrowException() {
		Blog invalidBlog = new Blog();
		invalidBlog.setName("abc");
		invalidBlog.setBlogStatus(null);
		invalidBlog.setUsers(new ArrayList<String>());

		Result result = testUpdate_withValidationError(Json.toJson(invalidBlog));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("blogStatus"));
	}
	
	

	@Test
	public void testUpdate_withUserValidationFail_shouldThrowException() {
		Blog invalidBlog = new Blog();
		invalidBlog.setName("abc");
		invalidBlog.setBlogStatus(BlogStatus.ACTIVE);
		invalidBlog.setUsers(null);

		Result result = testUpdate_withValidationError(Json.toJson(invalidBlog));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("user"));
	}
	
	private Result testUpdate_withValidationError(JsonNode validationFail){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(validationFail)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    return result;
	}

	@Test
	public void testUpdate_withValid() {
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(updateNode)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);

		Blog updated = dao.get(testBlog1.getId());
		assertNotNull(updated);
		assertEquals("Test Blog", updated.getName());
		assertEquals(BlogStatus.MAINTENANCE.toString(), updated.getBlogStatus().toString());
		assertTrue(updated.getSettings().size() == 1);
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
		assertTrue(response.contains("Test Blog"));
		assertTrue(response.contains(BlogStatus.ACTIVE.toString()));
	}

	private String testFindById_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}
	
	@Test
	public void testFindAll_shouldReturnOne() {
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
		assertNull(dao.get(testBlog1.getId()));
	}
	

	@After
	public void teardown() {
		dao.deleteById(testBlog1.getId());
	}
}
