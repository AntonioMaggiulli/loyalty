package it.unicam.cs.ids.loyalty.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

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

	public Merchant() {
	}

	public Merchant(String name, String description) {
		this.name = name;
		this.description = description;
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

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Partnership> getPartnerships() {
		return partnerships;
	}

	public void addPartnership(Partnership partnership) {
		partnerships.add(partnership);
	}

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