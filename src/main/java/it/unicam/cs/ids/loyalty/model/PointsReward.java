package it.unicam.cs.ids.loyalty.model;

public class PointsReward extends Benefit {

	private int earnsPoints;
	private double moneySpent;


	@Override
	public void applyBenefit(Membership membership) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
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

}