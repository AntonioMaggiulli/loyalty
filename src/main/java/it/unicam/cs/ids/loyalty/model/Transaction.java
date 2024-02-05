package it.unicam.cs.ids.loyalty.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "loyaltyBenefit_id", nullable = false)
	private Benefit loyaltyBenefit;

	private int pointsEarned;
	private int pointsSpent;
	private double moneySpent;
	private LocalDateTime timestamp;

	@Enumerated(EnumType.STRING)
	private RewardStatus status;

	@ManyToOne
	@JoinColumn(name = "membership_account_id")
	private MembershipAccount membershipAccount;

	public Transaction() {

	}

	public Transaction(Benefit loyaltyBenefit, double eurosSpent, MembershipAccount account) {
		this.loyaltyBenefit = loyaltyBenefit;
		this.moneySpent = eurosSpent;
		this.membershipAccount = account;
		this.timestamp = LocalDateTime.now();
		if (loyaltyBenefit instanceof Reward) {
			this.status = RewardStatus.PENDING;
		} else if (loyaltyBenefit instanceof PointsReward || loyaltyBenefit instanceof Cashback
				|| loyaltyBenefit instanceof Coupon) {
			this.status = RewardStatus.DELIVERED;
		}
	}

	public int getId() {
		return id;
	}

	public Benefit getLoyaltyBenefit() {
		return loyaltyBenefit;
	}

	public int getPointsEarned() {
		return pointsEarned;
	}

	public int getPointsSpent() {
		return pointsSpent;
	}

	public double getMoneySpent() {
		return moneySpent;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setPointsEarned(int pointsEarned) {
		this.pointsEarned = pointsEarned;

	}

	public MembershipAccount getMembershipAccount() {
		return membershipAccount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLoyaltyBenefit(Benefit loyaltyBenefit) {
		this.loyaltyBenefit = loyaltyBenefit;
	}

	public void setPointsSpent(int pointsSpent) {
		this.pointsSpent = pointsSpent;
	}

	public void setEurosSpent(double eurosSpent) {
		this.moneySpent = eurosSpent;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setMembershipAccount(MembershipAccount membershipAccount) {
		this.membershipAccount = membershipAccount;
	}

	public RewardStatus getStatus() {
		return status;
	}

	public void setStatus(RewardStatus status) {
		this.status = status;
	}
}