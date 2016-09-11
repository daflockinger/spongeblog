package model;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;


@Entity
public class Category extends BaseModel{
	
	private String name;
	
	@Reference
	private List<Post> posts;
	
	@Reference
	private Category parentCategory;
	
	@Reference
	private List<Category> childCategories;
	
	private Integer rank;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
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
