package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;

@Entity
public class Membership {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "loyalty_program_id", nullable = false)
	private LoyaltyProgram loyaltyProgram;

	@OneToOne(mappedBy = "membership", cascade = CascadeType.ALL)
	private MembershipAccount account;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@OneToOne(mappedBy = "membership", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private MemberCard memberCard;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "level_id", nullable = false)
	private Level currentLevel;

	@OneToOne
	private Invitation receivedInvitation;

	private boolean exclusive;

	public Membership(Customer customer, LoyaltyProgram loyaltyProgram) {
		this.customer = customer;
		this.loyaltyProgram = loyaltyProgram;
		this.account = new MembershipAccount(this);
	}

	public Membership() {
	}

	public int getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public LoyaltyProgram getLoyaltyProgram() {
		return loyaltyProgram;
	}

	public MembershipAccount getMembershipAccount() {
		return account;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
		this.loyaltyProgram = loyaltyProgram;
	}

	public void setMembershipAccount(MembershipAccount membershipAccount) {
		this.account = membershipAccount;
	}

	public MemberCard getMemberCard() {
		return memberCard;
	}

	public void setMemberCard(MemberCard memberCard) {
		this.memberCard = memberCard;
		if (memberCard != null && memberCard.getMembership() != this) {
			memberCard.setMembership(this);
		}
	}

	public MembershipAccount getAccount() {
		return account;
	}

	public void setAccount(MembershipAccount account) {
		this.account = account;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	public Invitation getReceivedInvitation() {
		return receivedInvitation;
	}

	public void setReceivedInvitation(Invitation receivedInvitation) {
		this.receivedInvitation = receivedInvitation;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}
}