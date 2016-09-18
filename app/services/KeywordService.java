package services;

import com.google.inject.Inject;

import dao.KeywordDAO;
import model.Keyword;

public class KeywordService extends BaseServiceImpl<Keyword, KeywordDAO>{

	@Inject
	private KeywordDAO dao;
	
	@Override
	protected boolean isNotUnique(Keyword model) {
		return dao().exists(dao().createQuery().filter("name", model.getName()));
	}

	@Override
	public Class<Keyword> getModelClass() {
		return Keyword.class;
	}

	@Override
	protected KeywordDAO dao() {
		return dao;
	}

	public void setDao(KeywordDAO dao){
		this.dao = dao;
	}
}
