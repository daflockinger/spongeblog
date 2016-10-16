package utils;

import com.google.inject.Singleton;

import dto.BlogDTO;
import dto.CategoryDTO;
import dto.PostDTO;
import dto.UserDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import model.Blog;
import model.Category;
import model.Post;
import model.User;

@Singleton
public class BlogMapperFactory {

	private MapperFactory mapperFactory;

	public MapperFacade getFascade() {
		return mapperFactory.getMapperFacade();
	}

	public BlogMapperFactory() {
		mapperFactory = new DefaultMapperFactory.Builder().build();
		
		registerBlogMapper();
		registerCategoryMapper();
		registerKeywordMapper();
		registerLoginCredentialMapper();
		registerLoginResultMapper();
		registerPaginationMapper();
		registerPaginationQueryMapper();
		registerPostMapper();
		registerUserMapper();
	}

	private void registerBlogMapper() {
		mapperFactory.classMap(Blog.class, BlogDTO.class).byDefault().register();
		mapperFactory.classMap(BlogDTO.class, Blog.class).byDefault().register();
	}

	private void registerCategoryMapper() {
		mapperFactory.classMap(Category.class, CategoryDTO.class).byDefault().register();
		mapperFactory.classMap(CategoryDTO.class, Category.class).byDefault().register();
	}

	private void registerKeywordMapper() {
		mapperFactory.classMap(Blog.class, BlogDTO.class).byDefault().register();
		mapperFactory.classMap(BlogDTO.class, Blog.class).byDefault().register();
	}

	private void registerLoginCredentialMapper() {

	}

	private void registerLoginResultMapper() {

	}

	private void registerPaginationMapper() {
		//mapperFactory.classMap(Pagi.class, BlogDTO.class).byDefault().register();
	//	mapperFactory.classMap(BlogDTO.class, Blog.class).byDefault().register();
	}

	private void registerPaginationQueryMapper() {

	}

	private void registerPostMapper() {
		mapperFactory.classMap(Post.class, PostDTO.class).byDefault().register();
		mapperFactory.classMap(PostDTO.class, Post.class).byDefault().register();
	}

	private void registerUserMapper() {
		mapperFactory.classMap(User.class, UserDTO.class).byDefault().register();
		mapperFactory.classMap(UserDTO.class, User.class).byDefault().register();
	}
}
