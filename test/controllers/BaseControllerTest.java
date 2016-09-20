package controllers;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.JsonNode;

import dao.ExtendedDAO;
import model.BaseModel;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.WithApplication;
import services.BaseService;

public class BaseControllerTest<C extends BaseController<S, M>, S extends BaseService<M>, D extends ExtendedDAO<M, ObjectId>, M extends BaseModel>
		extends WithApplication {

	protected C controller;
	protected S service;
	protected D dao;
	
	protected JsonNode insertNode;
	protected JsonNode updateNode;
	protected JsonNode pageNode;
	
	protected String routePath;
	protected String testId;

	protected void testCreate_withNotValid(){
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyText("invalid")
	            .uri(routePath);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}
	
	protected void testCreate_withValid(){
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(insertNode)
	            .uri(routePath);
	    Result result = route(request);	  
	    
	    assertTrue(result.status() == HttpStatus.SC_CREATED);
	}
	
	protected void testCreate_withAlreadyExisting(){
		RequestBuilder request = new RequestBuilder().method("POST")
	            .bodyJson(updateNode)
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_CONFLICT);
	}
	

	protected void testUpdate_withNotValid(){
		RequestBuilder request = new RequestBuilder().method("PUT")
				.bodyText("invalid")
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}
	
	protected void testUpdate_withValid(){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(updateNode)
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	protected void testUpdate_withNotExisting(){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(insertNode)
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_NOT_FOUND);
	}
	

	protected void testFindById_withNotValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath  + "/invalid");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_NOT_FOUND);
	}
	
	protected String testFindById_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}
	

	protected void testFindAllInPage_withNotValid(){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyText("invalid")
	            .uri(routePath + "/page");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}
	
	protected String testFindAllInPage_withValid(){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(pageNode)
	            .uri(routePath + "/page");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}
	
	
	protected void testDelete_withValid(){
		RequestBuilder request = new RequestBuilder().method("DELETE")
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	protected void testDelete_withNotValid(){
		RequestBuilder request = new RequestBuilder().method("DELETE")
	            .uri(routePath + "/invalid");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_NOT_FOUND);
	}
}
