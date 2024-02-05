package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;

@Entity
public class Cashback extends Benefit {

	private double cashBackRate = 0.0;
	private double minSpent = 0.0;

	public Cashback() {
		super();
	}

	@Override
	public void applyBenefit(Transaction transaction) {
		transaction.setPointsSpent(transaction.getLoyaltyBenefit().getPointsRequired());
		//TODO
	}

	public double getCashBackRate() {
		return cashBackRate;
	}

	public void setCashBackRate(double cashBackRate) {
		this.cashBackRate = cashBackRate;
	}

	@Override
	public boolean isEligibleForRedemption(MembershipAccount account) {

		return this.getPointsRequired() <= account.getLoyaltyPoints();
	}

	public double getMinSpent() {
		return minSpent;
	}

	public void setMinSpent(double minSpent) {
		this.minSpent = minSpent;
	}

}
