package model;

import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;


@Entity
@Indexes({
	@Index(fields = @Field(value="name"), options=@IndexOptions(unique=true)),
	@Index(fields = @Field(value="status"))
})
public class Blog extends BaseModel {

	@Property
	private String name;
	@Property
	private BlogStatus status;
	
	private List<String> users;
	private Map<String, String> settings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BlogStatus getStatus() {
		return status;
	}

	public void setStatus(BlogStatus status) {
		this.status = status;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
}
