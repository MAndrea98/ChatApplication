package models;

import java.util.Calendar;

public class Message {

	private User sender;
	private User reciever;
	private Calendar date;
	private String subject;
	private String text;
	
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message(User sender, User reciever, Calendar date, String subject, String text) {
		super();
		this.sender = sender;
		this.reciever = reciever;
		this.date = date;
		this.subject = subject;
		this.text = text;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReciever() {
		return reciever;
	}

	public void setReciever(User reciever) {
		this.reciever = reciever;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Message [sender=" + sender + ", reciever=" + reciever + ", date=" + date + ", subject=" + subject
				+ ", text=" + text + "]";
	}
	
	
	
}
