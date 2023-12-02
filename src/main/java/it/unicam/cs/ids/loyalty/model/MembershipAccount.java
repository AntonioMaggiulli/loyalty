package it.unicam.cs.ids.loyalty.model;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
/**
 * Represents a membership account associated with a loyalty program.
 */
@Entity
public class MembershipAccount {
    public void setId(Long id) {
		this.id = id;
	}
	public void setMembership(Membership membership) {
		this.membership = membership;
	}
	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;
    private int loyaltyPoints;
    @OneToMany(mappedBy = "membershipAccount", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
    /**
     * Creates a new membership account.
     *
     * @param membership The membership associated with the account.
     */
    public MembershipAccount(Membership membership) {
       this.membership = membership;
       this.loyaltyPoints = 0;
       this.transactions = new ArrayList<>();
    }
    /**
     * Retrieves the ID of the membership account.
     *
     * @return The ID of the membership account.
     */
    public Long getId() {
       return id;
    }
    /**
     * Retrieves the membership associated with the account.
     *
     * @return The membership associated with the account.
     */
    public Membership getMembership() {
       return membership;
    }
    /**
     * Retrieves the loyalty points of the membership account.
     *
     * @return The loyalty points of the membership account.
     */
    public int getLoyaltyPoints() {
       return loyaltyPoints;
    }
    /**
     * Retrieves the list of transactions associated with the membership account.
     *
     * @return The list of transactions associated with the membership account.
     */
    public List<Transaction> getTransactions() {
       return transactions;
    }
    /**
     * Adds a transaction to the list of transactions and updates loyalty points.
     *
     * @param transaction The transaction to be added.
     */
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
 