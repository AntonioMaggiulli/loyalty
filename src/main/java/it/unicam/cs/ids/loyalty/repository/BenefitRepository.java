package it.unicam.cs.ids.loyalty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;

import java.util.List;

/**
 * Repository interface for managing Benefit entities.
 */
public interface BenefitRepository extends JpaRepository<Benefit, Integer> {

    /**
     * Retrieves Benefit entities by the offering merchant.
     *
     * @param offeringMerchant The offering merchant to search for.
     * @return List of Benefit entities offered by the specified merchant.
     */
    List<Benefit> findByOfferingMerchant(Merchant offeringMerchant);

    /**
     * Retrieves Benefit entities by the associated loyalty program.
     *
     * @param loyaltyProgram The associated loyalty program to search for.
     * @return List of Benefit entities associated with the specified loyalty program.
     */
    List<Benefit> findByLoyaltyProgram(LoyaltyProgram loyaltyProgram);
}

