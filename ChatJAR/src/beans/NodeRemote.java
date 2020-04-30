package beans;

import java.util.ArrayList;

import javax.ejb.Remote;

import models.Host;

@Remote
public interface NodeRemote {

	public String register(Host host);
	
	public void newNode(Host host);
	
	public ArrayList<Host> allHost(Host host);
	
	public void deleteNode(String alias);
	
	public void checkNode(Host host);
}
