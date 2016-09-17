package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import dto.PaginationDTO;
import dto.PaginationQueryDTO;
import model.Category;
import model.Keyword;
import model.Post;
import model.PostStatus;
import model.User;
import model.UserStatus;

public class PostDAOTest {

	private PostDAO dao;
	private UserDAO userDao;
	private CategoryDAO categoryDao;
	private KeywordDAO keywordDAO;

	private Post testPost1;
	private Post testPost2;
	private Post testPost3;
	private Post testPost4;
	private Post testPost5;

	@Before
	public void setup() {
		dao = new PostDAO();
		userDao = new UserDAO();
		categoryDao = new CategoryDAO();
		keywordDAO = new KeywordDAO();

		User testUser = new User();
		testUser.setLogin("test");
		testUser.setEmail("test@testinger.cc");
		testUser.setNickname("te");
		testUser.setPassword("1234");
		testUser.setStatus(UserStatus.ADMIN);
		userDao.save(testUser);

		Category testCategory1 = new Category();
		testCategory1.setName("cat 1");
		testCategory1.setRank(1);
		categoryDao.save(testCategory1);

		Category testCategory2 = new Category();
		testCategory1.setName("cat 2");
		testCategory1.setRank(2);
		categoryDao.save(testCategory2);

		Keyword keyword1 = new Keyword();
		keyword1.setName("tech");
		keyword1.setPopularity(10);
		keywordDAO.save(keyword1);

		Keyword keyword2 = new Keyword();
		keyword2.setName("food");
		keyword2.setPopularity(20);
		keywordDAO.save(keyword2);

		Keyword keyword3 = new Keyword();
		keyword3.setName("drinks");
		keyword3.setPopularity(100);
		keywordDAO.save(keyword3);

		testPost1 = new Post();
		testPost1.setBlog("testBlog");
		testPost1.setCategory(testCategory1);
		testPost1.setUser(testUser);
		testPost1.setTitle("last Post");
		testPost1.setCreated(DateTime.now().minusDays(3).toDate());
		testPost1.setContent(
				"Aliquam tempor est in quam pharetra viverra at eget libero. In varius lacus massa, vel sollicitudin nibh interdum quis. Fusce tellus nisi, interdum eget elit condimentum, dictum imperdiet augue. Duis tempor, odio sit amet eleifend vestibulum, ex elit fermentum mi, ornare vestibulum lorem tortor ut elit. Mauris bibendum, tortor a tempor ultricies, ex est sodales libero, eget consectetur mi lacus congue urna. Fusce mollis sagittis consequat. Sed quis justo sit amet nibh iaculis blandit ut at felis. Nunc egestas sodales tellus, eget consectetur enim tincidunt nec. Ut at vulputate nibh.");
		testPost1.setKeywords(ImmutableList.of(keyword1));
		testPost1.setStatus(PostStatus.PUBLIC);
		dao.save(testPost1);

		testPost2 = new Post();
		testPost2.setBlog("testBlog2");
		testPost2.setCategory(testCategory1);
		testPost2.setUser(testUser);
		testPost2.setTitle("another blogs Post");
		testPost2.setCreated(DateTime.now().minusDays(4).toDate());
		testPost2.setContent(
				"Integer convallis sagittis pellentesque. Sed vel odio lobortis, egestas felis in, iaculis sem. Suspendisse feugiat nulla vel turpis tincidunt auctor.");
		testPost2.setKeywords(ImmutableList.of(keyword1));
		testPost2.setStatus(PostStatus.PUBLIC);
		dao.save(testPost2);

		testPost3 = new Post();
		testPost3.setBlog("testBlog");
		testPost3.setCategory(testCategory1);
		testPost3.setUser(testUser);
		testPost3.setTitle("old post");
		testPost3.setCreated(DateTime.now().minusDays(4).toDate());
		testPost3.setContent(
				"Vestibulum interdum auctor quam nec finibus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. ");
		testPost3.setKeywords(ImmutableList.of(keyword1, keyword2));
		testPost3.setStatus(PostStatus.PUBLIC);
		dao.save(testPost3);

		testPost4 = new Post();
		testPost4.setBlog("testBlog");
		testPost4.setCategory(testCategory2);
		testPost4.setUser(testUser);
		testPost4.setTitle("oldest post");
		testPost4.setCreated(DateTime.now().minusDays(30).toDate());
		testPost4.setContent(
				"Nulla id consectetur lacus. Cras condimentum, urna a semper tempor, neque nisi consequat velit, eu eleifend risus est vel enim.");
		testPost4.setKeywords(ImmutableList.of(keyword3, keyword2));
		testPost4.setStatus(PostStatus.PUBLIC);
		dao.save(testPost4);

		testPost5 = new Post();
		testPost5.setBlog("testBlog");
		testPost5.setCategory(testCategory1);
		testPost5.setUser(testUser);
		testPost5.setTitle("inactive Post");
		testPost5.setCreated(DateTime.now().minusDays(10).toDate());
		testPost5.setContent("Cras sed lacus nec dui viverra tincidunt. Pellentesque malesuada a augue quis euismod.");
		testPost5.setKeywords(ImmutableList.of(keyword3));
		testPost5.setStatus(PostStatus.MAINTENANCE);
		dao.save(testPost5);
	}

