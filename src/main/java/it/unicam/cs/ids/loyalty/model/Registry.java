package it.unicam.cs.ids.loyalty.model;
import java.util.ArrayList;
import java.util.List;
 
public class Registry {
	private static Registry instance;
 
	private List<LoyaltyProgram> loyaltyPrograms;
	private List<Benefit> availableServices;
	private List<Level> availableServiceLevels;
 
	private Registry() {
		loyaltyPrograms = new ArrayList<>();
		availableServices = new ArrayList<>();
		availableServiceLevels = new ArrayList<>();
	}
 
	public static Registry getInstance() {
		if (instance == null) {
			instance = new Registry();
		}
		return instance;
	}
 
	public void createLoyaltyProgram(Merchant partner, String programName, String programDescription,
			boolean coalizione) {
		LoyaltyProgram loyaltyProgram = new LoyaltyProgram(0, programName, programDescription, coalizione);
 
		loyaltyProgram.addProgramPartner(partner);
		loyaltyPrograms.add(loyaltyProgram);
	}
 
	public void addServiceToLoyaltyProgram(LoyaltyProgram loyaltyProgram, Benefit service) {
		loyaltyProgram.addService(service);
		availableServices.add(service);
	}
 
	public void addServiceLevelToLoyaltyProgram(LoyaltyProgram loyaltyProgram, Level serviceLevel) {
		loyaltyProgram.addServiceLevel(serviceLevel);
		availableServiceLevels.add(serviceLevel);
	}
 
	public List<LoyaltyProgram> getLoyaltyPrograms() {
		return loyaltyPrograms;
	}
}
