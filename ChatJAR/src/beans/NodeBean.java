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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import dao.NodeDAO;
import dao.UserDAO;
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
	
	private static int incrementerN;
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("nodeDAO") == null) {
			ctx.setAttribute("nodeDAO", new NodeDAO());
		}
		
		Enumeration<NetworkInterface> en;
        
		NodeDAO nodeDAO = (NodeDAO) ctx.getAttribute("nodeDAO");
		if (nodeDAO.getAllHosts().size() == 0) {
			try {
				en = NetworkInterface.getNetworkInterfaces();
				NetworkInterface n = en.nextElement();
			    Enumeration<InetAddress> ee = n.getInetAddresses();
			    List<InetAddress> addreses = new ArrayList<InetAddress>();
			    while (ee.hasMoreElements())
			    {
			        InetAddress i = ee.nextElement();
			        addreses.add(i);
			        
			    }
			    if(addreses.size() > 0)
			    	System.out.println("######" + addreses.get(0).getHostAddress());
	            Host host = new Host("master", addreses.get(0).getHostAddress());
	            nodeDAO.getAllHosts().add(host);
	            ResteasyClient client = new ResteasyClientBuilder().build();
	            String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/register";
	            System.out.println(http);
            	ResteasyWebTarget target = client.target(http);
            	Response response = target.request().post(Entity.entity(host, "application/json"));
            	String ret = response.readEntity(String.class);
            	System.out.println(ret);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			en = NetworkInterface.getNetworkInterfaces();
			NetworkInterface n = en.nextElement();
		    Enumeration<InetAddress> ee = n.getInetAddresses();
		    List<InetAddress> addreses = new ArrayList<InetAddress>();
		    
		    while (ee.hasMoreElements())
		    {
		        InetAddress i = ee.nextElement();
		        addreses.add(i);
		    }
		    
            if (!nodeDAO.findByIp(addreses.get(0).getHostAddress())) {
            	String alias = "Node" + incrementerN++;
            	Host host = new Host(alias, addreses.get(0).getHostAddress());
            	ResteasyClient client = new ResteasyClientBuilder().build();
            	String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/register";
            	System.out.println(http);
            	ResteasyWebTarget target = client.target(http);
            	Response response = target.request().post(Entity.entity(host, "application/json"));
            	String ret = response.readEntity(String.class);
            	System.out.println(ret);
            }
            
		} catch (SocketException e) {
			e.printStackTrace();
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
		
		ctx.setAttribute("nodeDAO", nodeDAO);
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String register(Host host) {
		NodeDAO nodeDAO = (NodeDAO) ctx.getAttribute("nodeDAO");
		nodeDAO.getAllHosts().add(host);
		ctx.setAttribute("nodeDAO", nodeDAO);
		return "OK";
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
	public ArrayList<Host> allHost(Host newHost) {
		NodeDAO nodeDAO = (NodeDAO) ctx.getAttribute("nodeDAO");
		return nodeDAO.getAllExceptNew(newHost);
	}

	@DELETE
	@Path("/node/{alias}")
	@Override
	public void deleteNode(@PathParam("alias") String alias) {
		NodeDAO nodeDAO = (NodeDAO) ctx.getAttribute("nodeDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		nodeDAO.deleteByAlias(alias, userDAO);
		ctx.setAttribute("nodeDAO", nodeDAO);
	}

	@GET
	@Path("/node")
	@Override
	public void checkNode(Host host) {
		NodeDAO nodeDAO = (NodeDAO) ctx.getAttribute("nodeDAO");
		if(!nodeDAO.checkNode(host))
			deleteNode(host.getAlias());
	}
	
	@GET
	@Path("/start")
	public String start() {
		return "started";
	}

}
