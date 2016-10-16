package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;

import dao.PostDAO;
import dto.PaginationDTO;
import dto.PostDTO;
import model.Post;
import model.PostStatus;
import play.libs.Json;
import play.mvc.Http.RequestBuilder;
import play.test.WithApplication;
import play.mvc.Result;
import services.PostService;
import utils.BlogMapperFactory;

public class PostControllerTest extends WithApplication{

	private Post testPost1;
	private Post insertPost;
	protected JsonNode insertNode;
	protected JsonNode updateNode;
	protected String params;
	
	protected String routePath;
	protected String testId;

	protected PostDAO dao;


	@Before
	public void setup() {
		routePath = "/api/v1/posts";

		dao = new PostDAO();

		testPost1 = new Post();
		testPost1.setTitle("Test Post");
		testPost1.setContent("original content");
		testPost1.setPostStatus(PostStatus.PUBLIC);
		testPost1.setCategory("Test Category");
		testPost1.setUser("abc");

		dao.save(testPost1);
		testId = testPost1.getTitle().replaceAll(" ","_");//.getId().toHexString();

		testPost1.setContent("updated content");
		testPost1.setPostStatus(PostStatus.MAINTENANCE);

		insertPost = new Post();
		insertPost.setTitle("New Post");
		insertPost.setContent("new content");
		insertPost.setPostStatus(PostStatus.PRIVATE);
		insertPost.setCategory("Inserted Category");
		insertPost.setUser("abc");

		PaginationDTO settings = new PaginationDTO();
		settings.setPage(0);
		settings.setLimit(1);
		settings.setFilters(ImmutableMap.of("postStatus", PostStatus.PUBLIC.toString()));
		settings.setSortBy("title");

		insertNode = Json.toJson(insertPost);
		updateNode = Json.toJson(testPost1);
		params = "?page=0&limit=1&sort=title&postStatus=PUBLIC";

		testPost1.setContent("original content");
		testPost1.setPostStatus(PostStatus.PUBLIC);
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
		Post newPost = dao.find(dao.createQuery().filter("title", "New Post")).asList().get(0);

		assertNotNull(newPost);
		assertEquals("New Post", newPost.getTitle());
		assertEquals(PostStatus.PRIVATE.toString(), newPost.getPostStatus().toString());
		assertEquals(newPost.getContent(),"new content");
		dao.deleteByQuery(dao.createQuery().filter("title", "New Post"));
	}

	@Test
	public void testUpdate_withNotValid() {
		RequestBuilder request = new RequestBuilder().method("PUT")
				.bodyText("invalid")
	            .uri(routePath + "/12345678");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}
	
	
	@Test
	public void testUpdate_withValidationGood_shouldWork(){
		Post invalidPost = new Post();
		invalidPost.setUser("abc");
		invalidPost.setTitle(testPost1.getTitle());
		invalidPost.setCategory("abc");
		invalidPost.setPostStatus(PostStatus.PRIVATE);
		invalidPost.setId(testPost1.getId());
		
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidPost));

		assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	@Test
	public void testUpdate_withUserValidationFail_shouldThrowException(){
		Post invalidPost = new Post();
		invalidPost.setUser("ac");
		invalidPost.setTitle("Test Post");
		invalidPost.setCategory("abc");
		invalidPost.setPostStatus(PostStatus.PRIVATE);
		invalidPost.setId(testPost1.getId());
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidPost));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("user"));
	}
	
	@Test
	public void testUpdate_withNullUserValidationFail_shouldThrowException(){
		Post invalidPost = new Post();
		invalidPost.setUser(null);
		invalidPost.setTitle("Test Post");
		invalidPost.setCategory("abc");
		invalidPost.setPostStatus(PostStatus.PRIVATE);
		invalidPost.setId(testPost1.getId());
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidPost));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("user"));
	}
	
	@Test
	public void testUpdate_withCategoryValidationFail_shouldThrowException(){
		Post invalidPost = new Post();
		invalidPost.setUser("ack");
		invalidPost.setTitle("Test Post");
		invalidPost.setCategory("");
		invalidPost.setPostStatus(PostStatus.PRIVATE);
		invalidPost.setId(testPost1.getId());
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidPost));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("category"));
	}
	
	@Test
	public void testUpdate_withNullCategoryValidationFail_shouldThrowException(){
		Post invalidPost = new Post();
		invalidPost.setUser("abc");
		invalidPost.setTitle("Test Post");
		invalidPost.setCategory(null);
		invalidPost.setPostStatus(PostStatus.PRIVATE);
		invalidPost.setId(testPost1.getId());
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidPost));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("category"));
	}
	
	@Test
	public void testUpdate_withNullStatusValidationFail_shouldThrowException(){
		Post invalidPost = new Post();
		invalidPost.setUser("ack");
		invalidPost.setTitle("Test Post");
		invalidPost.setCategory("kk");
		invalidPost.setPostStatus(null);
		invalidPost.setId(testPost1.getId());
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidPost));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("postStatus"));
	}
	
	protected Result testUpdate_withValidationError(JsonNode validationFail){
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

		Post updated = dao.get(testPost1.getId());
		assertNotNull(updated);
		assertEquals("Test Post", updated.getTitle());
		assertEquals(PostStatus.MAINTENANCE.toString(), updated.getPostStatus().toString());
		assertEquals(updated.getContent(),"updated content");
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
		assertTrue(response.contains("Test Post"));
		assertTrue(response.contains(PostStatus.PUBLIC.toString()));
		assertTrue(response.contains("original content"));
	}

	@Test
	public void testFindAllInPage_withValid_() {
		String response = testFindAllInPage_withValid();
		assertTrue(response.contains("Test Post"));
		assertTrue(response.contains(PostStatus.PUBLIC.toString()));
		assertTrue(response.contains("original content"));
	}
	
	protected String testFindById_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}
	
	protected String testFindAllInPage_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + params);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
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
	            .uri(routePath + "/" + testPost1.getId());
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
		assertEquals(dao.get(testPost1.getId()).getPostStatus().toString(),PostStatus.DELETED.toString());
	}

	@After
	public void teardown() {
		dao.deleteById(testPost1.getId());
	}
}
