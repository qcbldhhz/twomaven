package xc.ssh.domain;

import java.util.HashSet;
import java.util.Set;

public class Classes {
	private int id;
	private String name;
	private Set<User> users= new HashSet<User>();
	
	public int getId() {
		return id; 
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "Classes [id=" + id + ", name=" + name + ", users=" + users
				+ "]";
	}
	
	
	
	
	
}
