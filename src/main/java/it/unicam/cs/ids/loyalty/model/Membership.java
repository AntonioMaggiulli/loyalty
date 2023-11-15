package it.unicam.cs.ids.loyalty.model;
public class Membership {
	private int id;
	private Customer customer;
	private LoyaltyProgram loyaltyProgram;
	private MembershipAccount membershipAccount;
 
	public Membership(int id, Customer customer, LoyaltyProgram loyaltyProgram) {
		this.id = id;
		this.customer = customer;
		this.loyaltyProgram = loyaltyProgram;
		this.membershipAccount = new MembershipAccount(id, this);
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
 
	public MembershipAccount getLoyaltyAccount() {
		return membershipAccount;
	}
 
}
