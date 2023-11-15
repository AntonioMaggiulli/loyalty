package it.unicam.cs.ids.loyalty.model;

public class Benefit {
    private int id;
    private String name;
    private String description;
    private int pointsRequired;
    private boolean earnsPoints;
    private boolean isCoupon;
    private double euroSpent;
    private Merchant offeringMerchant;
 
    public Benefit(int id, String name, String description, int pointsRequired, boolean earnsPoints, boolean isCoupon, double euroSpent, Merchant offeringMerchant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.earnsPoints = earnsPoints;
        this.isCoupon = isCoupon;
        this.euroSpent = euroSpent;
        this.offeringMerchant = offeringMerchant;
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
 
    public boolean isEarnsPoints() {
        return earnsPoints;
    }
 
    public boolean isCoupon() {
        return isCoupon;
    }
 
    public double getEuroSpent() {
        return euroSpent;
    }
 
    public Merchant getOfferingMerchant() {
        return offeringMerchant;
    }
 
    public void applyBenefit(Membership membership) {
        
        MembershipAccount loyaltyAccount = membership.getLoyaltyAccount();
 
        if (loyaltyAccount != null) {
            int pointsEarned = earnsPoints ? getPointsRequired() : 0;
            double eurosSpent = isCoupon ? 0 : getEuroSpent();
 
            Transaction transaction = new Transaction(this, pointsEarned, pointsRequired, eurosSpent);
            loyaltyAccount.addTransaction(transaction);
        }
    }
}
