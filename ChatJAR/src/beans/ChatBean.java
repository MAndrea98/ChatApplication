package beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import models.User;

@Stateless
@Path("/chat")
@LocalBean
public class ChatBean implements ChatRemote{
	
	private List<User> users = new ArrayList<User>();
	private List<User> loggedIn = new ArrayList<User>();
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "OK";
	}
	
	@POST
	@Path("/post/{text}")
	@Produces(MediaType.TEXT_PLAIN)
	public String post(@PathParam("text") String text) {
		System.out.println("Received message: " + text);
		return "OK";
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public User login(User u) {
		// TODO Auto-generated method stub
		for (User user : users) {
			if (user.getUsername().equals(u.getUsername()) && user.getPassword().equals(u.getPassword())) {
				loggedIn.add(user);
				return user;
			}
		}
		
		return null;
	}
	
	@POST
	@Path("/register/{confirm}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public User register(User u, @PathParam("confirm") String confirm) {
		// TODO Auto-generated method stub
		if (!u.getPassword().equals(confirm)) {
			return null;
		}
		
		for (User user : users) {
			if (user.getUsername().equals(u.getUsername())) {
				return null;
			}
		}
		User user = new User();
		user.setUsername(u.getUsername());
		user.setPassword(u.getPassword());
		users.add(user);
		return user;
	}
	
	
}
