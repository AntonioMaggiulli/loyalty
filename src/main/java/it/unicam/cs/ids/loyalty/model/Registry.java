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
 

}
