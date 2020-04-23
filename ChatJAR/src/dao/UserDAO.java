package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;

import models.User;

@Singleton
public class UserDAO {

	private List<User> allUsers = new ArrayList<User>();
	private List<User> loggedUsers = new ArrayList<User>();
	
	public UserDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

	public List<User> getLoggedUsers() {
		return loggedUsers;
	}

	public void setLoggedUsers(List<User> loggedUsers) {
		this.loggedUsers = loggedUsers;
	}
	
	public User findByUsername(String username) {
		for (User u : allUsers) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		return null;
	}
	
}
