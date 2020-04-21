package beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.UserDAO;
import models.User;


@Stateless
@Path("/chat")
@LocalBean
public class ChatBean implements ChatRemote{

	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("userDAO")==null) {
			ctx.setAttribute("userDAO", new UserDAO());
		}
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public User login(User u) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		for (User user : users.getAllUsers()) {
			if (user.getUsername().equals(u.getUsername()) && user.getPassword().equals(u.getPassword())) {
				users.getLoggedUsers().add(user);
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
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		if (!u.getPassword().equals(confirm)) {
			return null;
		}
		
		for (User user : users.getAllUsers()) {
			if (user.getUsername().equals(u.getUsername())) {
				return null;
			}
		}
		User user = new User();
		user.setUsername(u.getUsername());
		user.setPassword(u.getPassword());
		users.getAllUsers().add(user);
		System.out.println("#######" + users.getAllUsers().size());
		return user;
	}

	@DELETE
	@Path("/loggedIn/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public void logout(@PathParam("username") String username) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		for (User user : users.getAllUsers()) {
			if (user.getUsername().equals(username)) {
				users.getLoggedUsers().remove(user);
			}
		}
	}

	@GET
	@Path("/registered")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<User> registered() {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		return users.getAllUsers();
	}

	@GET
	@Path("/loggedIn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<User> loggedIn() {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		return users.getLoggedUsers();
	}
	
	
}
