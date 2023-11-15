package it.unicam.cs.ids.loyalty.model;
 
import java.util.ArrayList;
import java.util.List;
 
public class LoyaltyProgram {
    private int id;
    private String programName;
    private String description;
    private boolean isCoalition;
    private List<Merchant> merchants;
    private List<Membership> memberships;
    private List<Level> loyaltyLevels;
 
    public LoyaltyProgram(int id, String programName, String description, boolean isCoalition) {
        this.id = id;
        this.programName = programName;
        this.description = description;
        this.isCoalition = isCoalition;
        this.merchants = new ArrayList<>();
        this.memberships = new ArrayList<>();
        this.loyaltyLevels = new ArrayList<>();
    }
 
    public int getId() {
        return id;
    }
 
    public String getProgramName() {
        return programName;
    }
 
    public String getDescription() {
        return description;
    }
 
    public boolean isCoalition() {
        return isCoalition;
    }
 
    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }
 
    public void removeMerchant(Merchant merchant) {
        merchants.remove(merchant);
    }
 
    public List<Merchant> getMerchants() {
        return merchants;
    }
 
    public void createLoyaltyProgram(Merchant merchant, int programId, String programName, String programDescription, boolean isCoalition) {
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram(programId, programName, programDescription, isCoalition);
        merchant.addLoyaltyProgram(loyaltyProgram);
    }
 
    public void addMembership(Membership membership) {
        memberships.add(membership);
    }
 
    public void removeMembership(Membership membership) {
        memberships.remove(membership);
    }
 
    public List<Membership> getMemberships() {
        return memberships;
    }
 
    public void addLoyaltyLevel(Level loyaltyLevel) {
        loyaltyLevels.add(loyaltyLevel);
    }
 
    public void removeLoyaltyLevel(Level loyaltyLevel) {
        loyaltyLevels.remove(loyaltyLevel);
    }
 
    public List<Level> getLoyaltyLevels() {
        return loyaltyLevels;
    }
 
 
 
	public void addService(Benefit service) {
		// TODO Auto-generated method stub
	}
 
}
