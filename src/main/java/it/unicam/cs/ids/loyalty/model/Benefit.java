package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;

@Entity
public abstract class Benefit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String type;
	private String name;
	private String description;

	@ManyToOne
	private LoyaltyProgram loyaltyProgram;

	@ManyToOne
	private Merchant offeringMerchant;

	@ManyToOne
	@JoinColumn(name = "associated_level_id")
	private Level associatedLevel;
	private int pointsRequired = 0;

	public Benefit() {
	}

	public Benefit(String type, String name, String description, int pointsRequired, Merchant offeringMerchant,
			LoyaltyProgram loyaltyProgram) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.pointsRequired = pointsRequired;
		this.offeringMerchant = offeringMerchant;
		this.loyaltyProgram = loyaltyProgram;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getPointsRequired() {
		return pointsRequired;
	}

	public Merchant getOfferingMerchant() {
		return offeringMerchant;
	}

	public LoyaltyProgram getLoyaltyProgram() {
		return loyaltyProgram;
	}

	public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
		this.loyaltyProgram = loyaltyProgram;
	}

	/**
	 * Applies the benefit to the membership, earning points or adding a transaction
	 * to the loyalty account.
	 *
	 * @param membership The membership to apply the benefit to.
	 */
	public abstract void applyBenefit(Transaction transaction);

	public abstract boolean isEligibleForRedemption(MembershipAccount account);

	public Level getAssociatedLevel() {
		return associatedLevel;
	}

	public void setAssociatedLevel(Level associatedLevel) {
		this.associatedLevel = associatedLevel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPointsRequired(int pointsRequired) {
		this.pointsRequired = pointsRequired;
	}

	public void setOfferingMerchant(Merchant offeringMerchant) {
		this.offeringMerchant = offeringMerchant;
	}
}
