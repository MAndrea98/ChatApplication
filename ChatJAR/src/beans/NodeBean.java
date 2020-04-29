package beans;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import dao.NodeDAO;
import models.Host;

@Startup
@Stateless
@Path("")
@LocalBean
public class NodeBean implements NodeRemote, NodeLocal {
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/mojQueue")
	private Queue queue;
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("nodeDAO") == null) {
			ctx.setAttribute("nodeDAO", new NodeDAO());
		}
		
		NodeDAO nodeDAO = (NodeDAO) ctx.getAttribute("nodeDAO");
		if (nodeDAO.getAllHosts().size() == 0) {
			Host host = new Host("master", "master");
			nodeDAO.getAllHosts().add(host);
			ctx.setAttribute("nodeDAO", nodeDAO);
		}
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText("NodeBean started");
			sender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

	@POST
	@Path("/register")
	@Override
	public Host register(Host host) {
		// TODO Auto-generated method stub
		return null;
	}

	@POST
	@Path("/node")
	@Override
	public void newNode(Host host) {
		// TODO Auto-generated method stub
		
	}

	@POST
	@Path("/nodes")
	@Override
	public ArrayList<Host> allHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@DELETE
	@Path("/node/{alias}")
	@Override
	public void deleteNode(@PathParam("alias") String alias) {
		// TODO Auto-generated method stub
		
	}

	@GET
	@Path("/node")
	@Override
	public void checkNode(Host host) {
		// TODO Auto-generated method stub
		
	}

}
