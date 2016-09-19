package services;

import static org.junit.Assert.*;

import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import dao.PostDAO;
import dto.OperationResult;
import model.Post;
import model.PostStatus;
import play.test.WithApplication;

public class PostServiceTest extends WithApplication {
	private PostDAO dao;

	private Post testPost1;
	private PostService service;

	@Before
	public void setup() {
		dao = new PostDAO();
		service = new PostService();
		service.setDao(dao);

		testPost1 = new Post();
		testPost1.setBlog("testBlog");
		testPost1.setCategory("cat 1");
		testPost1.setUser("test");
		testPost1.setTitle("last Post");
		testPost1.setCreated(DateTime.now().minusDays(3).toDate());
		testPost1.setContent(
				"Aliquam tempor est in quam pharetra viverra at eget libero. In varius lacus massa, vel sollicitudin nibh interdum quis. Fusce tellus nisi, interdum eget elit condimentum, dictum imperdiet augue. Duis tempor, odio sit amet eleifend vestibulum, ex elit fermentum mi, ornare vestibulum lorem tortor ut elit. Mauris bibendum, tortor a tempor ultricies, ex est sodales libero, eget consectetur mi lacus congue urna. Fusce mollis sagittis consequat. Sed quis justo sit amet nibh iaculis blandit ut at felis. Nunc egestas sodales tellus, eget consectetur enim tincidunt nec. Ut at vulputate nibh.");
		testPost1.setKeywords(ImmutableList.of("tech"));
		testPost1.setStatus(PostStatus.PUBLIC);
		dao.save(testPost1);
	}
	
	@Test
	public void testDelete_withExistingId_shouldSetStatusToDelete(){
		OperationResult<Post> result =  service.delete(testPost1.getId());
		
		assertNotNull(result);
		assertTrue(result.getStatus() == HttpStatus.SC_OK);
		
		Post deletedPost = dao.get(testPost1.getId());
		assertNotNull(deletedPost);
		assertEquals(PostStatus.DELETED, deletedPost.getStatus());
	}
	
	@Test
	public void testDelete_withNotExistingId_shouldReturnNotFound(){
		OperationResult<Post> result = service.delete(null);
		assertTrue(result.getStatus() == HttpStatus.SC_NOT_FOUND);
	}
	
	@After
	public void teardown() {
		dao.deleteById(testPost1.getId());
	}	
}
