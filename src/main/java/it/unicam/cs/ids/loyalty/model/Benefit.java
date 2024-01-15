package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;

/**
 * Represents a benefit in the loyalty program.
 */
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
	private boolean exclusiveRequired;

	/**
	 * Default constructor.
	 */
	public Benefit() {
	}

	/**
	 * Constructs a Benefit with the specified details.
	 *
	 * @param name             The name of the benefit.
	 * @param description      The description of the benefit.
	 * @param pointsRequired   The points required for the benefit.
	 * @param offeringMerchant The merchant offering the benefit.
	 * @param loyaltyProgram   The loyalty program associated with the benefit.
	 */
	public Benefit(String type, String name, String description, int pointsRequired, Merchant offeringMerchant,
			LoyaltyProgram loyaltyProgram) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.pointsRequired = pointsRequired;
		this.offeringMerchant = offeringMerchant;
		this.loyaltyProgram = loyaltyProgram;
	}

	/**
	 * Retrieves the ID of the benefit.
	 *
	 * @return The ID of the benefit.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retrieves the name of the benefit.
	 *
	 * @return The name of the benefit.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the description of the benefit.
	 *
	 * @return The description of the benefit.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Retrieves the points required for the benefit.
	 *
	 * @return The points required for the benefit.
	 */
	public int getPointsRequired() {
		return pointsRequired;
	}

	/**
	 * Retrieves the amount of euros spent for the benefit.
	 *
	 * @return The amount of euros spent for the benefit.
	 */

	/**
	 * Retrieves the merchant offering the benefit.
	 *
	 * @return The merchant offering the benefit.
	 */
	public Merchant getOfferingMerchant() {
		return offeringMerchant;
	}

	/**
	 * Retrieves the loyalty program associated with the benefit.
	 *
	 * @return The loyalty program associated with the benefit.
	 */
	public LoyaltyProgram getLoyaltyProgram() {
		return loyaltyProgram;
	}

	/**
	 * Sets the loyalty program associated with the benefit.
	 *
	 * @param loyaltyProgram The loyalty program to set.
	 */
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
