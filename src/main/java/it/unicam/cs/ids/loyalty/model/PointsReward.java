package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "points_reward")
public class PointsReward extends Benefit {

	private int earnsPoints = 1;
	private double moneySpent = 1.0;

	public PointsReward() {

	}

	public int getEarnsPoints() {
		return earnsPoints;
	}

	public void setEarnsPoints(int earnsPoints) {
		this.earnsPoints = earnsPoints;
	}

	public double getMoneySpent() {
		return moneySpent;
	}

	public void setMoneySpent(double moneySpent) {
		this.moneySpent = moneySpent;
	}

	@Override
	public void applyBenefit(Transaction transaction) {
		if (transaction.getLoyaltyBenefit() instanceof PointsReward) {
			PointsReward pointsReward = (PointsReward) transaction.getLoyaltyBenefit();
			double eurosSpent = transaction.getEurosSpent();

			int pointsEarned = (int) (eurosSpent / pointsReward.getMoneySpent() * pointsReward.getEarnsPoints());

			transaction.setPointsEarned(pointsEarned);
		} else {
			throw new UnsupportedOperationException("Il benefit associato non è di tipo PointsReward.");
		}
	}

	@Override
	public boolean isEligibleForRedemption(MembershipAccount account) {

		return true;
	}

}