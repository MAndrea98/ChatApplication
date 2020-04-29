package dao;

import java.util.ArrayList;
import java.util.List;

import models.Host;

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
	
	
}
