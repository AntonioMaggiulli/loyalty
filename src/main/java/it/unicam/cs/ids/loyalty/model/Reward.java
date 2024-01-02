package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;

@Entity
public class Reward extends Benefit {

	private int qtyAvailable;

	@Override
	public void applyBenefit(Transaction transaction) {
		// operazioni specifiche
		throw new UnsupportedOperationException();
	}

	public int getQty() {
		return qtyAvailable;
	}

	public void setQty(int qty) {
		this.qtyAvailable = qty;
	}
}
