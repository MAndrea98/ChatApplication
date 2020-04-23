package beans;

import java.util.List;

import javax.ejb.Remote;
import javax.ws.rs.PathParam;

import models.Message;

@Remote
public interface ChatMessageRemote {

	public void sendToAll(Message m);
	
	public void sendToUser(Message m, @PathParam("receiver") String username);
	
	public List<Message> allMessageOfUser(@PathParam("username") String username);
}
