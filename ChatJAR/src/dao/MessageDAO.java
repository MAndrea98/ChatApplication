package dao;

import java.util.ArrayList;
import java.util.List;

import models.Message;


public class MessageDAO {

	private List<Message> allMessages = new ArrayList<Message>();

	public MessageDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Message> getAllMessages() {
		return allMessages;
	}

	public void setAllMessages(List<Message> allMessages) {
		this.allMessages = allMessages;
	}
	
	
	public ArrayList<Message> findByUser(String username) {
		ArrayList<Message> messages = new ArrayList<Message>();
		for (Message m : allMessages) {
			if (m.getSender().getUsername().equals(username) || 
				m.getReciever().getUsername().equals(username) ||
				m.getReciever().getUsername() == "null") {
				messages.add(m);
			}
		}
		return messages;
	}
	
	public void addMessage(Message message) {
		allMessages.add(message);
	}
	
}
