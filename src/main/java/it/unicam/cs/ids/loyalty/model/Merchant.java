package it.unicam.cs.ids.loyalty.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Represents a merchant participating in loyalty programs.
 */
@Entity
public class Merchant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "merchant")
	private List<Partnership> partnerships = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "merchant")
	private List<Employee> employees = new ArrayList<>();

	/**
	 * Default constructor for JPA.
	 */
	public Merchant() {
	}

	/**
	 * Constructs a merchant with the specified details.
	 *
	 * @param name        The name of the merchant.
	 * @param description The description of the merchant.
	 */
	public Merchant(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Returns the unique identifier of the merchant.
	 *
	 * @return The merchant's ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the merchant.
	 *
	 * @return The merchant's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the merchant.
	 *
	 * @return The merchant's description.
	 */
	public String getDescription() {
		return description;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	/**
	 * Returns the list of partnerships associated with the merchant.
	 *
	 * @return The list of partnerships.
	 */
	public List<Partnership> getPartnerships() {
		return partnerships;
	}

	/**
	 * Adds a partnership to the merchant.
	 *
	 * @param partnership The partnership to add.
	 */
	public void addPartnership(Partnership partnership) {
		partnerships.add(partnership);
	}

	/**
	 * Removes a partnership from the merchant.
	 *
	 * @param partnership The partnership to remove.
	 */
	public void removePartnership(Partnership partnership) {
		partnerships.remove(partnership);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPartnerships(List<Partnership> partnerships) {
		this.partnerships = partnerships;
	}

	public void addEmployee(Employee employee) {
		employees.add(employee);
		employee.setMerchant(this);
	}

	public void removeEmployee(Employee employee) {
		employees.remove(employee);
		employee.setMerchant(null);
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
		Merchant other = (Merchant) obj;
		return id == other.id;
	}
}