package models;

import java.util.UUID;

public class User {
	
	private UUID id;
	private String username;
	private String password;
	private Host host;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
		this.id = UUID.randomUUID();
		this.host = new Host();
	}

	public User(String username, String password, Host host) {
		super();
		this.id = UUID.randomUUID();
		this.username = username;
		this.password = password;
		this.host = host;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", host=" + host + "]";
	}
	
	
	

}
