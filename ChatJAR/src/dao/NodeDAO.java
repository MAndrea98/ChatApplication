package dao;

import java.util.ArrayList;
import java.util.List;

import models.Host;
import models.User;

public class NodeDAO {

	private List<Host> allHosts = new ArrayList<Host>();

	public NodeDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Host> getAllHosts() {
		return allHosts;
	}

	public void setAllHosts(List<Host> allHosts) {
		this.allHosts = allHosts;
	}

	public ArrayList<Host> getAllExceptNew(Host newHost) {
		ArrayList<Host> allHost = new ArrayList<Host>();
		for(Host host : allHosts) {
			if(host.getAlias().equals(newHost.getAlias())) {
				allHost.add(host);
			}
		}
		return allHost;
	}

	public void deleteByAlias(String alias, UserDAO userDAO) {
		for (Host host : allHosts) {
			if(host.getAlias().equals(alias)) {
				allHosts.remove(host);
			}
		}
		
		for (User user : userDAO.getAllUsers()) {
			if(user.getHost().getAlias().equals(alias)) {
				userDAO.getAllUsers().remove(user);
			}
		}
	}
	
	public boolean checkNode(Host host) {
		return allHosts.contains(host);
	}
	
}
