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

	private UserDAO userDAO;
	
	@PostConstruct
	public void init() {
		if (userDAO == null) {
			userDAO = new UserDAO();
		}
	}
	

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public User login(User u) {
		//UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		for (User user : userDAO.getAllUsers()) {
			if (user.getUsername().equals(u.getUsername()) && user.getPassword().equals(u.getPassword())) {
				userDAO.getLoggedUsers().add(user);
				return user;
			}
		}
		//ctx.setAttribute("userDAO", users);
		return null;
	}
	
	@POST
	@Path("/register/{confirm}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public User register(User u, @PathParam("confirm") String confirm) {
		//UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		if (!u.getPassword().equals(confirm) || u.getUsername() == "null") {
			return null;
		}
		
		for (User user : userDAO.getAllUsers()) {
			if (user.getUsername().equals(u.getUsername())) {
				return null;
			}
		}
		User user = new User();
		user.setUsername(u.getUsername());
		user.setPassword(u.getPassword());
		userDAO.getAllUsers().add(user);
		//ctx.setAttribute("userDAO", users);
		return user;
	}

	@DELETE
	@Path("/loggedIn/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public void logout(@PathParam("username") String username) {
		//UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		for (User user : userDAO.getAllUsers()) {
			if (user.getUsername().equals(username)) {
				userDAO.getLoggedUsers().remove(user);
			}
		}
		//ctx.setAttribute("userDAO", users);
	}

	@GET
	@Path("/registered")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<User> registered() {
		//UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.getAllUsers();
	}

	@GET
	@Path("/loggedIn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<User> loggedIn() {
		//UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.getLoggedUsers();
	}
	
	
}
