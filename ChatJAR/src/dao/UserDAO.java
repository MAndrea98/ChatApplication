package dao;

import java.util.ArrayList;

import models.User;

public class UserDAO {

	private ArrayList<User> allUsers = new ArrayList<User>();
	private ArrayList<User> loggedUsers = new ArrayList<User>();
	
	public UserDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(ArrayList<User> allUsers) {
		this.allUsers = allUsers;
	}

	public ArrayList<User> getLoggedUsers() {
		return loggedUsers;
	}

	public void setLoggedUsers(ArrayList<User> loggedUsers) {
		this.loggedUsers = loggedUsers;
	}
	
	public User findByUsername(String username) {
		System.out.println("###uso" + allUsers.size());
		for (User u : allUsers) {
			System.out.println("###for" + u.getUsername() + "#" + username);
			if (u.getUsername().equals(username)) {
				System.out.println("###naso");
				return u;
			}
		}
		return null;
	}
	
}
