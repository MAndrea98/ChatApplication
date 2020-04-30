package beans;

import java.util.ArrayList;
import java.util.Calendar;

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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.MessageDAO;
import dao.UserDAO;
import models.Host;
import models.Message;
import models.User;

@Stateless
@Path("/messages")
@LocalBean
public class ChatMessageBean implements ChatMessageRemote, MessageLocal {
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/mojQueue")
	private Queue queue;
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("messageDAO") == null) {
			ctx.setAttribute("messageDAO", new MessageDAO());
		}
		if(ctx.getAttribute("userDAO") == null) {
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
	@Override
	public void sendToAll(Message m) {
		MessageDAO messageDAO = (MessageDAO) ctx.getAttribute("messageDAO");
		Message message = new Message();
		message.setSender(m.getSender());
		message.setText(m.getText());
		message.setReciever(new User("null", "null", new Host()));
		message.setSubject("@" + message.getSender().getUsername() + " to all");
		message.setDate(Calendar.getInstance());
		messageDAO.getAllMessages().add(message);
		ctx.setAttribute("messageDAO", messageDAO);
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText(message.getReciever().getUsername());
			sender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@POST
	@Path("/{receiver}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void sendToUser(Message m, @PathParam("receiver") String username) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User receiver = userDAO.findByUsername(username);
		MessageDAO messageDAO = (MessageDAO) ctx.getAttribute("messageDAO");
		Message message = new Message();
		message.setSender(m.getSender());
		message.setText(m.getText());
		message.setReciever(receiver);
		message.setSubject("@" +message.getSender().getUsername() + " to @" + message.getReciever().getUsername());
		message.setDate(Calendar.getInstance());
		messageDAO.getAllMessages().add(message);
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText(message.getReciever().getUsername());
			sender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ctx.setAttribute("messageDAO", messageDAO);
	}
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public ArrayList<Message> allMessageOfUser(@PathParam("username") String username) {
		MessageDAO messageDAO = (MessageDAO) ctx.getAttribute("messageDAO");
		ArrayList<Message> messages = messageDAO.findByUser(username);
		return messages;
	}

}
