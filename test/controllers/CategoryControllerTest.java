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

import dao.CategoryDAO;
import dto.CategoryDTO;
import model.Category;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.test.WithApplication;
import services.CategoryService;
import utils.BlogMapperFactory;

public class CategoryControllerTest extends WithApplication {

	private Category testCategory1;
	private Category insertCategory;
	protected CategoryController controller;
	protected CategoryService service;
	protected CategoryDAO dao;
	
	protected JsonNode insertNode;
	protected JsonNode updateNode;
	protected String params;
	
	protected String routePath;
	protected String testId;

	@Before
	public void setup() {
		routePath = "/api/v1/categories";

		dao = new CategoryDAO();
		service = new CategoryService();
		service.setDao(dao);
		service.setMapperFactory(new BlogMapperFactory());
		controller = new CategoryController();
		//controller.setService(service);
		SimpleController<CategoryDTO, Category, CategoryService> simple = new SimpleController<>();
		simple.setService(service);
		controller.setSimple(simple);

		testCategory1 = new Category();
		testCategory1.setName("Test Category");
		testCategory1.setRank(2);

		dao.save(testCategory1);
		testId = testCategory1.getId().toHexString();

		testCategory1.setRank(3);
		
		
		insertCategory = new Category();
		insertCategory.setName("New Category");
		insertCategory.setRank(99);

		insertNode = Json.toJson(insertCategory);
		updateNode = Json.toJson(testCategory1);

		testCategory1.setRank(2);
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
		Category newCategory = dao.find(dao.createQuery().filter("name", "New Category")).asList().get(0);

		assertNotNull(newCategory);
		assertEquals("New Category", newCategory.getName());
		assertTrue(newCategory.getRank() == 99);
		dao.deleteByQuery(dao.createQuery().filter("name", "New Category"));
	}

	@Test
	public void testCreate_withAlreadyExisting() {
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(updateNode)
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_CONFLICT);
	}

	private Result testUpdate_withValidationError(JsonNode validationFail){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(validationFail)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    return result;
	}
	
	@Test
	public void testUpdate_withValidationGood_shouldWork(){
		Category invalidCategory = new Category();
		invalidCategory.setName("ab");
		invalidCategory.setRank(0);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidCategory));

		assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	@Test
	public void testUpdate_withNameValidationFail_shouldThrowException(){
		Category invalidCategory = new Category();
		invalidCategory.setName("b");
		invalidCategory.setRank(0);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidCategory));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
	}
	
	@Test
	public void testUpdate_withNullNameValidationFail_shouldThrowException(){
		Category invalidCategory = new Category();
		invalidCategory.setName(null);
		invalidCategory.setRank(0);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidCategory));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
	}
	
	@Test
	public void testUpdate_withToLowRankValidationFail_shouldThrowException(){
		Category invalidCategory = new Category();
		invalidCategory.setName("ab");
		invalidCategory.setRank(-1);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidCategory));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("rank"));
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
	public void testUpdate_withValid() {
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(updateNode)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);

		Category updated = dao.get(testCategory1.getId());
		assertNotNull(updated);
		assertEquals("Test Category", updated.getName());
		assertTrue(updated.getRank() == 3);
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
	
	private String testFindById_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}
	
	private String testFindAllInPage_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + params);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}

	@Test
	public void testFindById_withValid_() {
		String response = testFindById_withValid();
		assertTrue(response.contains("Test Category"));
		assertTrue(response.contains("2"));
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
		assertNull(dao.get(testCategory1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteById(testCategory1.getId());
	}
}
