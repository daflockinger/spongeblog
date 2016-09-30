package model;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


@Entity
@Indexes({
	@Index(fields = @Field(value="name"), options=@IndexOptions(unique=true))
})
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=Id.NAME)
public class Category extends BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Property
	private String name;
		
	@Reference
	private Category parentCategory;
	
	@Reference
	private List<Category> childCategories;
	
	private Integer rank;
	private Boolean sideCategory;
	
	
	public Boolean getSideCategory() {
		return sideCategory;
	}
	public void setSideCategory(Boolean sideCategory) {
		this.sideCategory = sideCategory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Category getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}
	public List<Category> getChildCategories() {
		return childCategories;
	}
	public void setChildCategories(List<Category> childCategories) {
		this.childCategories = childCategories;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
