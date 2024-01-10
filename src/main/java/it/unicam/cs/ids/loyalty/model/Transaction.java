package it.unicam.cs.ids.loyalty.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * Represents a transaction associated with a loyalty program.
 */
@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "loyaltyBenefit_id", nullable = false)
	private Benefit loyaltyBenefit;

	private int pointsEarned;
	private int pointsSpent;
	private double moneySpent;
	private LocalDateTime timestamp;

	@ManyToOne
	@JoinColumn(name = "membership_account_id")
	private MembershipAccount membershipAccount;

	public Transaction() {

	}

	public Transaction(Benefit loyaltyBenefit, double eurosSpent, MembershipAccount account) {
		this.loyaltyBenefit = loyaltyBenefit;
		this.moneySpent = eurosSpent;
		this.membershipAccount = account;
		this.timestamp = LocalDateTime.now();
	}

	/**
	 * Retrieves the ID of the transaction.
	 *
	 * @return The ID of the transaction.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retrieves the loyalty benefit associated with the transaction.
	 *
	 * @return The loyalty benefit associated with the transaction.
	 */
	public Benefit getLoyaltyBenefit() {
		return loyaltyBenefit;
	}

	/**
	 * Retrieves the points earned in the transaction.
	 *
	 * @return The points earned in the transaction.
	 */
	public int getPointsEarned() {
		return pointsEarned;
	}

	/**
	 * Retrieves the points spent in the transaction.
	 *
	 * @return The points spent in the transaction.
	 */
	public int getPointsSpent() {
		return pointsSpent;
	}

	/**
	 * Retrieves the euros spent in the transaction.
	 *
	 * @return The euros spent in the transaction.
	 */
	public double getEurosSpent() {
		return moneySpent;
	}

	/**
	 * Retrieves the timestamp of the transaction.
	 *
	 * @return The timestamp of the transaction.
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setPointsEarned(int pointsEarned) {
		this.pointsEarned = pointsEarned;

	}

	public MembershipAccount getMembershipAccount() {
		return membershipAccount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLoyaltyBenefit(Benefit loyaltyBenefit) {
		this.loyaltyBenefit = loyaltyBenefit;
	}

	public void setPointsSpent(int pointsSpent) {
		this.pointsSpent = pointsSpent;
	}

	public void setEurosSpent(double eurosSpent) {
		this.moneySpent = eurosSpent;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setMembershipAccount(MembershipAccount membershipAccount) {
		this.membershipAccount = membershipAccount;
	}
}