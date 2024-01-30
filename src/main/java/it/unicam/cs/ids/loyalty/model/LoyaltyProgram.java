package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
public class LoyaltyProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String programName;
	private String description;
	private boolean isCoalition;

	@OneToMany(mappedBy = "loyaltyProgram", fetch = FetchType.EAGER)
	private List<Membership> memberships = new ArrayList<>();

	@OneToMany(mappedBy = "loyaltyProgram", fetch = FetchType.EAGER)
	private List<Level> levels = new ArrayList<>();

	@OneToMany(mappedBy = "loyaltyProgram", fetch = FetchType.EAGER)
	private List<Partnership> partnerships = new ArrayList<>();

	private LocalDate expiringDate;

	/**
	 * Default constructor.
	 */
	public LoyaltyProgram() {

	}

	public LoyaltyProgram(String programName, String description, boolean isCoalition, LocalDate expirationDate) {
		this.programName = programName;
		this.description = description;
		this.isCoalition = isCoalition;
		this.expiringDate = expirationDate;
	}

	public void addLevel(Level level) {
		this.levels.add(level);
		sortLevels();
	}

	public void sortLevels() {
		levels.sort(Comparator.comparingInt(Level::getPointsThreshold));
	}

	public void addMembership(Membership membership) {
		if (membership != null) {
			this.memberships.add(membership);
			membership.setLoyaltyProgram(this);
		}
	}

	public void addPartnership(Partnership partnership) {
		if (partnership != null) {
			this.partnerships.add(partnership);
			partnership.setLoyaltyProgram(this);
		}
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public List<Level> getLevels() {
		return levels;
	}

	public List<Membership> getMemberships() {
		return memberships;
	}

	public List<Partnership> getPartnerships() {
		return partnerships;
	}

	public String getProgramName() {
		return programName;
	}

	public boolean isCoalition() {
		return isCoalition;
	}

	public void removeLevel(Level level) {
		this.levels.remove(level);
	}

	public void removeMembership(Membership membership) {
		this.memberships.remove(membership);
	}

	public void removePartnership(Partnership partnership) {
		this.partnerships.remove(partnership);
	}

	public void setCoalition(boolean coalition) {
		isCoalition = coalition;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}

	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	public void setPartnerships(List<Partnership> partnerships) {
		this.partnerships = partnerships;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public LocalDate getExpiringDate() {
		return expiringDate;
	}

	public void setExpiringDate(LocalDate expiringDate) {
		this.expiringDate = expiringDate;
	}

	public Membership enrollCustomer(Customer customer, Level level) {
		Membership newMembership = new Membership(customer, this);
		newMembership.setCurrentLevel(level);
		return newMembership;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoyaltyProgram other = (LoyaltyProgram) obj;
		return id == other.id;
	}

}