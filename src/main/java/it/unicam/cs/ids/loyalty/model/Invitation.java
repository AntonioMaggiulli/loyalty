package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Invitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private Customer sender;

	private String friendEmail;
	private Date invitationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "membership_id")
	private Membership membership;

	private String telephoneNumber;

	public Invitation() {
		// Costruttore vuoto per JPA
	}

	public Invitation(Customer sender, String friendEmail, Date invitationDate, Membership membership) {
		this.sender = sender;
		this.friendEmail = friendEmail;
		this.invitationDate = invitationDate;
		this.membership = membership;
	}

	// Getters and Setters
	public Customer getSender() {
		return sender;
	}

	public void setSender(Customer sender) {
		this.sender = sender;
	}

	public String getRecipientEmail() {
		return friendEmail;
	}

	public void setRecipientEmail(String friendEmail) {
		this.friendEmail = friendEmail;
	}

	public Date getInvitationDate() {
		return invitationDate;
	}

	public void setInvitationDate(Date invitationDate) {
		this.invitationDate = invitationDate;
	}

	public Membership getMembership() {
		return membership;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFriendEmail() {
		return friendEmail;
	}

	public void setFriendEmail(String friendEmail) {
		this.friendEmail = friendEmail;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
}