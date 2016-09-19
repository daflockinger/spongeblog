package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

@Entity
@Indexes({
	// for editing
	@Index(fields = {@Field(value="blog"), @Field(value="user")}),
	@Index(fields = {@Field(value="blog"), @Field(value="user"), @Field(value="category")}),
	@Index(fields = {@Field(value="blog"), @Field(value="user"), @Field(value="status")}),
	// for view
	@Index(fields = {@Field(value="blog"), @Field(value="status")}),
	@Index(fields = {@Field(value="blog"), @Field(value="status"), @Field(value="category")})
})
public class Post extends BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;

	@Property
	private String blog;
	@Property
	private String user;
	
	private Date created;
	private Date modified;
	private String title;
	private String content;
	@Property
	private PostStatus status;
	private List<String> keywords;
	@Property
	private String category;
	
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
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
	public PostStatus getStatus() {
		return status;
	}
	public void setStatus(PostStatus status) {
		this.status = status;
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
}
