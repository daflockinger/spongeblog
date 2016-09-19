package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.KeywordDAO;
import dto.PaginationDTO;
import model.Keyword;
import play.libs.Json;
import services.KeywordService;

public class KeywordControllerTest extends BaseControllerTest<KeywordController, KeywordService, KeywordDAO, Keyword> {

	private Keyword testKeyword1;
	private Keyword insertKeyword;

	@Before
	public void setup() {
		routePath = "/api/v1/keywords";

		dao = new KeywordDAO();
		service = new KeywordService();
		service.setDao(dao);
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

		PaginationDTO settings = new PaginationDTO();
		settings.setPage(0);
		settings.setLimit(1);
		settings.setSortBy("name");

		insertNode = Json.toJson(insertKeyword);
		updateNode = Json.toJson(testKeyword1);
		pageNode = Json.toJson(settings);

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
	public void testFindAllInPage_withNotValid() {
		super.testFindAllInPage_withNotValid();
	}

	@Test
	public void testFindAllInPage_withValid_() {
		String response = super.testFindAllInPage_withValid();
		assertTrue(response.contains("Test Keyword"));
		assertTrue(response.contains("2"));
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