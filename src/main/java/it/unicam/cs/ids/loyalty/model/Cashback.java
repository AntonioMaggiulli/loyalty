package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;

@Entity
public class Cashback extends Benefit {

    private double cashBackRate;

    public Cashback() {
        super();
    }

    @Override
    public void applyBenefit(Membership membership) {
    	
    	//TODO
        // Implementazione specifica di come questo Cashback si applica alla Membership
    }

    public double getCashBackRate() {
        return cashBackRate;
    }

    public void setCashBackRate(double cashBackRate) {
        this.cashBackRate = cashBackRate;
    }

}