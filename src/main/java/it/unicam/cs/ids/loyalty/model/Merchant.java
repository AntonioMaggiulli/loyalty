package it.unicam.cs.ids.loyalty.model;
import java.util.ArrayList;
import java.util.List;
 
public class Merchant {
	private int id;
	private String name;
	private String description;
	private List<LoyaltyProgram> loyaltyPrograms;
 
	public Merchant(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.loyaltyPrograms = new ArrayList<>();
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
 
	public List<LoyaltyProgram> getLoyaltyPrograms() {
		return loyaltyPrograms;
	}
 
	public void addLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
		loyaltyPrograms.add(loyaltyProgram);
	}
 
	public void removeLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
		loyaltyPrograms.remove(loyaltyProgram);
	}
 
}