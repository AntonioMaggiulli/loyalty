package it.unicam.cs.ids.loyalty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.service.DefaultBenefitService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Benefit entities.
 */
@RestController
@RequestMapping("/api/benefits")
public class BenefitController {

    private final DefaultBenefitService benefitService;
	private final MerchantRepository merchantRepository;
	private final LoyaltyProgramRepository loyaltyProgramRepository;

    @Autowired
    public BenefitController(DefaultBenefitService benefitService, MerchantRepository merchantRepository, LoyaltyProgramRepository loyaltyProgramRepository) {
        this.benefitService = benefitService;
        this.merchantRepository = merchantRepository;
        this.loyaltyProgramRepository=loyaltyProgramRepository;
    }

    /**
     * Retrieves benefits offered by a specific merchant.
     *
     * @param merchantId The ID of the merchant.
     * @return List of Benefit entities offered by the specified merchant.
     */
    @GetMapping("/byMerchant/{merchantId}")
    public List<Benefit> getBenefitsByMerchant(@PathVariable int merchantId) {
    	Optional<Merchant> optionalMerchant = merchantRepository.findById(merchantId);
    	Merchant merchant;
		if (optionalMerchant.isPresent()) {
    	    merchant = optionalMerchant.get();
    	} else {
    		throw new EntityNotFoundException("Merchant not found with ID: " + merchantId);
    	}


		return benefitService.getBenefitsByOfferingMerchant(merchant);
    }

    /**
     * Retrieves benefits associated with a specific loyalty program.
     *
     * @param loyaltyProgramId The ID of the loyalty program.
     * @return List of Benefit entities associated with the specified loyalty program.
     */
    @GetMapping("/byLoyaltyProgram/{loyaltyProgramId}")
    public List<Benefit> getBenefitsByLoyaltyProgram(@PathVariable int loyaltyProgramId) {
    	LoyaltyProgram loyaltyProgram;
    	Optional<LoyaltyProgram> optionalLoyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId);
        if (optionalLoyaltyProgram.isPresent()) {
            loyaltyProgram = optionalLoyaltyProgram.get();
        } else {
        	throw new EntityNotFoundException("LoyaltyProgram not found with ID: " + loyaltyProgramId);
        }
		return benefitService.getBenefitsByLoyaltyProgram(loyaltyProgram);
    }

}
