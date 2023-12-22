package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;

/**
 * Represents a membership in a loyalty program.
 */
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
	
	@OneToOne(mappedBy = "membership", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private MemberCard memberCard;

	
	@ManyToOne
	@JoinColumn(name="level_id", nullable = false)
	private Level currentLevel ;
	/*
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "invitation_id")
    private Invitation invitation;*/

	/**
	 * Creates a new membership.
	 *
	 * @param customer       The customer associated with the membership.
	 * @param loyaltyProgram The loyalty program associated with the membership.
	 */
	public Membership(Customer customer, LoyaltyProgram loyaltyProgram) {
		this.customer = customer;
		this.loyaltyProgram = loyaltyProgram;
		this.account = new MembershipAccount(this);
	}

	public Membership() {
		// Costruttore vuoto necessario per Hibernate
	}

	/**
	 * Retrieves the ID of the membership.
	 *
	 * @return The ID of the membership.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retrieves the customer associated with the membership.
	 *
	 * @return The customer associated with the membership.
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Retrieves the loyalty program associated with the membership.
	 *
	 * @return The loyalty program associated with the membership.
	 */
	public LoyaltyProgram getLoyaltyProgram() {
		return loyaltyProgram;
	}

	/**
	 * Retrieves the membership account associated with the membership.
	 *
	 * @return The membership account associated with the membership.
	 */
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
}