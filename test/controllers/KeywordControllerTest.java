package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.KeywordDAO;
import dto.KeywordDTO;
import model.Keyword;
import play.libs.Json;
import play.mvc.Result;
import services.KeywordService;
import utils.BlogMapperFactory;

public class KeywordControllerTest extends BaseControllerTest<KeywordController, KeywordService, KeywordDAO, KeywordDTO, Keyword> {

	private Keyword testKeyword1;
	private Keyword insertKeyword;

	@Before
	public void setup() {
		routePath = "/api/v1/keywords";

		dao = new KeywordDAO();
		service = new KeywordService();
		service.setDao(dao);
		service.setMapperFactory(new BlogMapperFactory());
		controller = new KeywordController();
		controller.setService(service);

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
		super.testCreate_withNotValid();
	}
	
	@Test
	public void testCreate_withValid() {
		super.testCreate_withValid();
		Keyword newKeyword = dao.find(dao.createQuery().filter("name", "New Keyword")).asList().get(0);

		assertNotNull(newKeyword);
		assertEquals("New Keyword", newKeyword.getName());
		assertTrue(newKeyword.getPopularity() == 99);
		dao.deleteByQuery(dao.createQuery().filter("name", "New Keyword"));
	}

	@Test
	public void testCreate_withAlreadyExisting() {
		super.testCreate_withAlreadyExisting();
	}
	
	@Test
	public void testUpdate_withValidationGood_shouldWork(){
		Keyword invalidKeyword = new Keyword();
		invalidKeyword.setName("ab");
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidKeyword));

		assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	@Test
	public void testUpdate_withNameValidationFail_shouldThrowException(){
		Keyword invalidKeyword = new Keyword();
		invalidKeyword.setName("a");
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidKeyword));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
	}
	
	@Test
	public void testUpdate_withNameNullValidationFail_shouldThrowException(){
		Keyword invalidKeyword = new Keyword();
		invalidKeyword.setName(null);
		
		Result result = super.testUpdate_withValidationError(Json.toJson(invalidKeyword));

		assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
		assertTrue(contentAsString(result).contains("name"));
	}
	
	
	@Test
	public void testUpdate_withNotValid() {
		super.testUpdate_withNotValid();
	}

	@Test
	public void testUpdate_withValid() {
		super.testUpdate_withValid();

		Keyword updated = dao.get(testKeyword1.getId());
		assertNotNull(updated);
		assertEquals("Test Keyword", updated.getName());
		assertTrue(updated.getPopularity() == 3);
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
		assertTrue(response.contains("Test Keyword"));
		assertTrue(response.contains("2"));
	}

	@Test
	public void testFindAll_shouldReturnOne(){
		super.testFindAll_ShouldReturnOne();
	}

	@Test
	public void testDelete_withNotValid() {
		super.testDelete_withNotValid();
	}

	@Test
	public void testDelete_withValid() {
		super.testDelete_withValid();
		assertNull(dao.get(testKeyword1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteById(testKeyword1.getId());
	}

}
