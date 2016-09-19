package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import utils.ObjectIdSerializer;

@Entity
public class BaseModel{
	
	public final static String INVALID_JSON = "Invalid/Malformed Json";
	public final static String ALREADY_EXISTS = "Entity already exists";
	public final static String NOT_FOUND = "Entity not found";
	public final static String UNAUTHORIZED = "Username or password is not valid";
	
	@Id
	@JsonSerialize(using = ObjectIdSerializer.class)
	private ObjectId id ;
	
	@NotSaved
	@Transient
	private String errorMessage;
	

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
}
