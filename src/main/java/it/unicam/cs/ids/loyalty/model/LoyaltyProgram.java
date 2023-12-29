package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a loyalty program offered by Merchants.
 */
@Entity
public class LoyaltyProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String programName;
	private String description;
	private boolean isCoalition;

	@OneToMany(mappedBy = "loyaltyProgram")
	private List<Membership> memberships = new ArrayList<>();

	@OneToMany(mappedBy = "loyaltyProgram", fetch = FetchType.EAGER)
	private List<Level> levels = new ArrayList<>();

	@OneToMany(mappedBy = "loyaltyProgram", fetch = FetchType.EAGER)
	private List<Partnership> partnerships = new ArrayList<>();

	private Date expiringDate;

	/**
	 * Default constructor.
	 */
	public LoyaltyProgram() {

	}

	/**
	 * Constructs a LoyaltyProgram with the specified details.
	 *
	 * @param programName The name of the loyalty program.
	 * @param description A description of the loyalty program.
	 * @param isCoalition Indicates if the program is a coalition of multiple
	 *                    merchants.
	 */
	public LoyaltyProgram(String programName, String description, boolean isCoalition) {
		this.programName = programName;
		this.description = description;
		this.isCoalition = isCoalition;
	}

	/**
	 * Adds a level to the loyalty program's list of levels.
	 *
	 * @param level The level to add.
	 */
	public void addLevel(Level level) {
		this.levels.add(level);
	}

	/**
	 * Adds a membership to the loyalty program's list of memberships.
	 *
	 * @param membership The membership to add.
	 */
	public void addMembership(Membership membership) {
		if (membership != null) {
			this.memberships.add(membership);
			membership.setLoyaltyProgram(this);
		}
	}

	/**
	 * Adds a partnership to the loyalty program's list of partnerships.
	 *
	 * @param partnership The partnership to add.
	 */
	public void addPartnership(Partnership partnership) {
		if (partnership != null) {
			this.partnerships.add(partnership);
			partnership.setLoyaltyProgram(this);
		}
	}

	/**
	 * Gets the description of the loyalty program.
	 *
	 * @return The description of the loyalty program.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the ID of the loyalty program.
	 *
	 * @return The ID of the loyalty program.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the list of levels associated with the loyalty program.
	 *
	 * @return The list of levels.
	 */
	public List<Level> getLevels() {
		return levels;
	}

	/**
	 * Gets the list of memberships associated with the loyalty program.
	 *
	 * @return The list of memberships.
	 */
	public List<Membership> getMemberships() {
		return memberships;
	}

	/**
	 * Gets the list of partnerships associated with the loyalty program.
	 *
	 * @return The list of partnerships.
	 */
	public List<Partnership> getPartnerships() {
		return partnerships;
	}

	/**
	 * Gets the name of the loyalty program.
	 *
	 * @return The name of the loyalty program.
	 */
	public String getProgramName() {
		return programName;
	}

	/**
	 * Checks if the loyalty program is a coalition of multiple merchants.
	 *
	 * @return True if the program is a coalition, false otherwise.
	 */
	public boolean isCoalition() {
		return isCoalition;
	}

	/**
	 * Removes a level from the loyalty program's list of levels.
	 *
	 * @param level The level to remove.
	 */
	public void removeLevel(Level level) {
		this.levels.remove(level);
	}

	/**
	 * Removes a membership from the loyalty program's list of memberships.
	 *
	 * @param membership The membership to remove.
	 */
	public void removeMembership(Membership membership) {
		this.memberships.remove(membership);
	}

	/**
	 * Removes a partnership from the loyalty program's list of partnerships.
	 *
	 * @param partnership The partnership to remove.
	 */
	public void removePartnership(Partnership partnership) {
		this.partnerships.remove(partnership);
	}

	/**
	 * Sets whether the loyalty program is a coalition of multiple merchants.
	 *
	 * @param coalition True if the program is a coalition, false otherwise.
	 */
	public void setCoalition(boolean coalition) {
		isCoalition = coalition;
	}

	/**
	 * Sets the description of the loyalty program.
	 *
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the ID of the loyalty program.
	 *
	 * @param id The ID to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the list of levels associated with the loyalty program.
	 *
	 * @param levels The list of levels to set.
	 */
	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}

	/**
	 * Sets the list of memberships associated with the loyalty program.
	 *
	 * @param memberships The list of memberships to set.
	 */
	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	/**
	 * Sets the list of partnerships associated with the loyalty program.
	 *
	 * @param partnerships The list of partnerships to set.
	 */
	public void setPartnerships(List<Partnership> partnerships) {
		this.partnerships = partnerships;
	}

	/**
	 * Sets the name of the loyalty program.
	 *
	 * @param programName The name to set.
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Membership enrollCustomer(Customer customer, Level level) {
		Membership newMembership = new Membership(customer, this);
		newMembership.setCurrentLevel(level);
		return newMembership;
	}
}