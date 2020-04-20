package beans;

import models.User;

public interface ChatRemote {

	public User login(String username, String password);
	
	public String register(String username, String password, String confirm);
}
