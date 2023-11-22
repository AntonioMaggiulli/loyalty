package it.unicam.cs.ids.loyalty.model;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String cognome;
    private String nome;

    @OneToMany(mappedBy = "customer")
    private List<MemberCard> cards;

    /**
     * Gets the ID of the customer.
     *
     * @return The customer ID.
     */
    public int getId() {
       return id;
    }

    /**
     * Gets the username of the customer.
     *
     * @return The customer's username.
     */
    public String getUsername() {
       return username;
    }

    /**
     * Sets the username of the customer.
     *
     * @param username The new username.
     */
    public void setUsername(String username) {
       this.username = username;
    }

    /**
     * Gets the password of the customer.
     *
     * @return The customer's password.
     */
    public String getPassword() {
       return password;
    }

    /**
     * Sets the password of the customer.
     *
     * @param password The new password.
     */
    public void setPassword(String password) {
       this.password = password;
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

    /**
     * Gets the list of member cards associated with the customer.
     *
     * @return The list of member cards.
     */
    public List<MemberCard> getTessereClientes() {
       return cards;
    }

    /**
     * Sets the list of member cards associated with the customer.
     *
     * @param tessereClientes The new list of member cards.
     */
    public void setTessereClientes(List<MemberCard> tessereClientes) {
       this.cards = tessereClientes;
    }
}