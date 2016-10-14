package model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	@Size(min=2,max=60)
	@NotNull
	private String name;
		
	@Reference
	private Category parentCategory;
	
	@Reference
	private List<Category> childCategories;
	
	@Min(0)
	private Integer rank;
	private Boolean sideCategory;
	private Map<String,String> attributes;
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
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
