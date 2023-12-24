package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;

@Entity
public class Reward extends Benefit {

	private int qtyAvailable;

	@Override
	public void applyBenefit(Membership membership) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public int getQty() {
		return qtyAvailable;
	}

	public void setQty(int qty) {
		this.qtyAvailable = qty;
	}
}
