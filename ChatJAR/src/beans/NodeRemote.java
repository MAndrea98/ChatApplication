package beans;

import java.util.ArrayList;

import javax.ejb.Remote;

import models.Host;

@Remote
public interface NodeRemote {

	public Host register(Host host);
	
	public void newNode(Host host);
	
	public ArrayList<Host> allHost();
	
	public void deleteNode(String alias);
	
	public void checkNode(Host host);
}
