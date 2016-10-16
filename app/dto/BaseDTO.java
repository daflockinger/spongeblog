package dto;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import utils.ObjectIdSerializer;

public class BaseDTO {

	@JsonSerialize(using = ObjectIdSerializer.class)
	private ObjectId id ;
	
	private String errorMessage;
	private Integer status = HttpStatus.SC_OK;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
