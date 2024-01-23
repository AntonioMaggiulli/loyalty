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

/**
 * This class manages statistics related to the loyalty program.
 */
public class HandleStatistics {
	private final BenefitRepository benefitRepository;
	private final LoyaltyProgram loyaltyProgram;

	/**
	 * Constructor for the HandleStatistics class.
	 *
	 * @param benefitRepository Repository for benefits.
	 * @param loyaltyProgram    Loyalty program associated with the statistics.
	 */
	@Autowired
	public HandleStatistics(BenefitRepository benefitRepository, LoyaltyProgram loyaltyProgram) {
		this.benefitRepository = benefitRepository;
		this.loyaltyProgram = loyaltyProgram;
	}

	/**
	 * Returns the total number of customers in the loyalty program.
	 *
	 * @return Total number of customers.
	 */
	public int getNumberOfCustomers() {
		List<Membership> memberships = loyaltyProgram.getMemberships();
		return memberships.size();
	}

	/**
	 * Returns the total number of transactions made by all customers.
	 *
	 * @return Total number of transactions.
	 */
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

	/**
	 * Returns the total number of benefits in the loyalty program.
	 *
	 * @return Total number of benefits.
	 */
	public int getTotalBenefits() {
		return benefitRepository.findByLoyaltyProgram(loyaltyProgram).size();
	}

	/**
	 * Returns a map containing the count of benefits for each level.
	 *
	 * @return Map with the count of benefits for each level.
	 */
	public Map<Level, Integer> getBenefitsCountByLevel() {
		List<Benefit> benefits = benefitRepository.findByLoyaltyProgram(loyaltyProgram);
		Map<Level, Integer> benefitsCountByLevel = new HashMap<>();

		for (Benefit benefit : benefits) {
			Level associatedLevel = benefit.getAssociatedLevel();
			benefitsCountByLevel.put(associatedLevel, benefitsCountByLevel.getOrDefault(associatedLevel, 0) + 1);
		}

		return benefitsCountByLevel;
	}

	/**
	 * Returns the total number of points earned by all customers.
	 *
	 * @return Total number of points earned.
	 */
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

	/**
	 * Returns the average money spent for PointsReward benefits.
	 *
	 * @return Average money spent for PointsReward.
	 */
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

	/**
	 * Returns the total amount of money spent by all customers.
	 *
	 * @return Total amount of money spent.
	 */
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

	/**
	 * Returns a map containing the count of redeemed benefits for each type.
	 *
	 * @return Map with the count of redeemed benefits for each type.
	 */
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
