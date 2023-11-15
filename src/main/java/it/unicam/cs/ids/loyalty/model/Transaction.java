package it.unicam.cs.ids.loyalty.model;
import java.time.LocalDateTime;

public class Transaction {
	private static int idCounter = 0;
	private int id;
	private Benefit loyaltyBenefit;
	private int pointsEarned;
	private int pointsSpent;
	private double eurosSpent;
	private LocalDateTime timestamp;
 
	public Transaction(Benefit loyaltyBenefit, int pointsEarned, int pointsSpent, double eurosSpent) {
		this.id = generateUniqueId();
		this.loyaltyBenefit = loyaltyBenefit;
		this.pointsEarned = pointsEarned;
		this.pointsSpent = pointsSpent;
		this.eurosSpent = eurosSpent;
		this.timestamp = LocalDateTime.now();
	}
 
	private static int generateUniqueId() {
		idCounter++;
		return idCounter;
	}
 
	public int getId() {
		return id;
	}
 
	public Benefit getLoyaltyBenefit() {
		return loyaltyBenefit;
	}
 
	public int getPointsEarned() {
		return pointsEarned;
	}
 
	public int getPointsSpent() {
		return pointsSpent;
	}
 
	public double getEurosSpent() {
		return eurosSpent;
	}
 
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
 
}
