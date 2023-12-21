package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class Coupon extends Benefit {

    private Date expiringDate;

    public Coupon() {
        super();
    }

    @Override
    public void applyBenefit(Membership membership) {
    	
    	//TODO
        // Implementazione specifica di come questo Coupon si applica alla Membership
    }

    public Date getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(Date expiringDate) {
        this.expiringDate = expiringDate;
    }


}
