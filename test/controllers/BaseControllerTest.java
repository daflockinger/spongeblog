package controllers;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.JsonNode;

import dao.ExtendedDAO;
import dto.BaseDTO;
import model.BaseModel;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.WithApplication;
import services.BaseService;

public class BaseControllerTest<C extends BaseController<S, M, N>, S extends BaseService<M,N>, D extends ExtendedDAO<N, ObjectId>, M extends BaseDTO, N extends BaseModel>
		extends WithApplication {

	protected C controller;
	protected S service;
	protected D dao;
	
	protected JsonNode insertNode;
	protected JsonNode updateNode;
	protected String params;
	
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
	
	protected Result testUpdate_withValidationError(JsonNode validationFail){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(validationFail)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    return result;
	}

	protected void testUpdate_withNotValid(){
		RequestBuilder request = new RequestBuilder().method("PUT")
				.bodyText("invalid")
	            .uri(routePath + "/12345678");
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_BAD_REQUEST);
	}
	
	protected void testUpdate_withValid(){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(updateNode)
	            .uri(routePath + "/" + testId);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	}
	
	protected void testUpdate_withNotExisting(){
		RequestBuilder request = new RequestBuilder().method("PUT")
	            .bodyJson(insertNode)
	            .uri(routePath + "/1234567890123");
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
	
	protected String testFindAllInPage_withValid(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath + params);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
	    return contentAsString(result);
	}
	
	protected void testFindAll_ShouldReturnOne(){
		RequestBuilder request = new RequestBuilder().method("GET")
	            .uri(routePath);
	    Result result = route(request);
	    
	    assertTrue(result.status() == HttpStatus.SC_OK);
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
