package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;

@Entity
public class Reward extends Benefit {

	private int qtyAvailable;

	@Override
	public void applyBenefit(Transaction transaction) {
		transaction.setPointsSpent(transaction.getLoyaltyBenefit().getPointsRequired());
		qtyAvailable--;

	}

	public int getQty() {
		return qtyAvailable;
	}

	public void setQty(int qty) {
		this.qtyAvailable = qty;
	}
	
	@Override
	public boolean isEligibleForRedemption(MembershipAccount account) {

		return this.qtyAvailable > 0 && this.getPointsRequired() <= account.getLoyaltyPoints();
	}

}
