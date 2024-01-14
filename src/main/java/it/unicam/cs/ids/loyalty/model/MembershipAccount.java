package it.unicam.cs.ids.loyalty.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import jakarta.persistence.*;

/**
 * Represents a membership account associated with a loyalty program.
 */
@Entity
public class MembershipAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "membership_id", nullable = false)
	private Membership membership;

	// questo attributo rappresenta il saldo corrente dei punti in un programma a
	// punti
	private int currentPoints = 0;

	// questo attributo segna i punti guadagnati dall'inizio della membership e
	// servirà per determinare il
	// livello di fedeltà
	private int totalPointsEarned = 0;
	@OneToMany(mappedBy = "membershipAccount", cascade = CascadeType.ALL)
	private List<Transaction> transactions;

	public MembershipAccount() {

	}

	/**
	 * Creates a new membership account.
	 *
	 * @param membership The membership associated with the account.
	 */
	public MembershipAccount(Membership membership) {
		this.membership = membership;
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
		return currentPoints;
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
		updatePoints(transaction);
	}

	public void updatePoints(Transaction transaction) {
		int pointsEarned = transaction.getPointsEarned();
		int pointsSpent = transaction.getLoyaltyBenefit().getPointsRequired();
		currentPoints += (pointsEarned - pointsSpent);
		totalPointsEarned += pointsEarned;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		this.currentPoints = loyaltyPoints;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void checkUpgradeForLevel(LoyaltyProgram loyaltyProgram) {
		List<Level> levels = new ArrayList<>(loyaltyProgram.getLevels());

		Level nextLevel = levels.stream().filter(level -> totalPointsEarned >= level.getPointsThreshold())
				.reduce((first, second) -> second).orElse(getMembership().getCurrentLevel());

		if (!nextLevel.equals(getMembership().getCurrentLevel())) {
			getMembership().setCurrentLevel(nextLevel);

		}
	}

	public int getCurrentPoints() {
		return currentPoints;
	}

	public void setCurrentPoints(int currentPoints) {
		this.currentPoints = currentPoints;
	}

	public int getTotalPointsEarned() {
		return totalPointsEarned;
	}

	public void setTotalPointsEarned(int totalPointsEarned) {
		this.totalPointsEarned = totalPointsEarned;
	}

}
