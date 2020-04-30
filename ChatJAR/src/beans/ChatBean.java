package beans;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
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
public class ChatBean implements ChatRemote, ChatLocal {
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/mojQueue")
	private Queue queue;
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO());
		}
	}
	

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public User login(User u) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		for (User user : userDAO.getAllUsers()) {
			if (user.getUsername().equals(u.getUsername()) && user.getPassword().equals(u.getPassword())) {
				userDAO.getLoggedUsers().add(user);
				try {
					QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
					QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
					QueueSender sender = session.createSender(queue);
					// create and publish a message
					TextMessage mess = session.createTextMessage();
					mess.setText(user.getUsername() + " loged in");
					sender.send(mess);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				
				ctx.setAttribute("userDAO", userDAO);
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
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
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
		ctx.setAttribute("userDAO", userDAO);
        
		return user;
	}

	@DELETE
	@Path("/loggedIn/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public void logout(@PathParam("username") String username) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		for(int i = 0; i < userDAO.getLoggedUsers().size(); i++) {
			System.out.println(username + "###" + userDAO.getLoggedUsers().get(i).getUsername());
			if (userDAO.getLoggedUsers().get(i).getUsername().equals(username)) {
				userDAO.getLoggedUsers().remove(i);
				try {
					QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
					QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
					QueueSender sender = session.createSender(queue);
					// create and publish a message
					TextMessage mess = session.createTextMessage();
					mess.setText("Someone loged out");
					sender.send(mess);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		System.out.println(userDAO.getLoggedUsers().size() + " " + userDAO.getAllUsers().size());
		ctx.setAttribute("userDAO", userDAO);
	}

	@GET
	@Path("/registered")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<User> registered() {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.getAllUsers();
	}

	@GET
	@Path("/loggedIn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<User> loggedIn() {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.getLoggedUsers();
	}


	@POST
	@Path("/post/{text}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String test(@PathParam("text") String text) {
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			// create and publish a message
			TextMessage message = session.createTextMessage();
			message.setText(text);
			sender.send(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "OK";
	}
	
	@GET
	@Path("/get_user/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User get(@PathParam("username") String username) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.findByUsername(username);
	}
	
	
}
