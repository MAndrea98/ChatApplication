package beans;

import java.util.List;

import javax.ejb.Remote;

import models.User;

@Remote
public interface ChatRemote {

	public User login(User u);
	
	public User register(User u, String confirm);
	
	public void logout(String username);
	
	public List<User> registered();
	
	public List<User> loggedIn();
}
