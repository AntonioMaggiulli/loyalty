package it.unicam.cs.ids.loyalty.model;
import java.util.ArrayList;
import java.util.List;
 
public class MembershipAccount {
	private int id;
	private Membership membership;
	private int loyaltyPoints;
	private List<Transaction> transactions;
 
	public MembershipAccount(int id, Membership membership) {
		this.id = id;
		this.membership = membership;
		this.loyaltyPoints = 0;
		this.transactions = new ArrayList<>();
	}
 
	public int getId() {
		return id;
	}
 
	public Membership getMembership() {
		return membership;
	}
 
	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}
 
	public List<Transaction> getTransactions() {
		return transactions;
	}
 
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
		updateLoyaltyPoints(transaction);
	}
 
	private void updateLoyaltyPoints(Transaction transaction) {
 
		int pointsEarned = transaction.getPointsEarned();
		int pointsSpent = transaction.getPointsSpent();
 
		loyaltyPoints += (pointsEarned - pointsSpent);
	}
 
}