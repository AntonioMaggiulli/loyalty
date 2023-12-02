package it.unicam.cs.ids.loyalty.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * JPA Entity representing a member card.
 */
@Entity
public class MemberCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String numero;

    @ManyToOne
    private MembershipAccount conto;

    @ManyToOne
    private Customer customer;

    // Default constructor required by JPA
    public MemberCard() {
    }

    // Constructor with parameters
    public MemberCard(MembershipAccount conto, Customer customer) {
       this.conto = conto;
       this.customer = customer;
       // Automatically generate the card number
       this.numero = generateCardNumber();
    }

    /**
     * Gets the ID of the member card.
     *
     * @return The ID.
     */
    public int getId() {
       return id;
    }

    /**
     * Gets the automatically generated card number of the member card.
     *
     * @return The card number.
     */
    public String getNumero() {
       return numero;
    }

    /**
     * Sets the membership account associated with the member card.
     *
     * @param conto The new associated membership account.
     */
    public void setConto(MembershipAccount conto) {
       this.conto = conto;
    }

    /**
     * Gets the customer associated with the member card.
     *
     * @return The associated customer.
     */
    public Customer getCustomer() {
       return customer;
    }

    /**
     * Sets the customer associated with the member card.
     *
     * @param customer The new associated customer.
     */
    public void setCustomer(Customer customer) {
       this.customer = customer;
    }

    // Private method to generate the card number
    private String generateCardNumber() {
       // Use String.format to format the number with leading zeros
       return String.format("%08d", id);
    }

	public void setId(int id) {
		this.id = id;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}