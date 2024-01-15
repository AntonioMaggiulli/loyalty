package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class Coupon extends Benefit {

	private String code;
	private String conditionString;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getConditionString() {
		return conditionString;
	}

	public void setConditionString(String conditionString) {
		this.conditionString = conditionString;
	}

	private Date expiringDate;

	public Coupon() {
		super();
	}

	@Override
	public void applyBenefit(Transaction transaction) {
		//non implementato
		// TODO
	}

	public Date getExpiringDate() {
		return expiringDate;
	}

	public void setExpiringDate(Date expiringDate) {
		this.expiringDate = expiringDate;
	}

	@Override
	public boolean isEligibleForRedemption(MembershipAccount account) {

		return this.expiringDate.after(new Date()) && this.getPointsRequired() <= account.getLoyaltyPoints();
	}

}
