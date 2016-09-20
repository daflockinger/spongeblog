package model;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import utils.ObjectIdSerializer;

@Entity
public class BaseModel{
		
	@Id
	@JsonSerialize(using = ObjectIdSerializer.class)
	private ObjectId id ;
	
	@NotSaved
	@Transient
	private String errorMessage;
	
	@JsonIgnore
	@NotSaved
	@Transient
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
	
	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
