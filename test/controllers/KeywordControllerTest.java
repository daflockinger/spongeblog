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

import dao.KeywordDAO;
import dto.CategoryDTO;
import dto.KeywordDTO;
import model.Category;
import model.Keyword;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.test.WithApplication;
import services.CategoryService;
import services.KeywordService;
import utils.BlogMapperFactory;

public class KeywordControllerTest extends WithApplication {
	
	private Keyword testKeyword1;
	private Keyword insertKeyword;
	protected KeywordController controller;
	protected KeywordService service;
	protected KeywordDAO dao;
	
	protected JsonNode insertNode;
	protected JsonNode updateNode;
	protected String params;
	
	protected String routePath;
	protected String testId;
	
	@Before
	public void setup() {
		routePath = "/api/v1/keywords";

		dao = new KeywordDAO();
		service = new KeywordService();
		service.setDao(dao);
		service.setMapperFactory(new BlogMapperFactory());
		controller = new KeywordController();
		//controller.setService(service);
		SimpleController<KeywordDTO, Keyword, KeywordService> simple = new SimpleController<>();
		simple.setService(service);
		controller.setSimple(simple);

		testKeyword1 = new Keyword();
		testKeyword1.setName("Test Keyword");
		testKeyword1.setPopularity(2);

		dao.save(testKeyword1);
		testId = testKeyword1.getId().toHexString();

		testKeyword1.setPopularity(3);
		
		
		insertKeyword = new Keyword();
		insertKeyword.setName("New Keyword");
		insertKeyword.setPopularity(99);

		insertNode = Json.toJson(insertKeyword);
		updateNode = Json.toJson(testKeyword1);

		testKeyword1.setPopularity(2);
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
		Keyword newKeyword = dao.find(dao.createQuery().filter("name", "New Keyword")).asList().get(0);
		
		assertNotNull(newKeyword);
		assertEquals("New Keyword", newKeyword.getName());
		assertTrue(newKeyword.getPopularity() == 99);
		dao.deleteByQuery(dao.createQuery().filter("name", "New Keyword"));
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
	public void testUpdate_withValidationGood_shouldWork(){
		Keyword invalidKeyword = new Keyword();
		invalidKeyword.setName("ab");
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidKeyword));

		assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	protected Result testUpdate_withValidationError(JsonNode validationFail){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(validationFail)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    return result;
	}
	
	@Test
	public void testUpdate_withNameValidationFail_shouldThrowException(){
		Keyword invalidKeyword = new Keyword();
		invalidKeyword.setName("a");
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidKeyword));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
	}
	
	@Test
	public void testUpdate_withNameNullValidationFail_shouldThrowException(){
		Keyword invalidKeyword = new Keyword();
		invalidKeyword.setName(null);
		
		Result result = testUpdate_withValidationError(Json.toJson(invalidKeyword));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
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

		Keyword updated = dao.get(testKeyword1.getId());
		assertNotNull(updated);
		assertEquals("Test Keyword", updated.getName());
		assertTrue(updated.getPopularity() == 3);
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
		assertTrue(response.contains("Test Keyword"));
		assertTrue(response.contains("2"));
	}
	
	protected String testFindById_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
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
		assertNull(dao.get(testKeyword1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteById(testKeyword1.getId());
	}

}
