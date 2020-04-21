package beans;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.MessageDAO;
import dao.UserDAO;
import models.Message;
import models.User;

@Stateless
@Path("/messages")
@LocalBean
public class ChatMessageBean implements ChatMessageRemote{
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("messageDAO")==null) {
			ctx.setAttribute("messageDAO", new MessageDAO());
		}
		if(ctx.getAttribute("userDAO")==null) {
			ctx.setAttribute("userDAO", new UserDAO());
		}
	}
	
	@GET
	@Path("/test")
	public void test() {
		System.out.println("##########");
	}
	
	@POST
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendToAll(Message m) {
		MessageDAO messageDAO = (MessageDAO) ctx.getAttribute("messageDAO");
		Message message = new Message();
		message.setSender(m.getSender());
		message.setText(m.getText());
		message.setReciever(null);
		message.setSubject(message.getSender().getUsername() + " to all");
		message.setDate(Calendar.getInstance());
		messageDAO.getAllMessages().add(message);
	}
	
	@POST
	@Path("/{receiver}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendToUser(Message m, @PathParam("receiver") String username) {
		MessageDAO messageDAO = (MessageDAO) ctx.getAttribute("messageDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User receiver = userDAO.findByUsername(username);
		Message message = new Message();
		message.setSender(m.getSender());
		message.setText(m.getText());
		message.setReciever(receiver);
		message.setSubject("@" +message.getSender().getUsername() + " to @" + message.getReciever().getUsername());
		message.setDate(Calendar.getInstance());
		messageDAO.getAllMessages().add(message);
	}
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Message> allMessageOfUser(@PathParam("username") String username) {
		MessageDAO messageDAO = (MessageDAO) ctx.getAttribute("messageDAO");
		List<Message> messages = messageDAO.findByUser(username);
		return messages;
	}

}
