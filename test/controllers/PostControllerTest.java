package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.google.common.collect.ImmutableMap;
import com.sun.org.apache.bcel.internal.generic.NEW;

import dao.PostDAO;
import dto.PaginationDTO;
import model.Post;
import model.PostStatus;
import play.libs.Json;
import services.PostService;

public class PostControllerTest extends BaseControllerTest<PostController, PostService, PostDAO, Post> {

	private Post testPost1;
	private Post insertPost;

	@Before
	public void setup() {
		routePath = "/api/v1/posts";

		dao = new PostDAO();
		service = new PostService();
		service.setDao(dao);
		controller = new PostController();
		controller.setService(service);

		testPost1 = new Post();
		testPost1.setTitle("Test Post");
		testPost1.setContent("original content");
		testPost1.setPostStatus(PostStatus.PUBLIC);

		dao.save(testPost1);
		testId = testPost1.getId().toHexString();

		testPost1.setContent("updated content");
		testPost1.setPostStatus(PostStatus.MAINTENANCE);

		insertPost = new Post();
		insertPost.setTitle("New Post");
		insertPost.setContent("new content");
		insertPost.setPostStatus(PostStatus.PRIVATE);

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
		super.testCreate_withNotValid();
	}

	@Test
	public void testCreate_withValid() {
		super.testCreate_withValid();
		Post newPost = dao.find(dao.createQuery().filter("title", "New Post")).asList().get(0);

		assertNotNull(newPost);
		assertEquals("New Post", newPost.getTitle());
		assertEquals(PostStatus.PRIVATE.toString(), newPost.getPostStatus().toString());
		assertEquals(newPost.getContent(),"new content");
		dao.deleteByQuery(dao.createQuery().filter("title", "New Post"));
	}

	@Test
	public void testUpdate_withNotValid() {
		super.testUpdate_withNotValid();
	}

	@Test
	public void testUpdate_withValid() {
		super.testUpdate_withValid();

		Post updated = dao.get(testPost1.getId());
		assertNotNull(updated);
		assertEquals("Test Post", updated.getTitle());
		assertEquals(PostStatus.MAINTENANCE.toString(), updated.getPostStatus().toString());
		assertEquals(updated.getContent(),"updated content");
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
		assertTrue(response.contains("Test Post"));
		assertTrue(response.contains(PostStatus.PUBLIC.toString()));
		assertTrue(response.contains("original content"));
	}

	@Test
	public void testFindAllInPage_withValid_() {
		String response = super.testFindAllInPage_withValid();
		assertTrue(response.contains("Test Post"));
		assertTrue(response.contains(PostStatus.PUBLIC.toString()));
		assertTrue(response.contains("original content"));
	}

	@Test
	public void testDelete_withNotValid() {
		super.testDelete_withNotValid();
	}

	@Test
	public void testDelete_withValid() {
		super.testDelete_withValid();
		assertEquals(dao.get(testPost1.getId()).getPostStatus().toString(),PostStatus.DELETED.toString());
	}

	@After
	public void teardown() {
		dao.deleteById(testPost1.getId());
	}
}
