package it.unicam.cs.ids.loyalty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;

public interface BenefitRepository extends JpaRepository<Benefit, Integer> {

	List<Benefit> findByOfferingMerchant(Merchant offeringMerchant);

	List<Benefit> findByLoyaltyProgram(LoyaltyProgram loyaltyProgram);

	List<Benefit> findByAssociatedLevel(Level level);

	List<Benefit> findByTypeAndLoyaltyProgramIdAndOfferingMerchantIdAndAssociatedLevelId(String type,
			int loyaltyProgramId, int offeringMerchantId, int associatedLevelId);

}
