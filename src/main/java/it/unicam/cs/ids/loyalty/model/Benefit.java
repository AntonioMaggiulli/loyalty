package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.*;

/**
 * Represents a benefit in the loyalty program.
 */
@Entity
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int pointsRequired;
    private boolean earnsPoints;
    private boolean isCoupon;
    private double euroSpent;

    @ManyToOne
    private Merchant offeringMerchant;

    @ManyToOne
    private LoyaltyProgram loyaltyProgram;

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
     * @param earnsPoints      Indicates if the benefit earns points.
     * @param isCoupon         Indicates if the benefit is a coupon.
     * @param euroSpent        The amount of euros spent for the benefit.
     * @param offeringMerchant The merchant offering the benefit.
     * @param loyaltyProgram   The loyalty program associated with the benefit.
     */
    public Benefit(String name, String description, int pointsRequired, boolean earnsPoints, boolean isCoupon,
            double euroSpent, Merchant offeringMerchant, LoyaltyProgram loyaltyProgram) {
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.earnsPoints = earnsPoints;
        this.isCoupon = isCoupon;
        this.euroSpent = euroSpent;
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
     * Checks if the benefit earns points.
     *
     * @return True if the benefit earns points, false otherwise.
     */
    public boolean isEarnsPoints() {
        return earnsPoints;
    }

    /**
     * Checks if the benefit is a coupon.
     *
     * @return True if the benefit is a coupon, false otherwise.
     */
    public boolean isCoupon() {
        return isCoupon;
    }

    /**
     * Retrieves the amount of euros spent for the benefit.
     *
     * @return The amount of euros spent for the benefit.
     */
    public double getEuroSpent() {
        return euroSpent;
    }

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
     * Applies the benefit to the membership, earning points or adding a transaction to the loyalty account.
     *
     * @param membership The membership to apply the benefit to.
     */
    public void applyBenefit(Membership membership) {
        MembershipAccount loyaltyAccount = membership.getMembershipAccount();

        if (loyaltyAccount != null) {
            int pointsEarned = earnsPoints ? getPointsRequired() : 0;
            double eurosSpent = isCoupon ? 0 : getEuroSpent();

            Transaction transaction = new Transaction(this, pointsEarned, pointsRequired, eurosSpent);
            loyaltyAccount.addTransaction(transaction);
        }
    }
}