	@Test
	public void testFindAllInPage_withNullDto_shouldReturnEmpty() {
		assertNotNull(dao.findAllInPage(null));
	}
	
	@Test
	public void testFindAllInPage_testFilterAndSort_withValidDto_shouldReturnOnlyActivePostsFromTestBlogOrderdByNewestFirst() {
		PaginationDTO pagination = new PaginationDTO();
		pagination.setLimit(10);
		pagination.setPage(0);
		pagination.setSortBy("created");
		pagination.setSortAsc(false);
		pagination.setFilters(ImmutableMap.of("blog","testBlog","status", PostStatus.PUBLIC.toString()));
		
		List<Post> posts = dao.findAllInPage(new PaginationQueryDTO<Post>(pagination, Post.class));
		assertNotNull(posts);
		
		assertEquals(3, posts.size());
				
		assertEquals("last Post", posts.get(0).getTitle());
		assertEquals("old post", posts.get(1).getTitle());
		assertEquals("oldest post", posts.get(2).getTitle());
	}
	
	@Test
	public void testFindAllInPage_testLimitAndOffset_withValidDto_shouldReturnTwoPostsFromPageTwoSortedByTitle() {
		PaginationDTO pagination = new PaginationDTO();
		pagination.setLimit(2);
		pagination.setPage(1);
		pagination.setSortBy("title");
		pagination.setSortAsc(true);
		
		List<Post> posts = dao.findAllInPage(new PaginationQueryDTO<Post>(pagination, Post.class));
		assertNotNull(posts);
		
		assertEquals(2, posts.size());
		
		assertEquals("last Post", posts.get(0).getTitle());
		assertEquals("old post", posts.get(1).getTitle());
	}

	@After
	public void teardown() {
		dao.deleteById(testPost1.getId());
		dao.deleteById(testPost2.getId());
		dao.deleteById(testPost3.getId());
		dao.deleteById(testPost4.getId());
		dao.deleteById(testPost5.getId());
		keywordDAO.deleteByQuery(keywordDAO.createQuery().filter("name", "food"));
		keywordDAO.deleteByQuery(keywordDAO.createQuery().filter("name", "tech"));
		keywordDAO.deleteByQuery(keywordDAO.createQuery().filter("name", "drinks"));
		categoryDao.deleteByQuery(categoryDao.createQuery().filter("name", "cat 1"));
		categoryDao.deleteByQuery(categoryDao.createQuery().filter("name", "cat 2"));
		userDao.deleteByQuery(userDao.createQuery().filter("login", "test"));
	}
}
