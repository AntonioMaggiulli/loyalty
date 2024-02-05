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

	@Column(columnDefinition = "TEXT")
	private String text;

	private String telephoneNumber;

	public Invitation() {
	}

	public Invitation(Customer sender, String friendContact, Membership membership, Date invitationDate, String text) {
		this.sender = sender;
		this.friendEmail = friendContact;
		this.invitationDate = invitationDate;
		this.text = text;
	}

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