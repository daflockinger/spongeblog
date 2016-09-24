package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


@Entity
@Indexes({
	// for editing
	@Index(fields = {@Field(value="user")}),
	@Index(fields = {@Field(value="user"), @Field(value="category")}),
	@Index(fields = {@Field(value="user"), @Field(value="postStatus")}),
	// for view
	@Index(fields = {@Field(value="postStatus")}),
	@Index(fields = {@Field(value="postStatus"), @Field(value="category")})
})
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=Id.NAME)
public class Post extends BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;

	@Property
	private String user;
	
	private Date created;
	private Date modified;
	private String title;
	private String content;
	@Property
	private PostStatus postStatus;
	private List<String> keywords;
	@Property
	private String category;

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public PostStatus getPostStatus() {
		return postStatus;
	}
	public void setPostStatus(PostStatus postStatus) {
		this.postStatus = postStatus;
	}
	
}
