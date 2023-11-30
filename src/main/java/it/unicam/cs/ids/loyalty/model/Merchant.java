package it.unicam.cs.ids.loyalty.model;
import java.util.ArrayList;
import java.util.List;
 
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
 
    @OneToMany(mappedBy = "merchant")
    private List<Partnership> partnerships = new ArrayList<>();
 
    /**
     * Default constructor for JPA.
     */
    public Merchant() {
       // Default constructor for JPA
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
}