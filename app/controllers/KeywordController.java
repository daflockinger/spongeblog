package controllers;

import com.google.inject.Inject;

import model.Keyword;
import services.KeywordService;

public class KeywordController extends BaseController<KeywordService, Keyword>{

	@Inject
	private KeywordService service;
	
	@Override
	protected KeywordService service() {
		return service;
	}

	public void setService(KeywordService service) {
		this.service = service;
	}
}
