package it.unicam.cs.ids.loyalty.model;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
 
/**
* JPA Entity representing a member card.
*/
@Entity
public class MemberCard {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
 
    private String cardNumber;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;
 
    // Default constructor required by JPA
    public MemberCard() {
    }
 
    // Constructor with parameters
    public MemberCard(Membership membership) {
       this.membership = membership;
       // Automatically generate the card number
       this.cardNumber = generateCardNumber();
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
       return cardNumber;
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
		this.cardNumber = numero;
	}
	 public Membership getMembership() {
	        return membership;
	    }

	    public void setMembership(Membership membership) {
	        this.membership = membership;
	    }
}