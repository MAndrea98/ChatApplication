package beans;

import models.User;

public interface ChatRemote {

	public User login(User u);
	
	public User register(User u, String confirm);
}
