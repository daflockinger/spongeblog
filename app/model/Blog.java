package model;

import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;


@Entity
public class Blog extends BaseModel {

	private String name;
	private BlogStatus status;
	
	@Reference(idOnly=false)
	private List<User> users;
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
}
