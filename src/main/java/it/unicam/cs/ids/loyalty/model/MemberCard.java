package it.unicam.cs.ids.loyalty.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class MemberCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String cardNumber;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "membership_id", nullable = false)
	private Membership membership;

	public MemberCard() {
	}

	public MemberCard(Membership membership) {
		this.membership = membership;
	}

	public int getId() {
		return id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void SetCardNumber() {
		if (this.id != 0) {
			this.cardNumber = String.format("%08d", this.id);
		}
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Membership getMembership() {
		return membership;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
	}

	public boolean isCardValid() {
		if (this.membership == null) {
			return false;
		}

		LoyaltyProgram loyaltyProgram = this.membership.getLoyaltyProgram();
		if (loyaltyProgram == null) {
			return false;
		}

		LocalDate expirationDate = loyaltyProgram.getExpiringDate();
		if (expirationDate == null) {
			return false;
		}

		return !LocalDate.now().isAfter(expirationDate);
	}
}