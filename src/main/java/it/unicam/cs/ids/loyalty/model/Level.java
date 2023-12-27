package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a level in a loyalty program.
 */
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

	@ManyToOne
	@JoinColumn(name = "loyalty_program_id")
	private LoyaltyProgram loyaltyProgram;

	/**
	 * Default constructor.
	 */
	public Level() {
	}

	/**
	 * Constructs a Level with the specified details.
	 *
	 * @param name        The name of the level.
	 * @param description The description of the level.
	 */
	public Level(String name, String description, LoyaltyProgram loyaltyProgram) {
		this.name = name;
		this.description = description;
		this.loyaltyProgram = loyaltyProgram;
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
		this.id=id;
		
	}
}