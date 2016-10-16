package services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.google.inject.Inject;

import dao.BlogDAO;
import dto.BlogDTO;
import dto.RestError;
import exceptions.BlogOnlyOneInstanceAllowedException;
import exceptions.GeneralServiceException;
import exceptions.ModelAlreadyExistsException;
import exceptions.ModelNotFoundException;
import model.Blog;
import play.Logger;

public class BlogService extends BaseServiceImpl<Blog, BlogDTO, BlogDAO> {

	@Inject
	private BlogDAO dao;

	public BlogDTO create(BlogDTO model) throws BlogOnlyOneInstanceAllowedException, ModelAlreadyExistsException {
		if (model == null || (model != null && isNotUnique(model))) {
			throw new ModelAlreadyExistsException("Blog");
		}
		if (CollectionUtils.isNotEmpty(findAll())) {
			throw new BlogOnlyOneInstanceAllowedException("only 1 blog allowed " + findAll().size());
		}

		dao().save(mapper().map(model, getModelClass()));
		model.setStatus(HttpStatus.SC_CREATED);

		return model;
	}

	public BlogDTO findById(ObjectId id) throws ModelNotFoundException {
		return super.findById(id);
	}

	public List<BlogDTO> findAll() throws BlogOnlyOneInstanceAllowedException {
		List<BlogDTO> blogs = new ArrayList<>();
		try {
			blogs = super.findAll();
		} catch (GeneralServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (CollectionUtils.isNotEmpty(blogs) && blogs.size() > 1) {
			throw new BlogOnlyOneInstanceAllowedException("more than 1 blog in database, please delete one");
		}
		return blogs;
	}

	public BlogDTO errorModel(RestError message) {
		return errorModel(message, "");
	}

	public BlogDTO errorModel(RestError message, String addonMessage) {
		BlogDTO errorModel = new BlogDTO();
		errorModel.setErrorMessage(message.toString() + addonMessage);
		errorModel.setStatus(message.status());
		return errorModel;
	}

	public BlogDTO update(BlogDTO model) throws ModelNotFoundException {
		// TODO transform dto to model
		if (model == null || isNotValid(model)) {
			throw new ModelNotFoundException("Blog");
		}
		dao().save(mapper().map(model,Blog.class));

		return model;
	}

	private boolean isNotValid(BlogDTO model) {
		return model.getId() == null;
	}

	@Override
	protected boolean isNotUnique(BlogDTO model) {
		return dao.exists(dao.createQuery().filter("name", model.getName()));
	}

	@Override
	public Class<Blog> getModelClass() {
		return Blog.class;
	}

	@Override
	protected BlogDAO dao() {
		return dao;
	}

	public void setDao(BlogDAO dao) {
		this.dao = dao;
	}

	@Override
	public Class<BlogDTO> getDTOClass() {
		return BlogDTO.class;
	}
}
