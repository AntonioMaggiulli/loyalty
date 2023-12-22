package it.unicam.cs.ids.loyalty.model;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
 
/**
* JPA Entity representing a customer.
*/
@Entity
public class Customer {
 
    public Customer() {
		super();
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
 
    private String cognome;
    private String nome;
    private String codiceFiscale;
    private String email;
    private String telefono;
    private String indirizzo;
    private Date dateOfBirth;
    private String referralCode;
    
    @OneToMany(mappedBy = "writer")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "sender")
    private List<Invitation> invitations;

    @OneToMany(mappedBy = "customer")
    private List<Membership> memberships;
    /**
     * Gets the ID of the customer.
     *
     * @return The customer ID.
     */
    public int getId() {
       return id;
    }
 
    /**
     * Gets the last name of the customer.
     *
     * @return The customer's last name.
     */
    public String getCognome() {
       return cognome;
    }
 
    /**
     * Sets the last name of the customer.
     *
     * @param cognome The new last name.
     */
    public void setCognome(String cognome) {
       this.cognome = cognome;
    }
 
    /**
     * Gets the first name of the customer.
     *
     * @return The customer's first name.
     */
    public String getNome() {
       return nome;
    }
 
    /**
     * Sets the first name of the customer.
     *
     * @param nome The new first name.
     */
    public void setNome(String nome) {
       this.nome = nome;
    }

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getReferralCodeString() {
		return referralCode;
	}

	public void setReferralCodeString(String referralCodeString) {
		this.referralCode = referralCodeString;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public List<Invitation> getInvitations() {
		return invitations;
	}

	public void setInvitations(List<Invitation> invitations) {
		this.invitations = invitations;
	}

	public List<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void addFeedback(Feedback feedback) {
	    if (feedbacks == null) {
	        feedbacks = new ArrayList<>();
	    }
	    feedbacks.add(feedback);
	    feedback.setWriter(this);
	}
	public void removeFeedback(Feedback feedback) {
	    if (feedbacks != null) {
	        feedbacks.remove(feedback);
	        feedback.setWriter(null);
	    }
	}
	public void addInvitation(Invitation invitation) {
	    if (invitations == null) {
	        invitations = new ArrayList<>();
	    }
	    invitations.add(invitation);
	    invitation.setSender(this);
	}

	public void removeInvitation(Invitation invitation) {
	    if (invitations != null && invitations.contains(invitation)) {
	        invitations.remove(invitation);
	        invitation.setSender(null);
	    }
	}


}