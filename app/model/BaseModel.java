package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import utils.ObjectIdSerializer;

@Entity
public class BaseModel{
		
	@Id
	@JsonSerialize(using = ObjectIdSerializer.class)
	private ObjectId id ;
	
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
