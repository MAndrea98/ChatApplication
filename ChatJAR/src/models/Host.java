package models;

public class Host {

	private String alias;
	private String address;
	
	public Host() {
		super();
		// TODO Auto-generated constructor stub
		this.alias = "";
		this.address = "";
	}

	public Host(String alias, String address) {
		super();
		this.alias = alias;
		this.address = address;
	}

	public Host(Host host) {
		this.alias = host.alias;
		this.address = host.address;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
