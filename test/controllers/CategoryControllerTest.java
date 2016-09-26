package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.CategoryDAO;
import dto.PaginationDTO;
import model.Category;
import play.libs.Json;
import services.CategoryService;

public class CategoryControllerTest extends BaseControllerTest<CategoryController, CategoryService, CategoryDAO, Category> {

	private Category testCategory1;
	private Category insertCategory;

	@Before
	public void setup() {
		routePath = "/api/v1/categories";

		dao = new CategoryDAO();
		service = new CategoryService();
		service.setDao(dao);
		controller = new CategoryController();
		controller.setService(service);

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
		super.testCreate_withNotValid();
	}
	
	@Test
	public void testCreate_withValid() {
		super.testCreate_withValid();
		Category newCategory = dao.find(dao.createQuery().filter("name", "New Category")).asList().get(0);

		assertNotNull(newCategory);
		assertEquals("New Category", newCategory.getName());
		assertTrue(newCategory.getRank() == 99);
		dao.deleteByQuery(dao.createQuery().filter("name", "New Category"));
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

		Category updated = dao.get(testCategory1.getId());
		assertNotNull(updated);
		assertEquals("Test Category", updated.getName());
		assertTrue(updated.getRank() == 3);
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
		assertTrue(response.contains("Test Category"));
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
		assertNull(dao.get(testCategory1.getId()));
	}

	@After
	public void teardown() {
		dao.deleteById(testCategory1.getId());
	}
}
