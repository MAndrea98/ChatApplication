package beans;

import java.net.InetAddress;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import dao.NodeDAO;
import dao.UserDAO;
import models.Host;

@Startup
@Singleton
@Path("")
@LocalBean
public class NodeBean implements NodeRemote, NodeLocal {
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/mojQueue")
	private Queue queue;
	
	private static int incrementerN;
	
	private NodeDAO nodeDAO;
	
	private Host localHost;
	
	@EJB
	ChatBean chatBean;
	
	@PostConstruct
	public void init() throws Exception {
		if (nodeDAO == null) {
			nodeDAO = new NodeDAO();
		}
        
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println(inetAddress.getHostAddress());
		if (nodeDAO.getAllHosts().size() == 0) {
            Host host = new Host("master", inetAddress.getHostAddress());
            nodeDAO.getAllHosts().add(host);
            System.out.println(inetAddress.getHostAddress());
            localHost = host;
		}
		
        if (!nodeDAO.findByIp(inetAddress.getHostAddress())) {
        	String alias = "Node" + incrementerN++;
        	Host host = new Host(alias, inetAddress.getHostAddress());
        	localHost = host;
        	ResteasyClient client = new ResteasyClientBuilder().build();
        	String http = "http://"+nodeDAO.getMasterIP()+":8080/ChatWAR/rest/register";
        	System.out.println(http);
        	ResteasyWebTarget target = client.target(http);
        	Response response = target.request().post(Entity.entity(host, "application/json"));
        	String ret = response.readEntity(String.class);
        	System.out.println(ret);
        	
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String register(Host host) {
		nodeDAO.getAllHosts().add(host);
		ArrayList<Host> noMaster = nodeDAO.getAllNoMaster();
		for (Host h : noMaster) {
			try {
				ResteasyClient client = new ResteasyClientBuilder().build();
				String http = "http://"+h.getAddress()+":8080/ChatWAR/rest/node";
	        	System.out.println(http);
	        	ResteasyWebTarget target = client.target(http);
	        	Response response = target.request().post(Entity.entity(host, "application/json"));
	        	System.out.println(response);
			} catch(Exception e) {
				try {
					ResteasyClient client = new ResteasyClientBuilder().build();
					String http = "http://"+h.getAddress()+":8080/ChatWAR/rest/node";
		        	System.out.println(http);
		        	ResteasyWebTarget target = client.target(http);
		        	Response response = target.request().post(Entity.entity(host, "application/json"));
		        	System.out.println(response);
				} catch (Exception  ex) {
					ResteasyClient client = new ResteasyClientBuilder().build();
		    		String http = "http://"+h.getAddress()+":8080/ChatWAR/rest/node/" + h.getAlias();
		        	System.out.println(http);
		        	ResteasyWebTarget target = client.target(http);
		        	target.request().delete();
				}
			}
			
		}
	
		try {
			ResteasyClient client = new ResteasyClientBuilder().build();
			String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/nodes";
	    	System.out.println(http);
	    	ResteasyWebTarget target = client.target(http);
	    	Response response = target.request().post(Entity.entity(host, "application/json"));
	    	System.out.println(response);
		} catch(Exception e) {
			try {
				ResteasyClient client = new ResteasyClientBuilder().build();
				String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/nodes";
		    	System.out.println(http);
		    	ResteasyWebTarget target = client.target(http);
		    	Response response = target.request().post(Entity.entity(host, "application/json"));
		    	System.out.println(response);
			} catch (Exception  ex) {
				ResteasyClient client = new ResteasyClientBuilder().build();
	    		String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/node/" + host.getAlias();
	        	System.out.println(http);
	        	ResteasyWebTarget target = client.target(http);
	        	target.request().delete();
			}
		}
		
		
    	
    	try {
    		ResteasyClient client = new ResteasyClientBuilder().build();
    		String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/chat/loggedIn";
        	System.out.println(http);
        	ResteasyWebTarget target = client.target(http);
        	Response response = target.request().get();
    		System.out.println(response);
    	} catch (Exception e) {
    		try {
    			ResteasyClient client = new ResteasyClientBuilder().build();
        		String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/chat/loggedIn";
            	System.out.println(http);
            	ResteasyWebTarget target = client.target(http);
            	Response response = target.request().get();
        		System.out.println(response);
    		} catch (Exception ex) {
    			ResteasyClient client = new ResteasyClientBuilder().build();
	    		String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/node/" + host.getAlias();
	        	System.out.println(http);
	        	ResteasyWebTarget target = client.target(http);
	        	target.request().delete();
    		}
    		
    	}
		return "OK";
	}

	@POST
	@Path("/node")
	@Override
	public String newNode(Host host) {
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText("New node ("+host.getAlias()+"["+host.getAddress()+"]) has been started");
			sender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "New node ("+host.getAlias()+"["+host.getAddress()+"]) has been started";
	}

	@POST
	@Path("/nodes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public ArrayList<Host> allHost(Host newHost) {
		return nodeDAO.getAllExceptNew(newHost);
	}

	@DELETE
	@Path("/node/{alias}")
	@Override
	public void deleteNode(@PathParam("alias") String alias) {
		System.out.println("##############");
		UserDAO userDAO = (UserDAO) chatBean.getCtx();
		nodeDAO.deleteByAlias(alias, userDAO);
	}
	
	//@Schedule(hour = "*", minute = "*", second = "*/100", persistent = false)
	public void timerCheckNodes() {
		for (Host host : nodeDAO.getAllHosts()) {
			try {
	        	ResteasyClient client = new ResteasyClientBuilder().build();
	        	String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/node";
	        	System.out.println(http);
	            ResteasyWebTarget target = client.target(http);
	            Response response = target.request().get();
	            String ret = response.readEntity(String.class);
	    		System.out.println(ret);
			} catch (Exception e) {
				ResteasyClient client = new ResteasyClientBuilder().build();
	    		String http = "http://"+host.getAddress()+":8080/ChatWAR/rest/node/" + host.getAlias();
	        	System.out.println(http);
	        	ResteasyWebTarget target = client.target(http);
	        	 Response response = target.request().delete();
			}
		}
	}

	@GET
	@Path("/node")
	@Override
	public String checkNode() {
		return "Still alive";
	}
	
	
	@PreDestroy
	public void preDestroy() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		String http = "http://"+localHost.getAddress()+":8080/ChatWAR/rest/node/" + localHost.getAlias();
    	System.out.println(http);
    	ResteasyWebTarget target = client.target(http);
    	target.request().delete();
	}
	
	@GET
	@Path("/start")
	public String start() {
		return "started";
	}

	

}
