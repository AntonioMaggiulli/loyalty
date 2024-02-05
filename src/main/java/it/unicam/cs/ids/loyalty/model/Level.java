package it.unicam.cs.ids.loyalty.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Level {

	public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
		this.loyaltyProgram = loyaltyProgram;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String description;
	private int pointsThreshold; // Soglia minima di punti per passare a questo livello

	@ManyToOne
	@JoinColumn(name = "loyalty_program_id")
	private LoyaltyProgram loyaltyProgram;

	public Level() {
	}

	public Level(String name, String description, LoyaltyProgram loyaltyProgram, int threshold) {
		this.name = name;
		this.description = description;
		this.loyaltyProgram = loyaltyProgram;
		this.pointsThreshold = threshold;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;

	}

	public int getPointsThreshold() {
		return pointsThreshold;
	}

	public void setPointsThreshold(int pointsThreshold) {
		this.pointsThreshold = pointsThreshold;
	}

	public LoyaltyProgram getLoyaltyProgram() {
		return loyaltyProgram;
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
		Level other = (Level) obj;
		return id == other.id;
	}
}