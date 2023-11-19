package it.unicam.cs.ids.loyalty.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

/**
 * Represents a partnership between a LoyaltyProgram and a Merchant.
 */
@Entity
public class Partnership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "loyalty_program_id")
    private LoyaltyProgram loyaltyProgram;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private Date startDate;

    /**
     * Default constructor.
     */
    public Partnership() {
    }

    /**
     * Constructs a Partnership with the specified details.
     *
     * @param loyaltyProgram The loyalty program involved in the partnership.
     * @param merchant       The merchant involved in the partnership.
     * @param startDate      The start date of the partnership.
     */
    public Partnership(LoyaltyProgram loyaltyProgram, Merchant merchant, Date startDate) {
        this.loyaltyProgram = loyaltyProgram;
        this.merchant = merchant;
        this.startDate = startDate;
    }

    /**
     * Gets the ID of the partnership.
     *
     * @return The ID of the partnership.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the partnership.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the loyalty program involved in the partnership.
     *
     * @return The loyalty program involved in the partnership.
     */
    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    /**
     * Sets the loyalty program involved in the partnership.
     *
     * @param loyaltyProgram The loyalty program to set.
     */
    public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }

    /**
     * Gets the merchant involved in the partnership.
     *
     * @return The merchant involved in the partnership.
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * Sets the merchant involved in the partnership.
     *
     * @param merchant The merchant to set.
     */
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    /**
     * Gets the start date of the partnership.
     *
     * @return The start date of the partnership.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the partnership.
     *
     * @param startDate The start date to set.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
