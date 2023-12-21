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
 
    private String cardNumber;
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
}