package it.unicam.cs.ids.loyalty.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.model.MembershipAccount;
import it.unicam.cs.ids.loyalty.model.PointsReward;
import it.unicam.cs.ids.loyalty.model.Transaction;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;

public class HandleStatistics {
	private final BenefitRepository benefitRepository;
	private final LoyaltyProgram loyaltyProgram;

	@Autowired
	public HandleStatistics(BenefitRepository benefitRepository, LoyaltyProgram loyaltyProgram) {
		this.benefitRepository = benefitRepository;
		this.loyaltyProgram = loyaltyProgram;
	}

	public int getNumberOfCustomers() {
		List<Membership> memberships = loyaltyProgram.getMemberships();
		return memberships.size();
	}

	public int getNumberOfTransactions() {
		List<Membership> memberships = loyaltyProgram.getMemberships();
		int totalTransactions = 0;

		for (Membership membership : memberships) {
			MembershipAccount membershipAccount = membership.getMembershipAccount();

			if (membershipAccount != null) {
				List<Transaction> transactions = membershipAccount.getTransactions();
				totalTransactions += transactions.size();
			}
		}

		return totalTransactions;
	}

	public int getTotalBenefits() {
		return benefitRepository.findByLoyaltyProgram(loyaltyProgram).size();
	}

	public Map<Level, Integer> getBenefitsCountByLevel() {
		List<Benefit> benefits = benefitRepository.findByLoyaltyProgram(loyaltyProgram);
		Map<Level, Integer> benefitsCountByLevel = new HashMap<>();

		for (Benefit benefit : benefits) {
			Level associatedLevel = benefit.getAssociatedLevel();
			benefitsCountByLevel.put(associatedLevel, benefitsCountByLevel.getOrDefault(associatedLevel, 0) + 1);
		}

		return benefitsCountByLevel;
	}

	public int getTotalPointsEarned() {
		List<Membership> memberships = loyaltyProgram.getMemberships();
		int totalPointsEarned = 0;

		for (Membership membership : memberships) {
			MembershipAccount membershipAccount = membership.getMembershipAccount();
			List<Transaction> transactions = membershipAccount.getTransactions();

			for (Transaction transaction : transactions) {
				totalPointsEarned += transaction.getPointsEarned();
			}
		}

		return totalPointsEarned;
	}

	public double getAverageMoneySpentForPointsReward() {
		List<Membership> memberships = loyaltyProgram.getMemberships();
		int totalTransactions = 0;
		double totalMoneySpent = 0;

		for (Membership membership : memberships) {
			MembershipAccount membershipAccount = membership.getMembershipAccount();
			List<Transaction> transactions = membershipAccount.getTransactions();

			for (Transaction transaction : transactions) {
				Benefit loyaltyBenefit = transaction.getLoyaltyBenefit();

				if (loyaltyBenefit instanceof PointsReward) {
					totalTransactions++;
					totalMoneySpent += transaction.getMoneySpent();
				}
			}
		}

		return (totalTransactions > 0) ? (totalMoneySpent / totalTransactions) : 0;
	}

	public double getTotalMoneySpent() {
		List<Membership> memberships = loyaltyProgram.getMemberships();
		double totalMoneySpent = 0;

		for (Membership membership : memberships) {
			MembershipAccount membershipAccount = membership.getMembershipAccount();
			List<Transaction> transactions = membershipAccount.getTransactions();

			for (Transaction transaction : transactions) {
				totalMoneySpent += transaction.getMoneySpent();
			}
		}
		return totalMoneySpent;
	}

	public Map<String, Integer> getRedeemedBenefitsByType() {
		List<Membership> memberships = loyaltyProgram.getMemberships();
		Map<String, Integer> redeemedBenefitsByType = new HashMap<>();

		for (Membership membership : memberships) {
			MembershipAccount membershipAccount = membership.getMembershipAccount();
			List<Transaction> transactions = membershipAccount.getTransactions();

			for (Transaction transaction : transactions) {
				Benefit loyaltyBenefit = transaction.getLoyaltyBenefit();
				String benefitType = loyaltyBenefit.getType();

				redeemedBenefitsByType.put(benefitType, redeemedBenefitsByType.getOrDefault(benefitType, 0) + 1);
			}
		}

		return redeemedBenefitsByType;
	}
}
