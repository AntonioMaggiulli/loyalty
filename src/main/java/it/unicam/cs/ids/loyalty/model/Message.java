package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private Merchant sender;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "message_recipients", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
	private List<Customer> recipientCustomers;

	private String text;
	private Date sentDate;

	public Message() {
	}

	public Message(Merchant sender, List<Customer> recipientCustomers, String text, Date sentDate) {
		this.sender = sender;
		this.recipientCustomers = recipientCustomers;
		this.text = text;
		this.sentDate = sentDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Merchant getSender() {
		return sender;
	}

	public void setSender(Merchant sender) {
		this.sender = sender;
	}

	public List<Customer> getRecipientCustomers() {
		return recipientCustomers;
	}

	public void setRecipientCustomers(List<Customer> recipientCustomers) {
		this.recipientCustomers = recipientCustomers;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
}
