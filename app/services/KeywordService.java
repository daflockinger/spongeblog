package services;

import dao.KeywordDAO;
import model.Keyword;

public class KeywordService extends BaseService<Keyword, KeywordDAO>{

	@Override
	protected boolean isNotUnique(Keyword model) {
		return dao().exists(dao().createQuery().filter("name", model.getName()));
	}

}
